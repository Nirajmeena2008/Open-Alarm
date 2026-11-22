package com.alarmapp.domain.usecase.alarm

import com.alarmapp.domain.model.Alarm
import com.alarmapp.domain.model.AlarmId
import com.alarmapp.domain.model.ChallengeConfig
import com.alarmapp.domain.repository.AlarmRepository
import com.alarmapp.domain.util.NextOccurrenceCalculator
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.time.ZoneId
import java.util.UUID

class GetAlarmsUseCase(private val repository: AlarmRepository) {
    operator fun invoke(): Flow<List<Alarm>> = repository.getAllAlarms()
}

class GetAlarmByIdUseCase(private val repository: AlarmRepository) {
    operator fun invoke(id: AlarmId): Flow<Alarm?> = repository.getAlarmById(id)
}

class SaveAlarmUseCase(
    private val repository: AlarmRepository,
    private val clock: Clock = Clock.System
) {
    suspend operator fun invoke(alarm: Alarm): AlarmId {
        val now = clock.now()
        val nextTrigger = if (alarm.isEnabled) {
            NextOccurrenceCalculator.calculateNextOccurrence(
                targetTime = alarm.time,
                daysOfWeek = alarm.daysOfWeek,
                fromInstant = now
            )
        } else {
            null
        }

        val updatedAlarm = alarm.copy(
            id = if (alarm.id.value.isBlank()) AlarmId(UUID.randomUUID().toString()) else alarm.id,
            nextTriggerTime = nextTrigger,
            updatedAt = now
        )
        return repository.upsertAlarm(updatedAlarm)
    }
}

class ToggleAlarmUseCase(
    private val repository: AlarmRepository,
    private val clock: Clock = Clock.System
) {
    suspend operator fun invoke(id: AlarmId, isEnabled: Boolean) {
        val alarm = repository.getAlarm(id) ?: return
        val now = clock.now()
        val nextTrigger = if (isEnabled) {
            NextOccurrenceCalculator.calculateNextOccurrence(
                targetTime = alarm.time,
                daysOfWeek = alarm.daysOfWeek,
                fromInstant = now
            )
        } else {
            null
        }
        repository.updateAlarmEnabled(id, isEnabled)
        repository.updateNextTrigger(id, nextTrigger)
    }
}

class DeleteAlarmUseCase(private val repository: AlarmRepository) {
    suspend operator fun invoke(id: AlarmId) {
        repository.deleteAlarm(id)
    }
}

class SnoozeAlarmUseCase(
    private val repository: AlarmRepository,
    private val clock: Clock = Clock.System
) {
    sealed interface Result {
        data class Snoozed(val nextTrigger: Instant, val snoozeCount: Int) : Result
        data object MaxSnoozeReached : Result
        data object AlarmNotFound : Result
    }

    suspend operator fun invoke(id: AlarmId): Result {
        val alarm = repository.getAlarm(id) ?: return Result.AlarmNotFound

        if (alarm.currentSnoozeCount >= alarm.maxSnoozeCount) {
            return Result.MaxSnoozeReached
        }

        val now = clock.now()
        val snoozeMs = alarm.snoozeDurationMinutes * 60 * 1000L
        val nextTrigger = Instant.fromEpochMilliseconds(now.toEpochMilliseconds() + snoozeMs)
        val newCount = alarm.currentSnoozeCount + 1

        repository.updateSnoozeState(id, newCount, nextTrigger)
        return Result.Snoozed(nextTrigger, newCount)
    }
}

class DismissAlarmUseCase(
    private val repository: AlarmRepository,
    private val clock: Clock = Clock.System
) {
    suspend operator fun invoke(id: AlarmId) {
        val alarm = repository.getAlarm(id) ?: return
        val now = clock.now()

        if (alarm.isRepeating) {
            // Calculate next repeating occurrence
            val nextTrigger = NextOccurrenceCalculator.calculateNextOccurrence(
                targetTime = alarm.time,
                daysOfWeek = alarm.daysOfWeek,
                fromInstant = now
            )
            repository.updateSnoozeState(id, snoozeCount = 0, nextTrigger = nextTrigger)
        } else {
            // One-shot alarm: disable and clear trigger
            repository.updateAlarmEnabled(id, false)
            repository.updateSnoozeState(id, snoozeCount = 0, nextTrigger = null)
        }
    }
}

class CalculateNextAlarmUseCase {
    operator fun invoke(
        alarms: List<Alarm>,
        now: Instant = Clock.System.now(),
        zoneId: ZoneId = ZoneId.systemDefault()
    ): Alarm? {
        return alarms
            .filter { it.isEnabled }
            .mapNotNull { alarm ->
                val next = alarm.nextTriggerTime ?: NextOccurrenceCalculator.calculateNextOccurrence(
                    targetTime = alarm.time,
                    daysOfWeek = alarm.daysOfWeek,
                    fromInstant = now,
                    zoneId = zoneId
                )
                alarm to next
            }
            .filter { it.second > now }
            .minByOrNull { it.second }
            ?.first
    }
}

class ValidateChallengeAnswerUseCase {
    operator fun invoke(
        config: ChallengeConfig,
        userAnswer: String,
        expectedAnswer: String
    ): Boolean {
        return when (config) {
            is ChallengeConfig.Math -> userAnswer.trim() == expectedAnswer.trim()
            is ChallengeConfig.Nfc -> userAnswer.trim().equals(config.registeredTagId.trim(), ignoreCase = true)
            is ChallengeConfig.Qr -> userAnswer.trim() == config.expectedPayload.trim()
            is ChallengeConfig.Memory -> userAnswer.trim() == expectedAnswer.trim()
            is ChallengeConfig.Shake -> (userAnswer.toIntOrNull() ?: 0) >= config.targetShakes
            is ChallengeConfig.Plugin -> userAnswer.trim() == expectedAnswer.trim()
        }
    }
}
