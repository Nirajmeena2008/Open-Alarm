package com.alarmapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.alarmapp.data.converter.Converters
import com.alarmapp.data.dao.AlarmDao
import com.alarmapp.data.dao.PluginChallengeDao
import com.alarmapp.data.dao.RoutineDao
import com.alarmapp.data.dao.SettingsDao
import com.alarmapp.data.dao.SleepSessionDao
import com.alarmapp.data.dao.WebhookDao
import com.alarmapp.data.entity.AlarmEntity
import com.alarmapp.data.entity.PluginChallengeEntity
import com.alarmapp.data.entity.RoutineEntity
import com.alarmapp.data.entity.SettingEntity
import com.alarmapp.data.entity.SleepSessionEntity
import com.alarmapp.data.entity.WebhookEntity

@Database(
    entities = [
        AlarmEntity::class,
        SleepSessionEntity::class,
        RoutineEntity::class,
        SettingEntity::class,
        WebhookEntity::class,
        PluginChallengeEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AlarmDatabase : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao
    abstract fun sleepSessionDao(): SleepSessionDao
    abstract fun routineDao(): RoutineDao
    abstract fun settingsDao(): SettingsDao
    abstract fun webhookDao(): WebhookDao
    abstract fun pluginChallengeDao(): PluginChallengeDao

    companion object {
        const val DATABASE_NAME = "alarm_app.db"

        fun create(context: Context, inMemory: Boolean = false): AlarmDatabase {
            val builder = if (inMemory) {
                Room.inMemoryDatabaseBuilder(context, AlarmDatabase::class.java)
            } else {
                Room.databaseBuilder(context, AlarmDatabase::class.java, DATABASE_NAME)
            }
            return builder
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
