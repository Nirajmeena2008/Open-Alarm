package com.alarmapp.data.repository

import com.alarmapp.data.dao.PluginChallengeDao
import com.alarmapp.data.dao.RoutineDao
import com.alarmapp.data.dao.SettingsDao
import com.alarmapp.data.dao.SleepSessionDao
import com.alarmapp.data.dao.WebhookDao
import com.alarmapp.data.entity.PluginChallengeEntity
import com.alarmapp.data.entity.RoutineEntity
import com.alarmapp.data.entity.SettingEntity
import com.alarmapp.data.entity.SleepSessionEntity
import com.alarmapp.data.entity.WebhookEntity
import com.alarmapp.domain.model.PluginChallenge
import com.alarmapp.domain.model.Routine
import com.alarmapp.domain.model.SleepSession
import com.alarmapp.domain.model.Webhook
import com.alarmapp.domain.repository.PluginRepository
import com.alarmapp.domain.repository.RoutineRepository
import com.alarmapp.domain.repository.SettingsRepository
import com.alarmapp.domain.repository.SleepRepository
import com.alarmapp.domain.repository.WebhookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SleepRepositoryImpl(private val dao: SleepSessionDao) : SleepRepository {
    override fun getAllSessions(): Flow<List<SleepSession>> =
        dao.getAllSessions().map { list -> list.map { it.toDomain() } }

    override fun getSession(id: String): Flow<SleepSession?> =
        dao.getSession(id).map { it?.toDomain() }

    override suspend fun saveSession(session: SleepSession) {
        dao.upsertSession(session.toEntity())
    }

    override suspend fun deleteSession(id: String) {
        dao.deleteSessionById(id)
    }
}

class RoutineRepositoryImpl(private val dao: RoutineDao) : RoutineRepository {
    override fun getRoutines(): Flow<List<Routine>> =
        dao.getRoutines().map { list -> list.map { it.toDomain() } }

    override fun getRoutine(id: String): Flow<Routine?> =
        dao.getRoutine(id).map { it?.toDomain() }

    override suspend fun saveRoutine(routine: Routine) {
        dao.upsertRoutine(routine.toEntity())
    }

    override suspend fun deleteRoutine(id: String) {
        dao.deleteRoutineById(id)
    }
}

class SettingsRepositoryImpl(private val dao: SettingsDao) : SettingsRepository {
    override fun getString(key: String, defaultValue: String): Flow<String> =
        dao.getSetting(key).map { it ?: defaultValue }

    override suspend fun setString(key: String, value: String) {
        dao.setSetting(SettingEntity(key, value))
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Flow<Boolean> =
        dao.getSetting(key).map { it?.toBooleanStrictOrNull() ?: defaultValue }

    override suspend fun setBoolean(key: String, value: Boolean) {
        dao.setSetting(SettingEntity(key, value.toString()))
    }

    override fun getInt(key: String, defaultValue: Int): Flow<Int> =
        dao.getSetting(key).map { it?.toIntOrNull() ?: defaultValue }

    override suspend fun setInt(key: String, value: Int) {
        dao.setSetting(SettingEntity(key, value.toString()))
    }
}

class WebhookRepositoryImpl(private val dao: WebhookDao) : WebhookRepository {
    override fun getWebhooks(): Flow<List<Webhook>> =
        dao.getWebhooks().map { list -> list.map { it.toDomain() } }

    override suspend fun saveWebhook(webhook: Webhook) {
        dao.upsertWebhook(webhook.toEntity())
    }

    override suspend fun deleteWebhook(id: String) {
        dao.deleteWebhookById(id)
    }
}

class PluginRepositoryImpl(private val dao: PluginChallengeDao) : PluginRepository {
    override fun getInstalledPlugins(): Flow<List<PluginChallenge>> =
        dao.getInstalledPlugins().map { list -> list.map { it.toDomain() } }

    override suspend fun registerPlugin(plugin: PluginChallenge) {
        dao.upsertPlugin(plugin.toEntity())
    }

    override suspend fun unregisterPlugin(pluginId: String) {
        dao.deletePluginById(pluginId)
    }
}

// Extension mappers
fun SleepSession.toEntity(): SleepSessionEntity = SleepSessionEntity(id, startTime, endTime, quality, stages, notes)
fun SleepSessionEntity.toDomain(): SleepSession = SleepSession(id, startTime, endTime, quality, stages, notes)

fun Routine.toEntity(): RoutineEntity = RoutineEntity(id, name, steps, isEnabled)
fun RoutineEntity.toDomain(): Routine = Routine(id, name, steps, isEnabled)

fun Webhook.toEntity(): WebhookEntity = WebhookEntity(id, url, events, secret, retryPolicy, isEnabled)
fun WebhookEntity.toDomain(): Webhook = Webhook(id, url, events, secret, retryPolicy, isEnabled)

fun PluginChallenge.toEntity(): PluginChallengeEntity = PluginChallengeEntity(pluginId, packageName, title, description, schemaJson)
fun PluginChallengeEntity.toDomain(): PluginChallenge = PluginChallenge(pluginId, packageName, title, description, schemaJson)
