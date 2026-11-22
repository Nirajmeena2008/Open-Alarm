package com.alarmapp.domain.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

enum class SleepQuality {
    POOR,
    FAIR,
    GOOD,
    EXCELLENT
}

enum class SleepStage {
    AWAKE,
    LIGHT,
    DEEP,
    REM
}

@Serializable
data class SleepStageRecord(
    val stage: SleepStage,
    val startTime: Instant,
    val endTime: Instant
)

@Serializable
data class SleepSession(
    val id: String,
    val startTime: Instant,
    val endTime: Instant? = null,
    val quality: SleepQuality = SleepQuality.GOOD,
    val stages: List<SleepStageRecord> = emptyList(),
    val notes: String? = null
) {
    val durationMinutes: Long
        get() = if (endTime != null) {
            (endTime.toEpochMilliseconds() - startTime.toEpochMilliseconds()) / (1000 * 60)
        } else {
            0L
        }
}

@Serializable
data class RoutineStep(
    val id: String,
    val title: String,
    val durationMinutes: Int,
    val audioTrack: String? = null,
    val order: Int
)

@Serializable
data class Routine(
    val id: String,
    val name: String,
    val steps: List<RoutineStep> = emptyList(),
    val isEnabled: Boolean = true
) {
    val totalDurationMinutes: Int get() = steps.sumOf { it.durationMinutes }
}
