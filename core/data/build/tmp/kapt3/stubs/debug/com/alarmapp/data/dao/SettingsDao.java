package com.alarmapp.data.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bg\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00040\u00032\u0006\u0010\u0005\u001a\u00020\u0004H\'J\u0016\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\n\u00a8\u0006\u000b"}, d2 = {"Lcom/alarmapp/data/dao/SettingsDao;", "", "getSetting", "Lkotlinx/coroutines/flow/Flow;", "", "key", "setSetting", "", "setting", "Lcom/alarmapp/data/entity/SettingEntity;", "(Lcom/alarmapp/data/entity/SettingEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "data_debug"})
@androidx.room.Dao()
public abstract interface SettingsDao {
    
    @androidx.room.Query(value = "SELECT value FROM settings WHERE `key` = :key LIMIT 1")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.lang.String> getSetting(@org.jetbrains.annotations.NotNull()
    java.lang.String key);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object setSetting(@org.jetbrains.annotations.NotNull()
    com.alarmapp.data.entity.SettingEntity setting, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}