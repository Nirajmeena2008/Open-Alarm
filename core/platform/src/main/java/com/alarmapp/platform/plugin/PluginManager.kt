package com.alarmapp.platform.plugin

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.alarmapp.domain.model.PluginChallenge
import com.alarmapp.domain.repository.PluginRepository

class PluginManager(
    private val context: Context,
    private val pluginRepository: PluginRepository
) {
    suspend fun discoverAndRegisterPlugins() {
        val packageManager = context.packageManager
        val queryIntent = Intent(ACTION_DISMISS_CHALLENGE)
        val resolveInfos = packageManager.queryIntentServices(queryIntent, PackageManager.GET_META_DATA)

        for (resolveInfo in resolveInfos) {
            val serviceInfo = resolveInfo.serviceInfo ?: continue
            val packageName = serviceInfo.packageName
            val label = serviceInfo.loadLabel(packageManager).toString()
            val metaData = serviceInfo.metaData

            val challengeId = metaData?.getString("com.alarmapp.plugin.CHALLENGE_ID") ?: serviceInfo.name
            val description = metaData?.getString("com.alarmapp.plugin.DESCRIPTION") ?: "External Challenge Plugin"
            val schemaJson = metaData?.getString("com.alarmapp.plugin.SCHEMA_JSON") ?: "{}"

            val plugin = PluginChallenge(
                pluginId = challengeId,
                packageName = packageName,
                title = label,
                description = description,
                schemaJson = schemaJson
            )
            pluginRepository.registerPlugin(plugin)
        }
    }

    companion object {
        const val ACTION_DISMISS_CHALLENGE = "com.alarmapp.plugin.DISMISS_CHALLENGE"
    }
}
