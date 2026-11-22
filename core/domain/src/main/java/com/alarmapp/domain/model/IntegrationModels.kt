package com.alarmapp.domain.model

import kotlinx.serialization.Serializable

enum class WebhookEvent {
    ALARM_SCHEDULED,
    ALARM_TRIGGERED,
    ALARM_DISMISSED,
    ALARM_SNOOZED,
    SLEEP_SESSION_STARTED,
    SLEEP_SESSION_ENDED
}

@Serializable
data class RetryPolicy(
    val maxRetries: Int = 3,
    val initialBackoffMs: Long = 1000L
)

@Serializable
data class Webhook(
    val id: String,
    val url: String,
    val events: Set<WebhookEvent>,
    val secret: String? = null,
    val retryPolicy: RetryPolicy = RetryPolicy(),
    val isEnabled: Boolean = true
)

@Serializable
data class PluginChallenge(
    val pluginId: String,
    val packageName: String,
    val title: String,
    val description: String,
    val schemaJson: String = "{}"
)
