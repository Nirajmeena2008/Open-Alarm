package com.alarmapp.feature.sleep

import com.alarmapp.domain.model.Routine
import com.alarmapp.domain.model.RoutineStep
import com.alarmapp.domain.model.SleepSession
import com.alarmapp.domain.repository.AlarmRepository
import com.alarmapp.domain.repository.RoutineRepository
import com.alarmapp.domain.repository.SleepRepository
import com.alarmapp.domain.usecase.sleep.EndSleepSessionUseCase
import com.alarmapp.domain.usecase.sleep.GetSleepHistoryUseCase
import com.alarmapp.domain.usecase.sleep.RoutineUseCases
import com.alarmapp.domain.usecase.sleep.StartSleepSessionUseCase
import com.alarmapp.feature.sleep.ui.SleepViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import kotlinx.datetime.Instant
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SleepViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val sleepRepo: SleepRepository = mockk(relaxed = true)
    private val routineRepo: RoutineRepository = mockk(relaxed = true)
    private val alarmRepo: AlarmRepository = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `startSleep creates new session and updates activeSession`() = runTest {
        val startSession = StartSleepSessionUseCase(sleepRepo)
        val endSession = EndSleepSessionUseCase(sleepRepo)
        val history = GetSleepHistoryUseCase(sleepRepo)
        val routines = RoutineUseCases(routineRepo)

        coEvery { sleepRepo.getAllSessions() } returns flowOf(emptyList())
        coEvery { routineRepo.getRoutines() } returns flowOf(emptyList())

        val viewModel = SleepViewModel(
            sleepRepo, routineRepo, alarmRepo,
            startSession, endSession, history, routines
        )

        viewModel.startSleep()
        testScheduler.advanceUntilIdle()

        coVerify { sleepRepo.saveSession(any()) }
    }

    @Test
    fun `routine step progression moves forward until end`() = runTest {
        val startSession = StartSleepSessionUseCase(sleepRepo)
        val endSession = EndSleepSessionUseCase(sleepRepo)
        val history = GetSleepHistoryUseCase(sleepRepo)
        val routines = RoutineUseCases(routineRepo)

        coEvery { sleepRepo.getAllSessions() } returns flowOf(emptyList())
        coEvery { routineRepo.getRoutines() } returns flowOf(emptyList())

        val viewModel = SleepViewModel(
            sleepRepo, routineRepo, alarmRepo,
            startSession, endSession, history, routines
        )
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        val testRoutine = Routine(
            id = "r1",
            name = "Evening",
            steps = listOf(
                RoutineStep("s1", "Step 1", 5, null, 1),
                RoutineStep("s2", "Step 2", 10, null, 2)
            )
        )

        viewModel.startRoutine(testRoutine)
        testScheduler.advanceUntilIdle()
        assertThat(viewModel.uiState.value.activeRoutine?.id).isEqualTo("r1")
        assertThat(viewModel.uiState.value.currentStepIndex).isEqualTo(0)

        viewModel.nextRoutineStep()
        testScheduler.advanceUntilIdle()
        assertThat(viewModel.uiState.value.currentStepIndex).isEqualTo(1)

        viewModel.nextRoutineStep()
        testScheduler.advanceUntilIdle()
        assertThat(viewModel.uiState.value.activeRoutine).isNull()
    }
}
