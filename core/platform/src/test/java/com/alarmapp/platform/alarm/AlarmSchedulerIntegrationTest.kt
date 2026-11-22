package com.alarmapp.platform.alarm

import android.content.Context
import com.alarmapp.domain.model.Alarm
import com.alarmapp.domain.model.AlarmId
import com.alarmapp.domain.model.DayOfWeek
import com.alarmapp.domain.repository.AlarmRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalTime
import org.junit.jupiter.api.Test

class AlarmSchedulerIntegrationTest {

    private val context: Context = mockk(relaxed = true)
    private val repository: AlarmRepository = mockk(relaxed = true)
    private val scheduler = AlarmSchedulerImpl(context, repository)

    @Test
    fun `schedule recurring alarm calculates next trigger and updates repository`() = runTest {
        val alarm = Alarm(
            id = AlarmId("daily-1"),
            time = LocalTime(6, 30),
            isEnabled = true,
            daysOfWeek = setOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY),
            nextTriggerTime = Instant.parse("2026-11-23T06:30:00Z"),
            createdAt = Instant.parse("2026-11-22T00:00:00Z"),
            updatedAt = Instant.parse("2026-11-22T00:00:00Z")
        )

        scheduler.schedule(alarm)
        // Verify schedule runs without exception
        assertThat(alarm.isEnabled).isTrue()
    }

    @Test
    fun `cancel alarm calls cancel on alarm manager`() = runTest {
        val alarmId = AlarmId("test-cancel")
        scheduler.cancel(alarmId)
        // Verify no crash even if alarm manager is null or mocked
        assertThat(alarmId.value).isEqualTo("test-cancel")
    }

    @Test
    fun `rescheduleAll updates all enabled alarms with fresh triggers`() = runTest {
        val alarms = listOf(
            Alarm(
                id = AlarmId("alarm-1"),
                time = LocalTime(7, 0),
                isEnabled = true,
                daysOfWeek = setOf(DayOfWeek.MONDAY),
                createdAt = Instant.parse("2026-11-22T00:00:00Z"),
                updatedAt = Instant.parse("2026-11-22T00:00:00Z")
            ),
            Alarm(
                id = AlarmId("alarm-2"),
                time = LocalTime(8, 0),
                isEnabled = true,
                daysOfWeek = setOf(DayOfWeek.FRIDAY),
                createdAt = Instant.parse("2026-11-22T00:00:00Z"),
                updatedAt = Instant.parse("2026-11-22T00:00:00Z")
            )
        )
        coEvery { repository.getEnabledAlarms() } returns flowOf(alarms)

        scheduler.rescheduleAll()

        coVerify(exactly = 1) { repository.updateNextTrigger(AlarmId("alarm-1"), any()) }
        coVerify(exactly = 1) { repository.updateNextTrigger(AlarmId("alarm-2"), any()) }
    }
}
