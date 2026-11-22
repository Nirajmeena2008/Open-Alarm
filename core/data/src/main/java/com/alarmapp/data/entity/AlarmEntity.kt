package com.alarmapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alarmapp.domain.model.ChallengeConfig
import com.alarmapp.domain.model.DayOfWeek
import com.alarmapp.domain.model.DismissType
import com.alarmapp.domain.model.RingtoneSource
import com.alarmapp.domain.model.VibrationPattern
import com.alarmapp.domain.model.VolumeRamp
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalTime

@Entity(tableName = "alarms")
data class AlarmEntity(
    @PrimaryKey val id: String,
    val label: String,
    val time: LocalTime,
    val isEnabled: Boolean,
    val daysOfWeek: Set<DayOfWeek>,
    val ringtone: RingtoneSource,
    val volume: Float,
    val volumeRamp: VolumeRamp?,
    val vibration: VibrationPattern,
    val dismissType: DismissType,
    val challengeConfig: ChallengeConfig?,
    val snoozeDurationMinutes: Int,
    val maxSnoozeCount: Int,
    val currentSnoozeCount: Int,
    val isSmartWakeEnabled: Boolean,
    val smartWakeWindowMinutes: Int,
    val nextTriggerTime: Instant?,
    val createdAt: Instant,
    val updatedAt: Instant
)
