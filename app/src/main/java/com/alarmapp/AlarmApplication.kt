package com.alarmapp

import android.app.Application
import androidx.room.Room
import com.alarmapp.data.backup.BackupManager
import com.alarmapp.data.database.AlarmDatabase
import com.alarmapp.data.repository.*
import com.alarmapp.domain.repository.*
import com.alarmapp.domain.usecase.alarm.*
import com.alarmapp.domain.usecase.sleep.*
import com.alarmapp.platform.alarm.AlarmScheduler
import com.alarmapp.platform.alarm.AlarmSchedulerImpl
import com.alarmapp.platform.notification.AlarmNotificationChannels

class AlarmApplication : Application() {

    lateinit var database: AlarmDatabase
        private set

    lateinit var alarmRepository: AlarmRepository
        private set

    lateinit var sleepRepository: SleepRepository
        private set

    lateinit var routineRepository: RoutineRepository
        private set

    lateinit var settingsRepository: SettingsRepository
        private set

    lateinit var webhookRepository: WebhookRepository
        private set

    lateinit var alarmScheduler: AlarmScheduler
        private set

    lateinit var backupManager: BackupManager
        private set

    // Use cases
    lateinit var getAlarmsUseCase: GetAlarmsUseCase
        private set
    lateinit var saveAlarmUseCase: SaveAlarmUseCase
        private set
    lateinit var toggleAlarmUseCase: ToggleAlarmUseCase
        private set
    lateinit var deleteAlarmUseCase: DeleteAlarmUseCase
        private set
    lateinit var snoozeAlarmUseCase: SnoozeAlarmUseCase
        private set
    lateinit var dismissAlarmUseCase: DismissAlarmUseCase
        private set
    lateinit var startSleepSessionUseCase: StartSleepSessionUseCase
        private set
    lateinit var endSleepSessionUseCase: EndSleepSessionUseCase
        private set
    lateinit var getSleepHistoryUseCase: GetSleepHistoryUseCase
        private set
    lateinit var routineUseCases: RoutineUseCases
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this

        AlarmNotificationChannels.createChannels(this)

        database = Room.databaseBuilder(
            applicationContext,
            AlarmDatabase::class.java,
            "openalarm.db"
        ).fallbackToDestructiveMigration().build()

        alarmRepository = AlarmRepositoryImpl(database.alarmDao())
        sleepRepository = SleepRepositoryImpl(database.sleepSessionDao())
        routineRepository = RoutineRepositoryImpl(database.routineDao())
        settingsRepository = SettingsRepositoryImpl(database.settingsDao())
        webhookRepository = WebhookRepositoryImpl(database.webhookDao())

        alarmScheduler = AlarmSchedulerImpl(this, alarmRepository)
        backupManager = BackupManager(alarmRepository, routineRepository)

        getAlarmsUseCase = GetAlarmsUseCase(alarmRepository)
        saveAlarmUseCase = SaveAlarmUseCase(alarmRepository)
        toggleAlarmUseCase = ToggleAlarmUseCase(alarmRepository)
        deleteAlarmUseCase = DeleteAlarmUseCase(alarmRepository)
        snoozeAlarmUseCase = SnoozeAlarmUseCase(alarmRepository)
        dismissAlarmUseCase = DismissAlarmUseCase(alarmRepository)

        startSleepSessionUseCase = StartSleepSessionUseCase(sleepRepository)
        endSleepSessionUseCase = EndSleepSessionUseCase(sleepRepository)
        getSleepHistoryUseCase = GetSleepHistoryUseCase(sleepRepository)
        routineUseCases = RoutineUseCases(routineRepository)
    }

    companion object {
        lateinit var instance: AlarmApplication
            private set
    }
}
