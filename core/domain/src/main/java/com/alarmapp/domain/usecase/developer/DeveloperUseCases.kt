package com.alarmapp.domain.usecase.developer

import com.alarmapp.domain.model.PluginChallenge
import com.alarmapp.domain.model.Webhook
import com.alarmapp.domain.repository.PluginRepository
import com.alarmapp.domain.repository.WebhookRepository
import kotlinx.coroutines.flow.Flow

class WebhookUseCases(private val repository: WebhookRepository) {
    fun getWebhooks(): Flow<List<Webhook>> = repository.getWebhooks()
    suspend fun saveWebhook(webhook: Webhook) = repository.saveWebhook(webhook)
    suspend fun deleteWebhook(id: String) = repository.deleteWebhook(id)
}

class PluginUseCases(private val repository: PluginRepository) {
    fun getInstalledPlugins(): Flow<List<PluginChallenge>> = repository.getInstalledPlugins()
    suspend fun registerPlugin(plugin: PluginChallenge) = repository.registerPlugin(plugin)
    suspend fun unregisterPlugin(pluginId: String) = repository.unregisterPlugin(pluginId)
}
