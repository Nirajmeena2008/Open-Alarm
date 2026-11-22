package com.alarmapp.platform.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import com.alarmapp.domain.model.Alarm
import com.alarmapp.domain.model.AlarmId
import com.alarmapp.domain.repository.AlarmRepository
import com.alarmapp.domain.util.NextOccurrenceCalculator
import com.alarmapp.platform.notification.AlarmNotificationManagerImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

interface AlarmScheduler {
    fun schedule(alarm: Alarm)
    fun cancel(alarmId: AlarmId)
    suspend fun rescheduleAll()
}

class AlarmSchedulerImpl(
    private val context: Context,
    private val alarmRepository: AlarmRepository
) : AlarmScheduler {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

    override fun schedule(alarm: Alarm) {
        val triggerInstant = alarm.nextTriggerTime ?: return
        if (!alarm.isEnabled) return

        val am = alarmManager ?: return
        val pendingIntent = createAlarmPendingIntent(alarm.id) ?: return
        val triggerMillis = triggerInstant.toEpochMilliseconds()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!am.canScheduleExactAlarms()) {
                am.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerMillis,
                    pendingIntent
                )
                return
            }
        }

        // Use AlarmClockInfo for 99.99% reliability through Doze and battery saver
        val showPendingIntent = createShowPendingIntent(alarm.id)
        if (showPendingIntent != null) {
            val clockInfo = AlarmManager.AlarmClockInfo(triggerMillis, showPendingIntent)
            am.setAlarmClock(clockInfo, pendingIntent)
        } else {
            am.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerMillis,
                pendingIntent
            )
        }
    }

    override fun cancel(alarmId: AlarmId) {
        val pendingIntent = createAlarmPendingIntent(alarmId) ?: return
        alarmManager?.cancel(pendingIntent)
    }

    override suspend fun rescheduleAll() {
        val enabledAlarms = alarmRepository.getEnabledAlarms().first()
        val now = Clock.System.now()

        for (alarm in enabledAlarms) {
            val nextTrigger = NextOccurrenceCalculator.calculateNextOccurrence(
                targetTime = alarm.time,
                daysOfWeek = alarm.daysOfWeek,
                fromInstant = now
            )
            alarmRepository.updateNextTrigger(alarm.id, nextTrigger)
            schedule(alarm.copy(nextTriggerTime = nextTrigger))
        }
    }

    private fun createAlarmPendingIntent(alarmId: AlarmId): PendingIntent? {
        val intent = Intent(context, AlarmBroadcastReceiver::class.java).apply {
            action = ACTION_ALARM_TRIGGER
            putExtra(EXTRA_ALARM_ID, alarmId.value)
        }
        return PendingIntent.getBroadcast(
            context,
            alarmId.value.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun createShowPendingIntent(alarmId: AlarmId): PendingIntent? {
        val showIntent = Intent().apply {
            setPackage(context.packageName)
            action = ACTION_ALARM_RINGING
            setClassName(context.packageName, "com.alarmapp.feature.alarm.ui.ringing.RingingActivity")
            putExtra(EXTRA_ALARM_ID, alarmId.value)
        }
        return PendingIntent.getActivity(
            context,
            alarmId.value.hashCode(),
            showIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    companion object {
        const val ACTION_ALARM_TRIGGER = "com.alarmapp.action.ALARM_TRIGGER"
        const val ACTION_ALARM_RINGING = "com.alarmapp.action.ALARM_RINGING"
        const val EXTRA_ALARM_ID = "com.alarmapp.extra.ALARM_ID"
    }
}

class AlarmBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val alarmId = intent.getStringExtra(AlarmSchedulerImpl.EXTRA_ALARM_ID) ?: return

        // 1. Wake the CPU briefly so device stays awake during transition
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as? PowerManager
        val wakeLock = powerManager?.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
            "openalarm:ringing_wakelock"
        )
        wakeLock?.acquire(10 * 1000L)

        // 2. Post high-priority notification with full-screen intent
        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val db = com.alarmapp.data.database.AlarmDatabase.getInstance(context)
                val repo = com.alarmapp.data.repository.AlarmRepositoryImpl(db.alarmDao())
                val alarm = repo.getAlarm(AlarmId(alarmId))
                if (alarm != null) {
                    val notificationManager = AlarmNotificationManagerImpl(context)
                    notificationManager.showRingingNotification(alarm)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                pendingResult.finish()
            }
        }

        // 3. Launch full-screen ringing activity directly
        val ringIntent = Intent().apply {
            setPackage(context.packageName)
            action = AlarmSchedulerImpl.ACTION_ALARM_RINGING
            setClassName(context.packageName, "com.alarmapp.feature.alarm.ui.ringing.RingingActivity")
            putExtra(AlarmSchedulerImpl.EXTRA_ALARM_ID, alarmId)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NO_USER_ACTION
        }
        try {
            context.startActivity(ringIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED ||
            intent.action == Intent.ACTION_LOCKED_BOOT_COMPLETED ||
            intent.action == Intent.ACTION_MY_PACKAGE_REPLACED
        ) {
            // Kick off background reschedule
            val pendingResult = goAsync()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val db = com.alarmapp.data.database.AlarmDatabase.create(context)
                    val repo = com.alarmapp.data.repository.AlarmRepositoryImpl(db.alarmDao())
                    val scheduler = AlarmSchedulerImpl(context, repo)
                    scheduler.rescheduleAll()
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
}

class TimeZoneChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_TIMEZONE_CHANGED ||
            intent.action == Intent.ACTION_TIME_CHANGED
        ) {
            val pendingResult = goAsync()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val db = com.alarmapp.data.database.AlarmDatabase.create(context)
                    val repo = com.alarmapp.data.repository.AlarmRepositoryImpl(db.alarmDao())
                    val scheduler = AlarmSchedulerImpl(context, repo)
                    scheduler.rescheduleAll()
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
}
