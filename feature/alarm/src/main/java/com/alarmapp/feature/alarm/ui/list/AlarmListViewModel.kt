package com.alarmapp.feature.alarm.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alarmapp.domain.model.Alarm
import com.alarmapp.domain.model.AlarmId
import com.alarmapp.domain.repository.AlarmRepository
import com.alarmapp.domain.usecase.alarm.DeleteAlarmUseCase
import com.alarmapp.domain.usecase.alarm.GetAlarmsUseCase
import com.alarmapp.domain.usecase.alarm.ToggleAlarmUseCase
import com.alarmapp.platform.alarm.AlarmScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class AlarmListUiState(
    val alarms: List<Alarm> = emptyList(),
    val isLoading: Boolean = false,
    val message: String? = null
)

class AlarmListViewModel(
    private val alarmRepository: AlarmRepository,
    private val getAlarmsUseCase: GetAlarmsUseCase,
    private val toggleAlarmUseCase: ToggleAlarmUseCase,
    private val deleteAlarmUseCase: DeleteAlarmUseCase,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {

    private val _message = MutableStateFlow<String?>(null)

    val uiState: StateFlow<AlarmListUiState> = combine(
        getAlarmsUseCase(),
        _message
    ) { alarms, message ->
        AlarmListUiState(
            alarms = alarms.sortedBy { it.time },
            isLoading = false,
            message = message
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AlarmListUiState(isLoading = true)
    )

    fun toggleAlarm(alarmId: AlarmId, enabled: Boolean) {
        viewModelScope.launch {
            toggleAlarmUseCase(alarmId, enabled)
            val updated = alarmRepository.getAlarm(alarmId)
            if (updated != null) {
                if (updated.isEnabled) {
                    alarmScheduler.schedule(updated)
                } else {
                    alarmScheduler.cancel(updated.id)
                }
            }
        }
    }

    fun deleteAlarm(alarmId: AlarmId) {
        viewModelScope.launch {
            alarmScheduler.cancel(alarmId)
            deleteAlarmUseCase(alarmId)
            _message.value = "Alarm deleted"
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}
