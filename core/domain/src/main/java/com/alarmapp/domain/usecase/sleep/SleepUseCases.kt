package com.alarmapp.domain.usecase.sleep

import com.alarmapp.domain.model.Routine
import com.alarmapp.domain.model.SleepQuality
import com.alarmapp.domain.model.SleepSession
import com.alarmapp.domain.repository.RoutineRepository
import com.alarmapp.domain.repository.SleepRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.util.UUID

class StartSleepSessionUseCase(
    private val repository: SleepRepository,
    private val clock: Clock = Clock.System
) {
    suspend operator fun invoke(): SleepSession {
        val session = SleepSession(
            id = UUID.randomUUID().toString(),
            startTime = clock.now()
        )
        repository.saveSession(session)
        return session
    }
}

class EndSleepSessionUseCase(
    private val repository: SleepRepository,
    private val clock: Clock = Clock.System
) {
    suspend operator fun invoke(
        sessionId: String,
        quality: SleepQuality = SleepQuality.GOOD,
        notes: String? = null
    ) {
        val now = clock.now()
        val session = SleepSession(
            id = sessionId,
            startTime = now, // Will be preserved by repository or updated
            endTime = now,
            quality = quality,
            notes = notes
        )
        repository.saveSession(session)
    }
}

class GetSleepHistoryUseCase(private val repository: SleepRepository) {
    operator fun invoke(): Flow<List<SleepSession>> = repository.getAllSessions()
}

class RoutineUseCases(private val repository: RoutineRepository) {
    fun getRoutines(): Flow<List<Routine>> = repository.getRoutines()
    suspend fun saveRoutine(routine: Routine) = repository.saveRoutine(routine)
    suspend fun deleteRoutine(id: String) = repository.deleteRoutine(id)
}
