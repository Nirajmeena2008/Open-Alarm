package com.alarmapp.data.backup;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u000e\u0010\t\u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u0010\u000bJ\u001a\u0010\f\u001a\u00020\r2\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0086@\u00a2\u0006\u0002\u0010\u0010J\"\u0010\u0011\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\r2\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0086@\u00a2\u0006\u0002\u0010\u0013R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2 = {"Lcom/alarmapp/data/backup/BackupManager;", "", "alarmRepository", "Lcom/alarmapp/domain/repository/AlarmRepository;", "routineRepository", "Lcom/alarmapp/domain/repository/RoutineRepository;", "(Lcom/alarmapp/domain/repository/AlarmRepository;Lcom/alarmapp/domain/repository/RoutineRepository;)V", "json", "Lkotlinx/serialization/json/Json;", "createBackupPayload", "Lcom/alarmapp/data/backup/BackupPayload;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "exportJson", "", "passphrase", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "importJson", "data", "([BLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "data_debug"})
public final class BackupManager {
    @org.jetbrains.annotations.NotNull()
    private final com.alarmapp.domain.repository.AlarmRepository alarmRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.alarmapp.domain.repository.RoutineRepository routineRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.serialization.json.Json json = null;
    
    public BackupManager(@org.jetbrains.annotations.NotNull()
    com.alarmapp.domain.repository.AlarmRepository alarmRepository, @org.jetbrains.annotations.NotNull()
    com.alarmapp.domain.repository.RoutineRepository routineRepository) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object createBackupPayload(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.alarmapp.data.backup.BackupPayload> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object exportJson(@org.jetbrains.annotations.Nullable()
    java.lang.String passphrase, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super byte[]> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object importJson(@org.jetbrains.annotations.NotNull()
    byte[] data, @org.jetbrains.annotations.Nullable()
    java.lang.String passphrase, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.alarmapp.data.backup.BackupPayload> $completion) {
        return null;
    }
}