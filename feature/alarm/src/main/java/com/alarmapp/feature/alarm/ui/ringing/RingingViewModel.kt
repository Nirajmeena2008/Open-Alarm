package com.alarmapp.feature.alarm.ui.ringing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alarmapp.domain.model.*
import com.alarmapp.domain.repository.AlarmRepository
import com.alarmapp.domain.usecase.alarm.DismissAlarmUseCase
import com.alarmapp.domain.usecase.alarm.SnoozeAlarmUseCase
import com.alarmapp.domain.util.ChallengeGenerator
import com.alarmapp.domain.util.MathProblem
import com.alarmapp.platform.alarm.AlarmScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

sealed interface RingingChallengeState {
    data object None : RingingChallengeState

    data class MathChallenge(
        val problems: List<MathProblem>,
        val currentIndex: Int = 0,
        val currentInput: String = "",
        val isError: Boolean = false
    ) : RingingChallengeState

    data class MemoryChallenge(
        val sequence: List<Int>,
        val userSequence: List<Int> = emptyList(),
        val isShowingPattern: Boolean = true,
        val activeStep: Int = -1
    ) : RingingChallengeState

    data class ShakeChallenge(
        val currentShakes: Int = 0,
        val targetShakes: Int = 30
    ) : RingingChallengeState

    data class NfcChallenge(
        val expectedTagId: String,
        val isWaiting: Boolean = true
    ) : RingingChallengeState

    data class QrChallenge(
        val expectedPayload: String,
        val isMatched: Boolean = false
    ) : RingingChallengeState
}

data class RingingUiState(
    val alarm: Alarm? = null,
    val currentTimeString: String = "",
    val challengeState: RingingChallengeState = RingingChallengeState.None,
    val isSnoozed: Boolean = false,
    val isDismissed: Boolean = false,
    val errorMessage: String? = null
)

class RingingViewModel(
    private val alarmRepository: AlarmRepository,
    private val snoozeAlarmUseCase: SnoozeAlarmUseCase,
    private val dismissAlarmUseCase: DismissAlarmUseCase,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {

    private val _uiState = MutableStateFlow(RingingUiState())
    val uiState: StateFlow<RingingUiState> = _uiState.asStateFlow()

    fun initAlarm(alarmIdString: String) {
        viewModelScope.launch {
            val alarm = alarmRepository.getAlarm(AlarmId(alarmIdString))
            if (alarm != null) {
                val challengeState = setupChallenge(alarm.challengeConfig)
                _uiState.update {
                    it.copy(
                        alarm = alarm,
                        challengeState = challengeState
                    )
                }
            }
        }
    }

    private fun setupChallenge(config: ChallengeConfig?): RingingChallengeState {
        if (config == null) return RingingChallengeState.None
        val now = Clock.System.now().toEpochMilliseconds()

        return when (config) {
            is ChallengeConfig.Math -> {
                val problems = (0 until config.problemCount).map { i ->
                    ChallengeGenerator.generateMathProblem(config.difficulty, now + i * 17)
                }
                RingingChallengeState.MathChallenge(problems = problems)
            }
            is ChallengeConfig.Memory -> {
                val sequence = ChallengeGenerator.generateMemorySequence(
                    dimension = config.gridDimension,
                    sequenceLength = config.sequenceLength,
                    seed = now
                )
                RingingChallengeState.MemoryChallenge(sequence = sequence)
            }
            is ChallengeConfig.Shake -> {
                RingingChallengeState.ShakeChallenge(
                    currentShakes = 0,
                    targetShakes = config.targetShakes
                )
            }
            is ChallengeConfig.Nfc -> {
                RingingChallengeState.NfcChallenge(expectedTagId = config.registeredTagId)
            }
            is ChallengeConfig.Qr -> {
                RingingChallengeState.QrChallenge(expectedPayload = config.expectedPayload)
            }
            is ChallengeConfig.Plugin -> RingingChallengeState.None
        }
    }

    fun onMathInput(digit: String) {
        val curr = _uiState.value.challengeState as? RingingChallengeState.MathChallenge ?: return
        if (curr.currentInput.length < 5) {
            _uiState.update {
                it.copy(
                    challengeState = curr.copy(
                        currentInput = curr.currentInput + digit,
                        isError = false
                    )
                )
            }
        }
    }

    fun onMathBackspace() {
        val curr = _uiState.value.challengeState as? RingingChallengeState.MathChallenge ?: return
        if (curr.currentInput.isNotEmpty()) {
            _uiState.update {
                it.copy(
                    challengeState = curr.copy(
                        currentInput = curr.currentInput.dropLast(1),
                        isError = false
                    )
                )
            }
        }
    }

    fun onMathSubmit() {
        val curr = _uiState.value.challengeState as? RingingChallengeState.MathChallenge ?: return
        val currentProblem = curr.problems.getOrNull(curr.currentIndex) ?: return
        val entered = curr.currentInput.toIntOrNull()

        if (entered == currentProblem.answer) {
            if (curr.currentIndex + 1 >= curr.problems.size) {
                // All solved!
                dismiss()
            } else {
                _uiState.update {
                    it.copy(
                        challengeState = curr.copy(
                            currentIndex = curr.currentIndex + 1,
                            currentInput = "",
                            isError = false
                        )
                    )
                }
            }
        } else {
            _uiState.update {
                it.copy(
                    challengeState = curr.copy(
                        currentInput = "",
                        isError = true
                    )
                )
            }
        }
    }

    fun onMemoryCellClick(cellIndex: Int) {
        val curr = _uiState.value.challengeState as? RingingChallengeState.MemoryChallenge ?: return
        val nextStep = curr.userSequence.size
        if (curr.sequence.getOrNull(nextStep) == cellIndex) {
            val newUserSeq = curr.userSequence + cellIndex
            if (newUserSeq.size == curr.sequence.size) {
                // Completed memory challenge!
                dismiss()
            } else {
                _uiState.update {
                    it.copy(challengeState = curr.copy(userSequence = newUserSeq))
                }
            }
        } else {
            // Failed, reset user sequence
            _uiState.update {
                it.copy(challengeState = curr.copy(userSequence = emptyList()))
            }
        }
    }

    fun onShakeDetected() {
        val curr = _uiState.value.challengeState as? RingingChallengeState.ShakeChallenge ?: return
        val newShakes = curr.currentShakes + 1
        if (newShakes >= curr.targetShakes) {
            dismiss()
        } else {
            _uiState.update {
                it.copy(challengeState = curr.copy(currentShakes = newShakes))
            }
        }
    }

    fun onNfcScanned(scannedTagId: String) {
        val curr = _uiState.value.challengeState as? RingingChallengeState.NfcChallenge ?: return
        if (curr.expectedTagId.isBlank() || curr.expectedTagId == scannedTagId) {
            dismiss()
        }
    }

    fun onQrScanned(scannedPayload: String) {
        val curr = _uiState.value.challengeState as? RingingChallengeState.QrChallenge ?: return
        if (curr.expectedPayload.isBlank() || curr.expectedPayload == scannedPayload) {
            dismiss()
        }
    }

    fun snooze() {
        val alarm = _uiState.value.alarm
        if (alarm == null) {
            _uiState.update { it.copy(isSnoozed = true) }
            return
        }
        viewModelScope.launch {
            val result = snoozeAlarmUseCase(alarm.id)
            if (result is SnoozeAlarmUseCase.Result.Snoozed) {
                alarmScheduler.schedule(alarm.copy(nextTriggerTime = result.nextTrigger))
            }
            _uiState.update { it.copy(isSnoozed = true) }
        }
    }

    fun dismiss() {
        val alarm = _uiState.value.alarm
        if (alarm == null) {
            _uiState.update { it.copy(isDismissed = true) }
            return
        }
        viewModelScope.launch {
            dismissAlarmUseCase(alarm.id)
            val updated = alarmRepository.getAlarm(alarm.id)
            if (updated != null && updated.isEnabled && updated.isRepeating) {
                alarmScheduler.schedule(updated)
            }
            _uiState.update { it.copy(isDismissed = true) }
        }
    }
}
