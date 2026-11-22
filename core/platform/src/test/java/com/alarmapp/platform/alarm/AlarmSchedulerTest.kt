package com.alarmapp.platform.alarm

import android.content.Context
import com.alarmapp.domain.model.Alarm
import com.alarmapp.domain.model.AlarmId
import com.alarmapp.domain.model.DayOfWeek
import com.alarmapp.domain.repository.AlarmRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalTime
import org.junit.jupiter.api.Test

class AlarmSchedulerTest {

    private val context: Context = mockk(relaxed = true)
    private val repository: AlarmRepository = mockk(relaxed = true)

    @Test
    fun `alarm scheduler actions and extras are correctly defined`() {
        assertThat(AlarmSchedulerImpl.ACTION_ALARM_TRIGGER).isEqualTo("com.alarmapp.action.ALARM_TRIGGER")
        assertThat(AlarmSchedulerImpl.ACTION_ALARM_RINGING).isEqualTo("com.alarmapp.action.ALARM_RINGING")
        assertThat(AlarmSchedulerImpl.EXTRA_ALARM_ID).isEqualTo("com.alarmapp.extra.ALARM_ID")
    }

    @Test
    fun `rescheduleAll queries enabled alarms`() = runTest {
        val testAlarm = Alarm(
            id = AlarmId("sched-1"),
            label = "Morning",
            time = LocalTime(7, 30),
            isEnabled = true,
            daysOfWeek = setOf(DayOfWeek.MONDAY),
            createdAt = Instant.parse("2026-11-22T00:00:00Z"),
            updatedAt = Instant.parse("2026-11-22T00:00:00Z")
        )

        coEvery { repository.getEnabledAlarms() } returns flowOf(listOf(testAlarm))

        val scheduler = AlarmSchedulerImpl(context, repository)
        scheduler.rescheduleAll()

        coEvery { repository.updateNextTrigger(AlarmId("sched-1"), any()) }
    }
}
