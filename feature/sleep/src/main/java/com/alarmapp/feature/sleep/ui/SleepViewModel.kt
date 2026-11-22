package com.alarmapp.feature.sleep.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alarmapp.domain.model.*
import com.alarmapp.domain.repository.AlarmRepository
import com.alarmapp.domain.repository.RoutineRepository
import com.alarmapp.domain.repository.SleepRepository
import com.alarmapp.domain.usecase.sleep.EndSleepSessionUseCase
import com.alarmapp.domain.usecase.sleep.GetSleepHistoryUseCase
import com.alarmapp.domain.usecase.sleep.RoutineUseCases
import com.alarmapp.domain.usecase.sleep.StartSleepSessionUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

data class SleepUiState(
    val activeSession: SleepSession? = null,
    val sessions: List<SleepSession> = emptyList(),
    val routines: List<Routine> = emptyList(),
    val targetSleepHours: Double = 8.0,
    val recommendedBedtime: String? = "11:00 PM",
    val sleepDebtHours: Double = 0.0,
    val activeRoutine: Routine? = null,
    val currentStepIndex: Int = 0
)

class SleepViewModel(
    private val sleepRepository: SleepRepository,
    private val routineRepository: RoutineRepository,
    private val alarmRepository: AlarmRepository,
    private val startSleepSessionUseCase: StartSleepSessionUseCase,
    private val endSleepSessionUseCase: EndSleepSessionUseCase,
    private val getSleepHistoryUseCase: GetSleepHistoryUseCase,
    private val routineUseCases: RoutineUseCases
) : ViewModel() {

    private val _activeSession = MutableStateFlow<SleepSession?>(null)
    private val _activeRoutine = MutableStateFlow<Routine?>(null)
    private val _currentStepIndex = MutableStateFlow(0)

    val uiState: StateFlow<SleepUiState> = combine(
        getSleepHistoryUseCase(),
        routineUseCases.getRoutines(),
        _activeSession,
        _activeRoutine,
        _currentStepIndex
    ) { sessions, routines, activeSession, activeRoutine, stepIndex ->
        // Calculate average sleep and sleep debt
        val recentCompleted = sessions.filter { it.endTime != null }.take(7)
        val targetMinutesPerDay = 8.0 * 60
        val actualMinutesPerDay = if (recentCompleted.isNotEmpty()) {
            recentCompleted.map { it.durationMinutes }.average()
        } else {
            targetMinutesPerDay
        }
        val debtMinutes = (targetMinutesPerDay - actualMinutesPerDay).coerceAtLeast(0.0)
        val debtHours = debtMinutes / 60.0

        SleepUiState(
            activeSession = activeSession,
            sessions = sessions,
            routines = routines,
            sleepDebtHours = (debtHours * 10).toInt() / 10.0,
            activeRoutine = activeRoutine,
            currentStepIndex = stepIndex
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SleepUiState()
    )

    fun startSleep() {
        viewModelScope.launch {
            val session = startSleepSessionUseCase()
            _activeSession.value = session
        }
    }

    fun endSleep(quality: SleepQuality = SleepQuality.GOOD, notes: String? = null) {
        val session = _activeSession.value ?: return
        viewModelScope.launch {
            endSleepSessionUseCase(session.id, quality, notes)
            _activeSession.value = null
        }
    }

    fun startRoutine(routine: Routine) {
        _activeRoutine.value = routine
        _currentStepIndex.value = 0
    }

    fun nextRoutineStep() {
        val routine = _activeRoutine.value ?: return
        if (_currentStepIndex.value + 1 < routine.steps.size) {
            _currentStepIndex.value += 1
        } else {
            _activeRoutine.value = null
            _currentStepIndex.value = 0
        }
    }

    fun stopRoutine() {
        _activeRoutine.value = null
        _currentStepIndex.value = 0
    }
}
