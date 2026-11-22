package com.alarmapp.feature.settings

import android.content.Context
import com.alarmapp.data.backup.BackupManager
import com.alarmapp.data.backup.BackupPayload
import com.alarmapp.domain.model.Alarm
import com.alarmapp.domain.model.AlarmId
import com.alarmapp.domain.repository.AlarmRepository
import com.alarmapp.domain.repository.RoutineRepository
import com.alarmapp.domain.repository.SettingsRepository
import com.alarmapp.feature.settings.ui.SettingsViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalTime
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val context: Context = mockk(relaxed = true)
    private val settingsRepo: SettingsRepository = mockk(relaxed = true)
    private val alarmRepo: AlarmRepository = mockk(relaxed = true)
    private val routineRepo: RoutineRepository = mockk(relaxed = true)
    private val backupManager: BackupManager = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `exportBackup calls backupManager exportJson and invokes callback`() = runTest {
        val viewModel = SettingsViewModel(context, settingsRepo, alarmRepo, routineRepo, backupManager)
        val dummyBytes = "encrypted-data".toByteArray()
        coEvery { backupManager.exportJson(any()) } returns dummyBytes

        var exportedBytes: ByteArray? = null
        viewModel.exportBackup("pwd123") { exportedBytes = it }
        testScheduler.advanceUntilIdle()

        assertThat(exportedBytes).isEqualTo(dummyBytes)
        coVerify { backupManager.exportJson("pwd123") }
    }

    @Test
    fun `importBackup calls backupManager importJson and updates status`() = runTest {
        val viewModel = SettingsViewModel(context, settingsRepo, alarmRepo, routineRepo, backupManager)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        val payload = BackupPayload(
            alarms = listOf(
                Alarm(
                    id = AlarmId("bk-1"),
                    time = LocalTime(7, 30),
                    createdAt = Instant.parse("2026-11-22T00:00:00Z"),
                    updatedAt = Instant.parse("2026-11-22T00:00:00Z")
                )
            ),
            routines = emptyList()
        )
        coEvery { backupManager.importJson(any(), any()) } returns payload

        viewModel.importBackup("data".toByteArray(), "pwd123")
        testScheduler.advanceUntilIdle()

        assertThat(viewModel.uiState.value.backupMessage).contains("Restored 1 alarms")
    }
}
