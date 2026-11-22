package com.alarmapp.data.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0018\u0010\u000b\u001a\u0004\u0018\u00010\u00052\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0018\u0010\f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00050\r2\u0006\u0010\b\u001a\u00020\tH\'J\u0014\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u000f0\rH\'J\u0014\u0010\u0010\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u000f0\rH\'J\u001e\u0010\u0011\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u0012\u001a\u00020\u0013H\u00a7@\u00a2\u0006\u0002\u0010\u0014J \u0010\u0015\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\t2\b\u0010\u0016\u001a\u0004\u0018\u00010\u0017H\u00a7@\u00a2\u0006\u0002\u0010\u0018J(\u0010\u0019\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u001a\u001a\u00020\u001b2\b\u0010\u0016\u001a\u0004\u0018\u00010\u0017H\u00a7@\u00a2\u0006\u0002\u0010\u001cJ\u0016\u0010\u001d\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006\u00a8\u0006\u001e"}, d2 = {"Lcom/alarmapp/data/dao/AlarmDao;", "", "deleteAlarm", "", "alarm", "Lcom/alarmapp/data/entity/AlarmEntity;", "(Lcom/alarmapp/data/entity/AlarmEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteAlarmById", "id", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAlarm", "getAlarmById", "Lkotlinx/coroutines/flow/Flow;", "getAllAlarms", "", "getEnabledAlarms", "updateAlarmEnabled", "isEnabled", "", "(Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateNextTrigger", "nextTrigger", "", "(Ljava/lang/String;Ljava/lang/Long;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateSnoozeState", "snoozeCount", "", "(Ljava/lang/String;ILjava/lang/Long;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "upsertAlarm", "data_debug"})
@androidx.room.Dao()
public abstract interface AlarmDao {
    
    @androidx.room.Query(value = "SELECT * FROM alarms ORDER BY time ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.alarmapp.data.entity.AlarmEntity>> getAllAlarms();
    
    @androidx.room.Query(value = "SELECT * FROM alarms WHERE isEnabled = 1 ORDER BY time ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.alarmapp.data.entity.AlarmEntity>> getEnabledAlarms();
    
    @androidx.room.Query(value = "SELECT * FROM alarms WHERE id = :id LIMIT 1")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.alarmapp.data.entity.AlarmEntity> getAlarmById(@org.jetbrains.annotations.NotNull()
    java.lang.String id);
    
    @androidx.room.Query(value = "SELECT * FROM alarms WHERE id = :id LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAlarm(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.alarmapp.data.entity.AlarmEntity> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object upsertAlarm(@org.jetbrains.annotations.NotNull()
    com.alarmapp.data.entity.AlarmEntity alarm, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteAlarm(@org.jetbrains.annotations.NotNull()
    com.alarmapp.data.entity.AlarmEntity alarm, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM alarms WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteAlarmById(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE alarms SET isEnabled = :isEnabled WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateAlarmEnabled(@org.jetbrains.annotations.NotNull()
    java.lang.String id, boolean isEnabled, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE alarms SET nextTriggerTime = :nextTrigger WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateNextTrigger(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.Nullable()
    java.lang.Long nextTrigger, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE alarms SET currentSnoozeCount = :snoozeCount, nextTriggerTime = :nextTrigger WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateSnoozeState(@org.jetbrains.annotations.NotNull()
    java.lang.String id, int snoozeCount, @org.jetbrains.annotations.Nullable()
    java.lang.Long nextTrigger, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}