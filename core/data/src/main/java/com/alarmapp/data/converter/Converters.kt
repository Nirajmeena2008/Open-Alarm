package com.alarmapp.data.converter

import androidx.room.TypeConverter
import com.alarmapp.domain.model.ChallengeConfig
import com.alarmapp.domain.model.DayOfWeek
import com.alarmapp.domain.model.DismissType
import com.alarmapp.domain.model.RetryPolicy
import com.alarmapp.domain.model.RingtoneSource
import com.alarmapp.domain.model.RoutineStep
import com.alarmapp.domain.model.SleepQuality
import com.alarmapp.domain.model.SleepStageRecord
import com.alarmapp.domain.model.VibrationPattern
import com.alarmapp.domain.model.VolumeRamp
import com.alarmapp.domain.model.WebhookEvent
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalTime
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @TypeConverter
    fun fromInstant(instant: Instant?): Long? = instant?.toEpochMilliseconds()

    @TypeConverter
    fun toInstant(millis: Long?): Instant? = millis?.let { Instant.fromEpochMilliseconds(it) }

    @TypeConverter
    fun fromLocalTime(time: LocalTime?): String? = time?.toString()

    @TypeConverter
    fun toLocalTime(str: String?): LocalTime? = str?.let { LocalTime.parse(it) }

    @TypeConverter
    fun fromDayOfWeekSet(days: Set<DayOfWeek>?): String =
        days?.joinToString(",") { it.name } ?: ""

    @TypeConverter
    fun toDayOfWeekSet(data: String?): Set<DayOfWeek> {
        if (data.isNullOrBlank()) return emptySet()
        return data.split(",")
            .mapNotNull { runCatching { DayOfWeek.valueOf(it) }.getOrNull() }
            .toSet()
    }

    @TypeConverter
    fun fromRingtoneSource(source: RingtoneSource?): String =
        if (source == null) "" else json.encodeToString<RingtoneSource>(source)

    @TypeConverter
    fun toRingtoneSource(data: String?): RingtoneSource =
        if (data.isNullOrBlank()) RingtoneSource.Default else json.decodeFromString(data)

    @TypeConverter
    fun fromVolumeRamp(ramp: VolumeRamp?): String? =
        ramp?.let { json.encodeToString(it) }

    @TypeConverter
    fun toVolumeRamp(data: String?): VolumeRamp? =
        data?.let { json.decodeFromString(it) }

    @TypeConverter
    fun fromVibrationPattern(pattern: VibrationPattern?): String = pattern?.name ?: VibrationPattern.STANDARD.name

    @TypeConverter
    fun toVibrationPattern(data: String?): VibrationPattern =
        runCatching { VibrationPattern.valueOf(data ?: "") }.getOrDefault(VibrationPattern.STANDARD)

    @TypeConverter
    fun fromDismissType(type: DismissType?): String = type?.name ?: DismissType.STANDARD.name

    @TypeConverter
    fun toDismissType(data: String?): DismissType =
        runCatching { DismissType.valueOf(data ?: "") }.getOrDefault(DismissType.STANDARD)

    @TypeConverter
    fun fromChallengeConfig(config: ChallengeConfig?): String? =
        config?.let { json.encodeToString<ChallengeConfig>(it) }

    @TypeConverter
    fun toChallengeConfig(data: String?): ChallengeConfig? =
        if (data.isNullOrBlank()) null else json.decodeFromString<ChallengeConfig>(data)

    @TypeConverter
    fun fromSleepQuality(quality: SleepQuality?): String = quality?.name ?: SleepQuality.GOOD.name

    @TypeConverter
    fun toSleepQuality(data: String?): SleepQuality =
        runCatching { SleepQuality.valueOf(data ?: "") }.getOrDefault(SleepQuality.GOOD)

    @TypeConverter
    fun fromSleepStages(stages: List<SleepStageRecord>?): String =
        if (stages == null) "[]" else json.encodeToString(stages)

    @TypeConverter
    fun toSleepStages(data: String?): List<SleepStageRecord> =
        if (data.isNullOrBlank()) emptyList() else json.decodeFromString(data)

    @TypeConverter
    fun fromRoutineSteps(steps: List<RoutineStep>?): String =
        if (steps == null) "[]" else json.encodeToString(steps)

    @TypeConverter
    fun toRoutineSteps(data: String?): List<RoutineStep> =
        if (data.isNullOrBlank()) emptyList() else json.decodeFromString(data)

    @TypeConverter
    fun fromWebhookEvents(events: Set<WebhookEvent>?): String =
        events?.joinToString(",") { it.name } ?: ""

    @TypeConverter
    fun toWebhookEvents(data: String?): Set<WebhookEvent> {
        if (data.isNullOrBlank()) return emptySet()
        return data.split(",")
            .mapNotNull { runCatching { WebhookEvent.valueOf(it) }.getOrNull() }
            .toSet()
    }

    @TypeConverter
    fun fromRetryPolicy(policy: RetryPolicy?): String =
        if (policy == null) "" else json.encodeToString(policy)

    @TypeConverter
    fun toRetryPolicy(data: String?): RetryPolicy =
        if (data.isNullOrBlank()) RetryPolicy() else json.decodeFromString(data)
}
