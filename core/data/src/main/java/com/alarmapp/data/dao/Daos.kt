package com.alarmapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.alarmapp.data.entity.AlarmEntity
import com.alarmapp.data.entity.PluginChallengeEntity
import com.alarmapp.data.entity.RoutineEntity
import com.alarmapp.data.entity.SettingEntity
import com.alarmapp.data.entity.SleepSessionEntity
import com.alarmapp.data.entity.WebhookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {
    @Query("SELECT * FROM alarms ORDER BY time ASC")
    fun getAllAlarms(): Flow<List<AlarmEntity>>

    @Query("SELECT * FROM alarms WHERE isEnabled = 1 ORDER BY time ASC")
    fun getEnabledAlarms(): Flow<List<AlarmEntity>>

    @Query("SELECT * FROM alarms WHERE id = :id LIMIT 1")
    fun getAlarmById(id: String): Flow<AlarmEntity?>

    @Query("SELECT * FROM alarms WHERE id = :id LIMIT 1")
    suspend fun getAlarm(id: String): AlarmEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAlarm(alarm: AlarmEntity)

    @Delete
    suspend fun deleteAlarm(alarm: AlarmEntity)

    @Query("DELETE FROM alarms WHERE id = :id")
    suspend fun deleteAlarmById(id: String)

    @Query("UPDATE alarms SET isEnabled = :isEnabled WHERE id = :id")
    suspend fun updateAlarmEnabled(id: String, isEnabled: Boolean)

    @Query("UPDATE alarms SET nextTriggerTime = :nextTrigger WHERE id = :id")
    suspend fun updateNextTrigger(id: String, nextTrigger: Long?)

    @Query("UPDATE alarms SET currentSnoozeCount = :snoozeCount, nextTriggerTime = :nextTrigger WHERE id = :id")
    suspend fun updateSnoozeState(id: String, snoozeCount: Int, nextTrigger: Long?)
}

@Dao
interface SleepSessionDao {
    @Query("SELECT * FROM sleep_sessions ORDER BY startTime DESC")
    fun getAllSessions(): Flow<List<SleepSessionEntity>>

    @Query("SELECT * FROM sleep_sessions WHERE id = :id LIMIT 1")
    fun getSession(id: String): Flow<SleepSessionEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSession(session: SleepSessionEntity)

    @Query("DELETE FROM sleep_sessions WHERE id = :id")
    suspend fun deleteSessionById(id: String)
}

@Dao
interface RoutineDao {
    @Query("SELECT * FROM routines ORDER BY name ASC")
    fun getRoutines(): Flow<List<RoutineEntity>>

    @Query("SELECT * FROM routines WHERE id = :id LIMIT 1")
    fun getRoutine(id: String): Flow<RoutineEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRoutine(routine: RoutineEntity)

    @Query("DELETE FROM routines WHERE id = :id")
    suspend fun deleteRoutineById(id: String)
}

@Dao
interface SettingsDao {
    @Query("SELECT value FROM settings WHERE `key` = :key LIMIT 1")
    fun getSetting(key: String): Flow<String?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setSetting(setting: SettingEntity)
}

@Dao
interface WebhookDao {
    @Query("SELECT * FROM webhooks ORDER BY url ASC")
    fun getWebhooks(): Flow<List<WebhookEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertWebhook(webhook: WebhookEntity)

    @Query("DELETE FROM webhooks WHERE id = :id")
    suspend fun deleteWebhookById(id: String)
}

@Dao
interface PluginChallengeDao {
    @Query("SELECT * FROM plugin_challenges ORDER BY title ASC")
    fun getInstalledPlugins(): Flow<List<PluginChallengeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPlugin(plugin: PluginChallengeEntity)

    @Query("DELETE FROM plugin_challenges WHERE pluginId = :pluginId")
    suspend fun deletePluginById(pluginId: String)
}
