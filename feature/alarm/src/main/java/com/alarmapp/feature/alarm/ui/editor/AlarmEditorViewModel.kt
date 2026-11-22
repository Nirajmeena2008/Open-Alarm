package com.alarmapp.feature.alarm.ui.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alarmapp.domain.model.*
import com.alarmapp.domain.repository.AlarmRepository
import com.alarmapp.domain.usecase.alarm.SaveAlarmUseCase
import com.alarmapp.platform.alarm.AlarmScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalTime

data class AlarmEditorState(
    val alarmId: AlarmId? = null,
    val time: LocalTime = LocalTime(7, 0),
    val daysOfWeek: Set<DayOfWeek> = emptySet(),
    val isEnabled: Boolean = true,
    val label: String = "",
    val ringtone: RingtoneSource = RingtoneSource.Default,
    val volumeRamp: VolumeRamp? = VolumeRamp(),
    val vibrationPattern: VibrationPattern = VibrationPattern.STANDARD,
    val challengeConfig: ChallengeConfig? = null,
    val snoozeDurationMinutes: Int = 5,
    val isSaving: Boolean = false
)

class AlarmEditorViewModel(
    private val alarmRepository: AlarmRepository,
    private val saveAlarmUseCase: SaveAlarmUseCase,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {

    private val _state = MutableStateFlow(AlarmEditorState())
    val state: StateFlow<AlarmEditorState> = _state.asStateFlow()

    fun loadAlarm(alarmIdString: String?) {
        if (alarmIdString == null || alarmIdString == "new") {
            _state.value = AlarmEditorState()
            return
        }

        viewModelScope.launch {
            val alarm = alarmRepository.getAlarm(AlarmId(alarmIdString))
            if (alarm != null) {
                _state.value = AlarmEditorState(
                    alarmId = alarm.id,
                    time = alarm.time,
                    daysOfWeek = alarm.daysOfWeek,
                    isEnabled = alarm.isEnabled,
                    label = alarm.label,
                    ringtone = alarm.ringtone,
                    volumeRamp = alarm.volumeRamp,
                    vibrationPattern = alarm.vibration,
                    challengeConfig = alarm.challengeConfig,
                    snoozeDurationMinutes = alarm.snoozeDurationMinutes
                )
            }
        }
    }

    fun setTime(hour: Int, minute: Int) {
        _state.update { it.copy(time = LocalTime(hour, minute)) }
    }

    fun toggleDay(day: DayOfWeek) {
        _state.update { curr ->
            val updated = if (curr.daysOfWeek.contains(day)) {
                curr.daysOfWeek - day
            } else {
                curr.daysOfWeek + day
            }
            curr.copy(daysOfWeek = updated)
        }
    }

    fun setLabel(label: String) {
        _state.update { it.copy(label = label) }
    }

    fun setRingtone(ringtone: RingtoneSource) {
        _state.update { it.copy(ringtone = ringtone) }
    }

    fun setVolumeRamp(volumeRamp: VolumeRamp?) {
        _state.update { it.copy(volumeRamp = volumeRamp) }
    }

    fun setVibration(pattern: VibrationPattern) {
        _state.update { it.copy(vibrationPattern = pattern) }
    }

    fun setChallenge(config: ChallengeConfig?) {
        _state.update { it.copy(challengeConfig = config) }
    }

    fun setSnoozeDuration(minutes: Int) {
        _state.update { it.copy(snoozeDurationMinutes = minutes) }
    }

    fun saveAlarm(onSaved: () -> Unit) {
        val s = _state.value
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            val now = Clock.System.now()
            val alarm = Alarm(
                id = s.alarmId ?: AlarmId(""),
                label = s.label.ifBlank { "Alarm" },
                time = s.time,
                isEnabled = true,
                daysOfWeek = s.daysOfWeek,
                ringtone = s.ringtone,
                volumeRamp = s.volumeRamp,
                vibration = s.vibrationPattern,
                challengeConfig = s.challengeConfig,
                dismissType = when (s.challengeConfig) {
                    is ChallengeConfig.Math -> DismissType.MATH
                    is ChallengeConfig.Memory -> DismissType.MEMORY
                    is ChallengeConfig.Shake -> DismissType.SHAKE
                    is ChallengeConfig.Nfc -> DismissType.NFC
                    is ChallengeConfig.Qr -> DismissType.QR
                    is ChallengeConfig.Plugin -> DismissType.PLUGIN
                    null -> DismissType.STANDARD
                },
                snoozeDurationMinutes = s.snoozeDurationMinutes,
                createdAt = now,
                updatedAt = now
            )

            val savedId = saveAlarmUseCase(alarm)
            val updated = alarmRepository.getAlarm(savedId)
            if (updated != null && updated.isEnabled) {
                alarmScheduler.schedule(updated)
            }
            onSaved()
        }
    }
}
