package com.alarmapp.feature.developer.webhook

import com.alarmapp.domain.model.Webhook
import com.alarmapp.domain.model.WebhookEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

@Serializable
data class WebhookPayload(
    val event: WebhookEvent,
    val timestamp: Long,
    val data: String
)

object WebhookDispatcher {

    fun computeSignature(payload: String, secret: String): String {
        val keySpec = SecretKeySpec(secret.toByteArray(Charsets.UTF_8), "HmacSHA256")
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(keySpec)
        val hmacBytes = mac.doFinal(payload.toByteArray(Charsets.UTF_8))
        return hmacBytes.joinToString("") { "%02x".format(it) }
    }

    suspend fun dispatchEvent(
        webhook: Webhook,
        event: WebhookEvent,
        dataJson: String
    ): Boolean = withContext(Dispatchers.IO) {
        if (!webhook.isEnabled || !webhook.events.contains(event)) {
            return@withContext false
        }

        val payloadObj = WebhookPayload(
            event = event,
            timestamp = Clock.System.now().toEpochMilliseconds(),
            data = dataJson
        )
        val payloadString = Json.encodeToString(payloadObj)
        val signature = webhook.secret?.let { computeSignature(payloadString, it) }

        var currentAttempt = 0
        var backoffMs = webhook.retryPolicy.initialBackoffMs

        while (currentAttempt <= webhook.retryPolicy.maxRetries) {
            try {
                val url = URL(webhook.url)
                val conn = (url.openConnection() as HttpURLConnection).apply {
                    requestMethod = "POST"
                    connectTimeout = 5000
                    readTimeout = 5000
                    doOutput = true
                    setRequestProperty("Content-Type", "application/json")
                    setRequestProperty("User-Agent", "OpenAlarm-Webhook/1.0")
                    if (signature != null) {
                        setRequestProperty("X-AlarmApp-Signature", "sha256=$signature")
                    }
                }

                conn.outputStream.use { os ->
                    OutputStreamWriter(os, Charsets.UTF_8).use { writer ->
                        writer.write(payloadString)
                        writer.flush()
                    }
                }

                val code = conn.responseCode
                conn.disconnect()

                if (code in 200..299) {
                    return@withContext true
                }
            } catch (_: Exception) {
                // Retry on exception
            }

            currentAttempt++
            if (currentAttempt <= webhook.retryPolicy.maxRetries) {
                delay(backoffMs)
                backoffMs *= 2
            }
        }

        false
    }
}
