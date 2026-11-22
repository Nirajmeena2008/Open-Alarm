package com.alarmapp.domain.repository

import com.alarmapp.domain.model.Alarm
import com.alarmapp.domain.model.AlarmId
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

interface AlarmRepository {
    fun getAllAlarms(): Flow<List<Alarm>>
    fun getAlarmById(id: AlarmId): Flow<Alarm?>
    fun getEnabledAlarms(): Flow<List<Alarm>>
    suspend fun getAlarm(id: AlarmId): Alarm?
    suspend fun upsertAlarm(alarm: Alarm): AlarmId
    suspend fun deleteAlarm(id: AlarmId)
    suspend fun updateAlarmEnabled(id: AlarmId, isEnabled: Boolean)
    suspend fun updateNextTrigger(id: AlarmId, nextTrigger: Instant?)
    suspend fun updateSnoozeState(id: AlarmId, snoozeCount: Int, nextTrigger: Instant?)
}

interface SleepRepository {
    fun getAllSessions(): Flow<List<com.alarmapp.domain.model.SleepSession>>
    fun getSession(id: String): Flow<com.alarmapp.domain.model.SleepSession?>
    suspend fun saveSession(session: com.alarmapp.domain.model.SleepSession)
    suspend fun deleteSession(id: String)
}

interface SettingsRepository {
    fun getString(key: String, defaultValue: String): Flow<String>
    suspend fun setString(key: String, value: String)
    fun getBoolean(key: String, defaultValue: Boolean): Flow<Boolean>
    suspend fun setBoolean(key: String, value: Boolean)
    fun getInt(key: String, defaultValue: Int): Flow<Int>
    suspend fun setInt(key: String, value: Int)
}

interface RoutineRepository {
    fun getRoutines(): Flow<List<com.alarmapp.domain.model.Routine>>
    fun getRoutine(id: String): Flow<com.alarmapp.domain.model.Routine?>
    suspend fun saveRoutine(routine: com.alarmapp.domain.model.Routine)
    suspend fun deleteRoutine(id: String)
}

interface WebhookRepository {
    fun getWebhooks(): Flow<List<com.alarmapp.domain.model.Webhook>>
    suspend fun saveWebhook(webhook: com.alarmapp.domain.model.Webhook)
    suspend fun deleteWebhook(id: String)
}

interface PluginRepository {
    fun getInstalledPlugins(): Flow<List<com.alarmapp.domain.model.PluginChallenge>>
    suspend fun registerPlugin(plugin: com.alarmapp.domain.model.PluginChallenge)
    suspend fun unregisterPlugin(pluginId: String)
}
