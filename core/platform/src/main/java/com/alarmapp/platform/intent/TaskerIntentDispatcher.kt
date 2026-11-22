package com.alarmapp.platform.intent

import android.content.Context
import android.content.Intent
import com.alarmapp.domain.model.Alarm
import com.alarmapp.domain.model.WebhookEvent

interface TaskerIntentDispatcher {
    fun dispatchEvent(event: WebhookEvent, alarm: Alarm)
}

class TaskerIntentDispatcherImpl(private val context: Context) : TaskerIntentDispatcher {

    override fun dispatchEvent(event: WebhookEvent, alarm: Alarm) {
        val intent = Intent(ACTION_ALARM_EVENT).apply {
            putExtra(EXTRA_EVENT, event.name)
            putExtra(EXTRA_ALARM_ID, alarm.id.value)
            putExtra(EXTRA_ALARM_LABEL, alarm.label)
            putExtra(EXTRA_ALARM_TIME, alarm.time.toString())
        }
        context.sendBroadcast(intent)
    }

    companion object {
        const val ACTION_ALARM_EVENT = "com.alarmapp.intent.action.ALARM_EVENT"
        const val EXTRA_EVENT = "event"
        const val EXTRA_ALARM_ID = "alarm_id"
        const val EXTRA_ALARM_LABEL = "alarm_label"
        const val EXTRA_ALARM_TIME = "alarm_time"
    }
}
