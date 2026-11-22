package com.alarmapp.data.backup

import com.alarmapp.domain.model.Alarm
import com.alarmapp.domain.model.Routine
import com.alarmapp.domain.repository.AlarmRepository
import com.alarmapp.domain.repository.RoutineRepository
import kotlinx.coroutines.flow.first
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class BackupPayload(
    val version: Int = 1,
    val alarms: List<Alarm> = emptyList(),
    val routines: List<Routine> = emptyList()
)

class BackupManager(
    private val alarmRepository: AlarmRepository,
    private val routineRepository: RoutineRepository
) {
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    suspend fun createBackupPayload(): BackupPayload {
        val alarms = alarmRepository.getAllAlarms().first()
        val routines = routineRepository.getRoutines().first()
        return BackupPayload(version = 1, alarms = alarms, routines = routines)
    }

    suspend fun exportJson(passphrase: String? = null): ByteArray {
        val payload = createBackupPayload()
        val jsonString = json.encodeToString(payload)
        val bytes = jsonString.toByteArray(Charsets.UTF_8)
        return if (!passphrase.isNullOrBlank()) {
            BackupEncryption.encrypt(bytes, passphrase)
        } else {
            bytes
        }
    }

    suspend fun importJson(data: ByteArray, passphrase: String? = null): BackupPayload {
        val plaintextBytes = if (!passphrase.isNullOrBlank()) {
            BackupEncryption.decrypt(data, passphrase)
        } else {
            data
        }
        val jsonString = plaintextBytes.toString(Charsets.UTF_8)
        val payload = json.decodeFromString<BackupPayload>(jsonString)

        // Restore alarms
        for (alarm in payload.alarms) {
            alarmRepository.upsertAlarm(alarm)
        }
        // Restore routines
        for (routine in payload.routines) {
            routineRepository.saveRoutine(routine)
        }
        return payload
    }
}
