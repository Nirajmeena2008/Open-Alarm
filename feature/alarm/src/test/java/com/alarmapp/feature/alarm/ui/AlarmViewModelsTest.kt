package com.alarmapp.feature.alarm.ui

import com.alarmapp.domain.model.Alarm
import com.alarmapp.domain.model.AlarmId
import com.alarmapp.domain.model.DayOfWeek
import com.alarmapp.domain.repository.AlarmRepository
import com.alarmapp.domain.usecase.alarm.DeleteAlarmUseCase
import com.alarmapp.domain.usecase.alarm.GetAlarmsUseCase
import com.alarmapp.domain.usecase.alarm.SaveAlarmUseCase
import com.alarmapp.domain.usecase.alarm.ToggleAlarmUseCase
import com.alarmapp.feature.alarm.ui.editor.AlarmEditorViewModel
import com.alarmapp.feature.alarm.ui.list.AlarmListViewModel
import com.alarmapp.platform.alarm.AlarmScheduler
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalTime
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AlarmViewModelsTest {

    private val testDispatcher = StandardTestDispatcher()
    private val repository: AlarmRepository = mockk(relaxed = true)
    private val scheduler: AlarmScheduler = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `toggleAlarm disables alarm when toggled false`() = runTest {
        val toggleAlarmUseCase = ToggleAlarmUseCase(repository)
        val getAlarmsUseCase = GetAlarmsUseCase(repository)
        val deleteAlarmUseCase = DeleteAlarmUseCase(repository)
        val viewModel = AlarmListViewModel(
            repository,
            getAlarmsUseCase,
            toggleAlarmUseCase,
            deleteAlarmUseCase,
            scheduler
        )

        val alarmId = AlarmId("alarm-1")
        val alarm = Alarm(
            id = alarmId,
            time = LocalTime(8, 0),
            isEnabled = false,
            createdAt = Instant.parse("2026-11-22T00:00:00Z"),
            updatedAt = Instant.parse("2026-11-22T00:00:00Z")
        )
        coEvery { repository.getAlarm(alarmId) } returns alarm

        viewModel.toggleAlarm(alarmId, false)
        testScheduler.advanceUntilIdle()

        coVerify { repository.updateAlarmEnabled(alarmId, false) }
        verify { scheduler.cancel(alarmId) }
    }

    @Test
    fun `AlarmEditorViewModel saves new alarm`() = runTest {
        val saveAlarmUseCase = SaveAlarmUseCase(repository)
        val viewModel = AlarmEditorViewModel(repository, saveAlarmUseCase, scheduler)

        viewModel.setTime(6, 45)
        viewModel.setLabel("Morning Run")
        viewModel.toggleDay(DayOfWeek.MONDAY)

        coEvery { repository.upsertAlarm(any()) } returns AlarmId("gen-1")
        coEvery { repository.getAlarm(any()) } returns Alarm(
            id = AlarmId("gen-1"),
            time = LocalTime(6, 45),
            label = "Morning Run",
            isEnabled = true,
            createdAt = Instant.parse("2026-11-22T00:00:00Z"),
            updatedAt = Instant.parse("2026-11-22T00:00:00Z")
        )

        var saved = false
        viewModel.saveAlarm { saved = true }
        testScheduler.advanceUntilIdle()

        assertThat(saved).isTrue()
        coVerify { repository.upsertAlarm(match { it.label == "Morning Run" && it.time == LocalTime(6, 45) }) }
    }
}
