package com.alarmapp.data.backup

import com.alarmapp.domain.model.Alarm
import com.alarmapp.domain.model.AlarmId
import com.alarmapp.domain.model.DayOfWeek
import com.alarmapp.domain.model.Routine
import com.alarmapp.domain.model.RoutineStep
import com.alarmapp.domain.repository.AlarmRepository
import com.alarmapp.domain.repository.RoutineRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalTime
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class BackupManagerTest {

    private val alarmRepo: AlarmRepository = mockk(relaxed = true)
    private val routineRepo: RoutineRepository = mockk(relaxed = true)
    private lateinit var backupManager: BackupManager

    private val testAlarm = Alarm(
        id = AlarmId("backup-alarm-1"),
        label = "Workout",
        time = LocalTime(6, 0),
        isEnabled = true,
        daysOfWeek = setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY),
        createdAt = Instant.parse("2026-11-22T06:00:00Z"),
        updatedAt = Instant.parse("2026-11-22T06:00:00Z")
    )

    private val testRoutine = Routine(
        id = "routine-1",
        name = "Night Wind Down",
        steps = listOf(
            RoutineStep(id = "step-1", title = "Stretch", durationMinutes = 10, order = 1),
            RoutineStep(id = "step-2", title = "Read", durationMinutes = 20, order = 2)
        )
    )

    @BeforeEach
    fun setup() {
        coEvery { alarmRepo.getAllAlarms() } returns flowOf(listOf(testAlarm))
        coEvery { routineRepo.getRoutines() } returns flowOf(listOf(testRoutine))
        backupManager = BackupManager(alarmRepo, routineRepo)
    }

    @Test
    fun `export and import without encryption preserves alarms and routines`() = runTest {
        val exportedBytes = backupManager.exportJson(passphrase = null)
        assertThat(exportedBytes.size).isGreaterThan(0)

        val imported = backupManager.importJson(exportedBytes, passphrase = null)

        assertThat(imported.alarms).hasSize(1)
        assertThat(imported.alarms.first().label).isEqualTo("Workout")
        assertThat(imported.routines).hasSize(1)
        assertThat(imported.routines.first().steps).hasSize(2)

        coVerify {
            alarmRepo.upsertAlarm(testAlarm)
            routineRepo.saveRoutine(testRoutine)
        }
    }

    @Test
    fun `export with encryption requires same passphrase to import`() = runTest {
        val passphrase = "my-secure-alarm-backup"
        val encryptedBytes = backupManager.exportJson(passphrase = passphrase)

        val imported = backupManager.importJson(encryptedBytes, passphrase = passphrase)
        assertThat(imported.alarms).hasSize(1)
        assertThat(imported.alarms.first().id).isEqualTo(AlarmId("backup-alarm-1"))
    }
}
