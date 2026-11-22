package com.alarmapp.data.repository

import com.alarmapp.data.dao.AlarmDao
import com.alarmapp.data.entity.AlarmEntity
import com.alarmapp.domain.model.Alarm
import com.alarmapp.domain.model.AlarmId
import com.alarmapp.domain.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant

class AlarmRepositoryImpl(
    private val alarmDao: AlarmDao
) : AlarmRepository {

    override fun getAllAlarms(): Flow<List<Alarm>> =
        alarmDao.getAllAlarms().map { list -> list.map { it.toDomain() } }

    override fun getAlarmById(id: AlarmId): Flow<Alarm?> =
        alarmDao.getAlarmById(id.value).map { it?.toDomain() }

    override fun getEnabledAlarms(): Flow<List<Alarm>> =
        alarmDao.getEnabledAlarms().map { list -> list.map { it.toDomain() } }

    override suspend fun getAlarm(id: AlarmId): Alarm? =
        alarmDao.getAlarm(id.value)?.toDomain()

    override suspend fun upsertAlarm(alarm: Alarm): AlarmId {
        alarmDao.upsertAlarm(alarm.toEntity())
        return alarm.id
    }

    override suspend fun deleteAlarm(id: AlarmId) {
        alarmDao.deleteAlarmById(id.value)
    }

    override suspend fun updateAlarmEnabled(id: AlarmId, isEnabled: Boolean) {
        alarmDao.updateAlarmEnabled(id.value, isEnabled)
    }

    override suspend fun updateNextTrigger(id: AlarmId, nextTrigger: Instant?) {
        alarmDao.updateNextTrigger(id.value, nextTrigger?.toEpochMilliseconds())
    }

    override suspend fun updateSnoozeState(id: AlarmId, snoozeCount: Int, nextTrigger: Instant?) {
        alarmDao.updateSnoozeState(id.value, snoozeCount, nextTrigger?.toEpochMilliseconds())
    }
}

fun Alarm.toEntity(): AlarmEntity = AlarmEntity(
    id = id.value,
    label = label,
    time = time,
    isEnabled = isEnabled,
    daysOfWeek = daysOfWeek,
    ringtone = ringtone,
    volume = volume,
    volumeRamp = volumeRamp,
    vibration = vibration,
    dismissType = dismissType,
    challengeConfig = challengeConfig,
    snoozeDurationMinutes = snoozeDurationMinutes,
    maxSnoozeCount = maxSnoozeCount,
    currentSnoozeCount = currentSnoozeCount,
    isSmartWakeEnabled = isSmartWakeEnabled,
    smartWakeWindowMinutes = smartWakeWindowMinutes,
    nextTriggerTime = nextTriggerTime,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun AlarmEntity.toDomain(): Alarm = Alarm(
    id = AlarmId(id),
    label = label,
    time = time,
    isEnabled = isEnabled,
    daysOfWeek = daysOfWeek,
    ringtone = ringtone,
    volume = volume,
    volumeRamp = volumeRamp,
    vibration = vibration,
    dismissType = dismissType,
    challengeConfig = challengeConfig,
    snoozeDurationMinutes = snoozeDurationMinutes,
    maxSnoozeCount = maxSnoozeCount,
    currentSnoozeCount = currentSnoozeCount,
    isSmartWakeEnabled = isSmartWakeEnabled,
    smartWakeWindowMinutes = smartWakeWindowMinutes,
    nextTriggerTime = nextTriggerTime,
    createdAt = createdAt,
    updatedAt = updatedAt
)
