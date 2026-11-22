package com.alarmapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alarmapp.domain.model.RetryPolicy
import com.alarmapp.domain.model.RoutineStep
import com.alarmapp.domain.model.SleepQuality
import com.alarmapp.domain.model.SleepStageRecord
import com.alarmapp.domain.model.WebhookEvent
import kotlinx.datetime.Instant

@Entity(tableName = "sleep_sessions")
data class SleepSessionEntity(
    @PrimaryKey val id: String,
    val startTime: Instant,
    val endTime: Instant?,
    val quality: SleepQuality,
    val stages: List<SleepStageRecord>,
    val notes: String?
)

@Entity(tableName = "routines")
data class RoutineEntity(
    @PrimaryKey val id: String,
    val name: String,
    val steps: List<RoutineStep>,
    val isEnabled: Boolean
)

@Entity(tableName = "settings")
data class SettingEntity(
    @PrimaryKey val key: String,
    val value: String
)

@Entity(tableName = "webhooks")
data class WebhookEntity(
    @PrimaryKey val id: String,
    val url: String,
    val events: Set<WebhookEvent>,
    val secret: String?,
    val retryPolicy: RetryPolicy,
    val isEnabled: Boolean
)

@Entity(tableName = "plugin_challenges")
data class PluginChallengeEntity(
    @PrimaryKey val pluginId: String,
    val packageName: String,
    val title: String,
    val description: String,
    val schemaJson: String
)
