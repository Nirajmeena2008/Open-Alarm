package com.alarmapp.data.repository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001b\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0004\b\t\u0010\nJ\u001d\u0010\u000b\u001a\u0004\u0018\u00010\f2\u0006\u0010\u0007\u001a\u00020\bH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0004\b\r\u0010\nJ\"\u0010\u000e\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000f2\u0006\u0010\u0007\u001a\u00020\bH\u0016\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0010\u0010\u0011J\u0014\u0010\u0012\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u00130\u000fH\u0016J\u0014\u0010\u0014\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u00130\u000fH\u0016J#\u0010\u0015\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0016\u001a\u00020\u0017H\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0018\u0010\u0019J%\u0010\u001a\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\b\u0010\u001b\u001a\u0004\u0018\u00010\u001cH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001d\u0010\u001eJ-\u0010\u001f\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010 \u001a\u00020!2\b\u0010\u001b\u001a\u0004\u0018\u00010\u001cH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0004\b\"\u0010#J\u001e\u0010$\u001a\u00020\b2\u0006\u0010%\u001a\u00020\fH\u0096@\u00f8\u0001\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b&\u0010\'R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0005\b\u00a1\u001e0\u0001\n\u0002\b!\u00a8\u0006("}, d2 = {"Lcom/alarmapp/data/repository/AlarmRepositoryImpl;", "Lcom/alarmapp/domain/repository/AlarmRepository;", "alarmDao", "Lcom/alarmapp/data/dao/AlarmDao;", "(Lcom/alarmapp/data/dao/AlarmDao;)V", "deleteAlarm", "", "id", "Lcom/alarmapp/domain/model/AlarmId;", "deleteAlarm-US-NAac", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAlarm", "Lcom/alarmapp/domain/model/Alarm;", "getAlarm-US-NAac", "getAlarmById", "Lkotlinx/coroutines/flow/Flow;", "getAlarmById-E4AVW0M", "(Ljava/lang/String;)Lkotlinx/coroutines/flow/Flow;", "getAllAlarms", "", "getEnabledAlarms", "updateAlarmEnabled", "isEnabled", "", "updateAlarmEnabled-7Ffs0r0", "(Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateNextTrigger", "nextTrigger", "Lkotlinx/datetime/Instant;", "updateNextTrigger-7Ffs0r0", "(Ljava/lang/String;Lkotlinx/datetime/Instant;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateSnoozeState", "snoozeCount", "", "updateSnoozeState-Q7CzVdw", "(Ljava/lang/String;ILkotlinx/datetime/Instant;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "upsertAlarm", "alarm", "upsertAlarm-2VUH1Vo", "(Lcom/alarmapp/domain/model/Alarm;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "data_debug"})
public final class AlarmRepositoryImpl implements com.alarmapp.domain.repository.AlarmRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.alarmapp.data.dao.AlarmDao alarmDao = null;
    
    public AlarmRepositoryImpl(@org.jetbrains.annotations.NotNull()
    com.alarmapp.data.dao.AlarmDao alarmDao) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.alarmapp.domain.model.Alarm>> getAllAlarms() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.alarmapp.domain.model.Alarm>> getEnabledAlarms() {
        return null;
    }
}