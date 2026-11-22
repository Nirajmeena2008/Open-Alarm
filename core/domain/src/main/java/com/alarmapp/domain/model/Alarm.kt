package com.alarmapp.domain.model

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class AlarmId(val value: String) {
    override fun toString(): String = value
}

enum class DayOfWeek(val isoValue: Int) {
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6),
    SUNDAY(7);

    companion object {
        fun fromIso(value: Int): DayOfWeek = entries.firstOrNull { it.isoValue == value } ?: MONDAY
        val WEEKDAYS: Set<DayOfWeek> = setOf(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY)
        val WEEKENDS: Set<DayOfWeek> = setOf(SATURDAY, SUNDAY)
        val ALL: Set<DayOfWeek> = entries.toSet()
    }
}

@Serializable
sealed interface RingtoneSource {
    @Serializable
    data object Default : RingtoneSource

    @Serializable
    data class System(val title: String, val uri: String) : RingtoneSource

    @Serializable
    data class File(val title: String, val path: String) : RingtoneSource

    @Serializable
    data object Silent : RingtoneSource
}

@Serializable
data class VolumeRamp(
    val durationSeconds: Int = 60,
    val startVolume: Float = 0.1f,
    val endVolume: Float = 1.0f
) {
    init {
        require(durationSeconds > 0) { "durationSeconds must be > 0" }
        require(startVolume in 0f..1f) { "startVolume must be in 0..1" }
        require(endVolume in 0f..1f) { "endVolume must be in 0..1" }
        require(startVolume <= endVolume) { "startVolume must be <= endVolume" }
    }
}

enum class VibrationPattern {
    NONE,
    STANDARD,
    HEARTBEAT,
    ESCALATING,
    STACCATO
}

enum class DismissType {
    STANDARD,
    MATH,
    MEMORY,
    SHAKE,
    NFC,
    QR,
    PLUGIN
}

enum class MathDifficulty {
    EASY,     // e.g. 12 + 7
    MEDIUM,   // e.g. 24 * 3 + 15
    HARD      // e.g. (17 * 8) - (45 / 3)
}

@Serializable
sealed interface ChallengeConfig {
    @Serializable
    data class Math(
        val difficulty: MathDifficulty = MathDifficulty.MEDIUM,
        val problemCount: Int = 3
    ) : ChallengeConfig

    @Serializable
    data class Memory(
        val gridDimension: Int = 3,
        val sequenceLength: Int = 5
    ) : ChallengeConfig

    @Serializable
    data class Shake(
        val targetShakes: Int = 30,
        val sensitivity: Float = 12.0f
    ) : ChallengeConfig

    @Serializable
    data class Nfc(
        val registeredTagId: String,
        val tagName: String = "Alarm Tag"
    ) : ChallengeConfig

    @Serializable
    data class Qr(
        val expectedPayload: String,
        val description: String = "Bathroom QR Code"
    ) : ChallengeConfig

    @Serializable
    data class Plugin(
        val pluginPackage: String,
        val challengeId: String,
        val configJson: String = "{}"
    ) : ChallengeConfig
}

@Serializable
data class Alarm(
    val id: AlarmId,
    val label: String = "Alarm",
    val time: LocalTime,
    val isEnabled: Boolean = true,
    val daysOfWeek: Set<DayOfWeek> = emptySet(), // Empty means one-time alarm
    val ringtone: RingtoneSource = RingtoneSource.Default,
    val volume: Float = 0.8f,
    val volumeRamp: VolumeRamp? = VolumeRamp(),
    val vibration: VibrationPattern = VibrationPattern.STANDARD,
    val dismissType: DismissType = DismissType.STANDARD,
    val challengeConfig: ChallengeConfig? = null,
    val snoozeDurationMinutes: Int = 5,
    val maxSnoozeCount: Int = 3,
    val currentSnoozeCount: Int = 0,
    val isSmartWakeEnabled: Boolean = false,
    val smartWakeWindowMinutes: Int = 20,
    val nextTriggerTime: Instant? = null,
    val createdAt: Instant,
    val updatedAt: Instant
) {
    val isRepeating: Boolean get() = daysOfWeek.isNotEmpty()
}
