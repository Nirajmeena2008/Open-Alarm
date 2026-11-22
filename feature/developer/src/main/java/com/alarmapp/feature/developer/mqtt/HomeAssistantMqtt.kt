package com.alarmapp.feature.developer.mqtt

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class HaDiscoveryDevice(
    val identifiers: List<String>,
    val name: String,
    val model: String = "OpenAlarm Android",
    val manufacturer: String = "Open Source"
)

@Serializable
data class HaSensorConfig(
    val name: String,
    val state_topic: String,
    val unique_id: String,
    val device_class: String? = "timestamp",
    val icon: String? = "mdi:alarm",
    val device: HaDiscoveryDevice
)

object HomeAssistantMqtt {

    fun generateNextAlarmConfig(deviceId: String = "openalarm_main"): Pair<String, String> {
        val topic = "homeassistant/sensor/openalarm/$deviceId/config"
        val config = HaSensorConfig(
            name = "Next Scheduled Alarm",
            state_topic = "openalarm/$deviceId/state",
            unique_id = "openalarm_${deviceId}_next_alarm",
            device = HaDiscoveryDevice(
                identifiers = listOf(deviceId),
                name = "OpenAlarm Device"
            )
        )
        return topic to Json.encodeToString(config)
    }

    fun generateStatePayload(nextTriggerIso: String?, status: String = "idle"): String {
        return """{"status":"$status","next_trigger":"${nextTriggerIso ?: "none"}"}"""
    }
}
