package com.alarmapp.platform.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.alarmapp.domain.model.Alarm
import com.alarmapp.platform.alarm.AlarmSchedulerImpl

object AlarmNotificationChannels {
    const val CHANNEL_ALARM = "alarm_channel"
    const val CHANNEL_SLEEP = "sleep_channel"
    const val CHANNEL_DEVELOPER = "developer_channel"

    fun createChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val alarmChannel = NotificationChannel(
                CHANNEL_ALARM,
                "Alarm Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Critical alarm alerts and full screen ringing triggers"
                enableVibration(true)
                setBypassDnd(true)
            }

            val sleepChannel = NotificationChannel(
                CHANNEL_SLEEP,
                "Sleep & Bedtime",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Bedtime reminders and sleep tracking notifications"
            }

            val developerChannel = NotificationChannel(
                CHANNEL_DEVELOPER,
                "Developer Service",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Foreground status for local HTTP / MQTT server"
            }

            notificationManager.createNotificationChannels(listOf(alarmChannel, sleepChannel, developerChannel))
        }
    }
}

interface AlarmNotificationManager {
    fun showRingingNotification(alarm: Alarm)
    fun dismissRingingNotification(alarmId: String)
    fun showBedtimeReminder(reminderText: String)
}

class AlarmNotificationManagerImpl(private val context: Context) : AlarmNotificationManager {

    init {
        AlarmNotificationChannels.createChannels(context)
    }

    override fun showRingingNotification(alarm: Alarm) {
        val fullScreenIntent = Intent().apply {
            setPackage(context.packageName)
            action = AlarmSchedulerImpl.ACTION_ALARM_RINGING
            setClassName(context.packageName, "com.alarmapp.feature.alarm.ui.ringing.RingingActivity")
            putExtra(AlarmSchedulerImpl.EXTRA_ALARM_ID, alarm.id.value)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val fullScreenPendingIntent = PendingIntent.getActivity(
            context,
            alarm.id.value.hashCode(),
            fullScreenIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, AlarmNotificationChannels.CHANNEL_ALARM)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle(alarm.label.ifBlank { "Alarm" })
            .setContentText("Tap to dismiss or solve challenge")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOngoing(true)
            .setAutoCancel(false)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(alarm.id.value.hashCode(), notification)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    override fun dismissRingingNotification(alarmId: String) {
        try {
            NotificationManagerCompat.from(context).cancel(alarmId.hashCode())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun showBedtimeReminder(reminderText: String) {
        val notification = NotificationCompat.Builder(context, AlarmNotificationChannels.CHANNEL_SLEEP)
            .setSmallIcon(android.R.drawable.ic_menu_recent_history)
            .setContentTitle("Bedtime Routine")
            .setContentText(reminderText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(9999, notification)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}
