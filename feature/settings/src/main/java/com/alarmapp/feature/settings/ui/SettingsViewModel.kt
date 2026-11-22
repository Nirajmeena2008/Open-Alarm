package com.alarmapp.feature.settings.ui

import android.content.Context
import android.os.PowerManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alarmapp.data.backup.BackupManager
import com.alarmapp.domain.repository.AlarmRepository
import com.alarmapp.domain.repository.RoutineRepository
import com.alarmapp.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class SettingsUiState(
    val themeMode: String = "Light",
    val defaultSnoozeDuration: Int = 5,
    val defaultVolumeRampDuration: Int = 60,
    val isBatteryOptimizationIgnored: Boolean = false,
    val backupMessage: String? = null
)

class SettingsViewModel(
    private val context: Context,
    private val settingsRepository: SettingsRepository,
    private val alarmRepository: AlarmRepository,
    private val routineRepository: RoutineRepository,
    private val backupManager: BackupManager
) : ViewModel() {

    private val _themeMode = MutableStateFlow("Light")
    private val _snoozeDuration = MutableStateFlow(5)
    private val _rampDuration = MutableStateFlow(60)
    private val _backupMessage = MutableStateFlow<String?>(null)

    init {
        viewModelScope.launch {
            settingsRepository.getString("theme_mode", "Light").collect { savedTheme ->
                _themeMode.value = savedTheme
            }
        }
        viewModelScope.launch {
            settingsRepository.getInt("snooze_duration", 5).collect { savedSnooze ->
                _snoozeDuration.value = savedSnooze
            }
        }
        viewModelScope.launch {
            settingsRepository.getInt("ramp_duration", 60).collect { savedRamp ->
                _rampDuration.value = savedRamp
            }
        }
    }

    val uiState: StateFlow<SettingsUiState> = combine(
        _themeMode,
        _snoozeDuration,
        _rampDuration,
        _backupMessage
    ) { theme, snooze, ramp, msg ->
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as? PowerManager
        val isIgnored = powerManager?.isIgnoringBatteryOptimizations(context.packageName) ?: false

        SettingsUiState(
            themeMode = theme,
            defaultSnoozeDuration = snooze,
            defaultVolumeRampDuration = ramp,
            isBatteryOptimizationIgnored = isIgnored,
            backupMessage = msg
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState()
    )

    fun setTheme(mode: String) {
        _themeMode.value = mode
        viewModelScope.launch {
            settingsRepository.setString("theme_mode", mode)
        }
    }

    fun setSnoozeDuration(minutes: Int) {
        _snoozeDuration.value = minutes
        viewModelScope.launch {
            settingsRepository.setInt("snooze_duration", minutes)
        }
    }

    fun setRampDuration(seconds: Int) {
        _rampDuration.value = seconds
        viewModelScope.launch {
            settingsRepository.setInt("ramp_duration", seconds)
        }
    }

    fun exportBackup(password: String, onExported: (ByteArray) -> Unit) {
        viewModelScope.launch {
            try {
                val bytes = backupManager.exportJson(passphrase = password)
                _backupMessage.value = "Backup created successfully"
                onExported(bytes)
            } catch (e: Exception) {
                _backupMessage.value = "Backup export failed: ${e.message}"
            }
        }
    }

    fun importBackup(bytes: ByteArray, password: String) {
        viewModelScope.launch {
            try {
                val payload = backupManager.importJson(data = bytes, passphrase = password)
                _backupMessage.value = "Restored ${payload.alarms.size} alarms and ${payload.routines.size} routines"
            } catch (e: Exception) {
                _backupMessage.value = "Import failed: Invalid password or corrupted file"
            }
        }
    }

    fun clearBackupMessage() {
        _backupMessage.value = null
    }
}
