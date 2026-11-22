package com.alarmapp.feature.developer.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alarmapp.domain.model.Webhook
import com.alarmapp.domain.model.WebhookEvent
import com.alarmapp.domain.repository.WebhookRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

data class DeveloperUiState(
    val isServerRunning: Boolean = false,
    val port: Int = 18427,
    val apiKey: String = "dev-secret-key-12345",
    val webhooks: List<Webhook> = emptyList(),
    val mqttHost: String = "192.168.1.100",
    val isMqttEnabled: Boolean = false
)

class DeveloperViewModel(
    private val webhookRepository: WebhookRepository
) : ViewModel() {

    private val _isServerRunning = MutableStateFlow(false)
    private val _port = MutableStateFlow(18427)
    private val _apiKey = MutableStateFlow("dev-secret-key-12345")
    private val _mqttHost = MutableStateFlow("192.168.1.100")
    private val _isMqttEnabled = MutableStateFlow(false)

    val uiState: StateFlow<DeveloperUiState> = combine(
        _isServerRunning,
        _port,
        _apiKey,
        webhookRepository.getWebhooks(),
        _isMqttEnabled
    ) { isRunning, port, key, webhooks, isMqtt ->
        DeveloperUiState(
            isServerRunning = isRunning,
            port = port,
            apiKey = key,
            webhooks = webhooks,
            isMqttEnabled = isMqtt
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DeveloperUiState()
    )

    fun toggleServer(enable: Boolean) {
        _isServerRunning.value = enable
    }

    fun generateNewApiKey() {
        _apiKey.value = UUID.randomUUID().toString()
    }

    fun addWebhook(url: String, secret: String?) {
        viewModelScope.launch {
            val webhook = Webhook(
                id = UUID.randomUUID().toString(),
                url = url,
                events = setOf(
                    WebhookEvent.ALARM_TRIGGERED,
                    WebhookEvent.ALARM_DISMISSED,
                    WebhookEvent.ALARM_SNOOZED
                ),
                secret = secret
            )
            webhookRepository.saveWebhook(webhook)
        }
    }

    fun deleteWebhook(id: String) {
        viewModelScope.launch {
            webhookRepository.deleteWebhook(id)
        }
    }
}
