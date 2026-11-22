package com.alarmapp.domain.usecase.alarm

import com.alarmapp.domain.model.Alarm
import com.alarmapp.domain.model.AlarmId
import com.alarmapp.domain.model.DayOfWeek
import com.alarmapp.domain.repository.AlarmRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalTime
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AlarmUseCasesTest {

    private val repository: AlarmRepository = mockk(relaxed = true)
    private val fixedNow = Instant.parse("2026-11-23T08:00:00Z")
    private val testClock = object : Clock {
        override fun now(): Instant = fixedNow
    }

    private lateinit var saveAlarmUseCase: SaveAlarmUseCase
    private lateinit var snoozeAlarmUseCase: SnoozeAlarmUseCase
    private lateinit var dismissAlarmUseCase: DismissAlarmUseCase
    private lateinit var toggleAlarmUseCase: ToggleAlarmUseCase

    private val testAlarm = Alarm(
        id = AlarmId("alarm-1"),
        label = "Morning",
        time = LocalTime(8, 30),
        isEnabled = true,
        daysOfWeek = setOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY),
        snoozeDurationMinutes = 5,
        maxSnoozeCount = 2,
        currentSnoozeCount = 0,
        createdAt = fixedNow,
        updatedAt = fixedNow
    )

    @BeforeEach
    fun setup() {
        saveAlarmUseCase = SaveAlarmUseCase(repository, testClock)
        snoozeAlarmUseCase = SnoozeAlarmUseCase(repository, testClock)
        dismissAlarmUseCase = DismissAlarmUseCase(repository, testClock)
        toggleAlarmUseCase = ToggleAlarmUseCase(repository, testClock)

        coEvery { repository.getAlarm(AlarmId("alarm-1")) } returns testAlarm
    }

    @Test
    fun `saveAlarm sets nextTriggerTime if enabled`() = runTest {
        coEvery { repository.upsertAlarm(any()) } answers { firstArg<Alarm>().id }

        val resultId = saveAlarmUseCase(testAlarm)
        assertThat(resultId).isEqualTo(AlarmId("alarm-1"))

        coVerify {
            repository.upsertAlarm(match {
                it.nextTriggerTime != null && it.isEnabled
            })
        }
    }

    @Test
    fun `snoozeAlarm increments count and updates trigger`() = runTest {
        val result = snoozeAlarmUseCase(AlarmId("alarm-1"))

        assertThat(result).isInstanceOf(SnoozeAlarmUseCase.Result.Snoozed::class.java)
        val snoozed = result as SnoozeAlarmUseCase.Result.Snoozed
        assertThat(snoozed.snoozeCount).isEqualTo(1)
        assertThat(snoozed.nextTrigger).isEqualTo(Instant.parse("2026-11-23T08:05:00Z"))

        coVerify {
            repository.updateSnoozeState(AlarmId("alarm-1"), 1, Instant.parse("2026-11-23T08:05:00Z"))
        }
    }

    @Test
    fun `snoozeAlarm returns MaxSnoozeReached when limit hit`() = runTest {
        val maxedAlarm = testAlarm.copy(currentSnoozeCount = 2)
        coEvery { repository.getAlarm(AlarmId("alarm-1")) } returns maxedAlarm

        val result = snoozeAlarmUseCase(AlarmId("alarm-1"))
        assertThat(result).isEqualTo(SnoozeAlarmUseCase.Result.MaxSnoozeReached)
    }

    @Test
    fun `dismissAlarm resets snooze count and reschedules repeating alarm`() = runTest {
        dismissAlarmUseCase(AlarmId("alarm-1"))

        coVerify {
            repository.updateSnoozeState(
                AlarmId("alarm-1"),
                snoozeCount = 0,
                nextTrigger = match { it.toEpochMilliseconds() > 0 }
            )
        }
    }

    @Test
    fun `dismissAlarm disables one-shot alarm`() = runTest {
        val oneShot = testAlarm.copy(daysOfWeek = emptySet())
        coEvery { repository.getAlarm(AlarmId("alarm-1")) } returns oneShot

        dismissAlarmUseCase(AlarmId("alarm-1"))

        coVerify {
            repository.updateAlarmEnabled(AlarmId("alarm-1"), false)
            repository.updateSnoozeState(AlarmId("alarm-1"), 0, null)
        }
    }
}
