package com.alarmapp.data.backup;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\f\u0010\r\u001a\u00060\u000ej\u0002`\u000fH\u0007J\f\u0010\u0010\u001a\u00060\u000ej\u0002`\u000fH\u0007J\b\u0010\u0011\u001a\u00020\u000eH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2 = {"Lcom/alarmapp/data/backup/BackupManagerTest;", "", "()V", "alarmRepo", "Lcom/alarmapp/domain/repository/AlarmRepository;", "backupManager", "Lcom/alarmapp/data/backup/BackupManager;", "routineRepo", "Lcom/alarmapp/domain/repository/RoutineRepository;", "testAlarm", "Lcom/alarmapp/domain/model/Alarm;", "testRoutine", "Lcom/alarmapp/domain/model/Routine;", "export and import without encryption preserves alarms and routines", "", "Lkotlinx/coroutines/test/TestResult;", "export with encryption requires same passphrase to import", "setup", "data_debugUnitTest"})
public final class BackupManagerTest {
    @org.jetbrains.annotations.NotNull()
    private final com.alarmapp.domain.repository.AlarmRepository alarmRepo = null;
    @org.jetbrains.annotations.NotNull()
    private final com.alarmapp.domain.repository.RoutineRepository routineRepo = null;
    private com.alarmapp.data.backup.BackupManager backupManager;
    @org.jetbrains.annotations.NotNull()
    private final com.alarmapp.domain.model.Alarm testAlarm = null;
    @org.jetbrains.annotations.NotNull()
    private final com.alarmapp.domain.model.Routine testRoutine = null;
    
    public BackupManagerTest() {
        super();
    }
    
    @org.junit.jupiter.api.BeforeEach()
    public final void setup() {
    }
}