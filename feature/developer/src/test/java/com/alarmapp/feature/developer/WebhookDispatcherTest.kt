package com.alarmapp.feature.developer

import com.alarmapp.feature.developer.mqtt.HomeAssistantMqtt
import com.alarmapp.feature.developer.webhook.WebhookDispatcher
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class WebhookDispatcherTest {

    @Test
    fun `computeSignature produces deterministic sha256 hmac hex string`() {
        val payload = """{"event":"ALARM_TRIGGERED","timestamp":1700000000000,"data":"test"}"""
        val secret = "my_super_secret"

        val sig1 = WebhookDispatcher.computeSignature(payload, secret)
        val sig2 = WebhookDispatcher.computeSignature(payload, secret)

        assertThat(sig1).isEqualTo(sig2)
        assertThat(sig1.length).isEqualTo(64) // SHA-256 hex length
    }

    @Test
    fun `HomeAssistantMqtt generates valid discovery config topic and payload`() {
        val (topic, payload) = HomeAssistantMqtt.generateNextAlarmConfig("pixel9")
        assertThat(topic).isEqualTo("homeassistant/sensor/openalarm/pixel9/config")
        assertThat(payload).contains("Next Scheduled Alarm")
        assertThat(payload).contains("openalarm/pixel9/state")
    }
}
