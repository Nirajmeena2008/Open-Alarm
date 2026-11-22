package com.alarmapp.data.database;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\'\u0018\u0000 \u000f2\u00020\u0001:\u0001\u000fB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&J\b\u0010\u0007\u001a\u00020\bH&J\b\u0010\t\u001a\u00020\nH&J\b\u0010\u000b\u001a\u00020\fH&J\b\u0010\r\u001a\u00020\u000eH&\u00a8\u0006\u0010"}, d2 = {"Lcom/alarmapp/data/database/AlarmDatabase;", "Landroidx/room/RoomDatabase;", "()V", "alarmDao", "Lcom/alarmapp/data/dao/AlarmDao;", "pluginChallengeDao", "Lcom/alarmapp/data/dao/PluginChallengeDao;", "routineDao", "Lcom/alarmapp/data/dao/RoutineDao;", "settingsDao", "Lcom/alarmapp/data/dao/SettingsDao;", "sleepSessionDao", "Lcom/alarmapp/data/dao/SleepSessionDao;", "webhookDao", "Lcom/alarmapp/data/dao/WebhookDao;", "Companion", "data_debug"})
@androidx.room.Database(entities = {com.alarmapp.data.entity.AlarmEntity.class, com.alarmapp.data.entity.SleepSessionEntity.class, com.alarmapp.data.entity.RoutineEntity.class, com.alarmapp.data.entity.SettingEntity.class, com.alarmapp.data.entity.WebhookEntity.class, com.alarmapp.data.entity.PluginChallengeEntity.class}, version = 1, exportSchema = true)
@androidx.room.TypeConverters(value = {com.alarmapp.data.converter.Converters.class})
public abstract class AlarmDatabase extends androidx.room.RoomDatabase {
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String DATABASE_NAME = "openalarm.db";
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private static volatile com.alarmapp.data.database.AlarmDatabase INSTANCE;
    @org.jetbrains.annotations.NotNull()
    public static final com.alarmapp.data.database.AlarmDatabase.Companion Companion = null;
    
    public AlarmDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.alarmapp.data.dao.AlarmDao alarmDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.alarmapp.data.dao.SleepSessionDao sleepSessionDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.alarmapp.data.dao.RoutineDao routineDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.alarmapp.data.dao.SettingsDao settingsDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.alarmapp.data.dao.WebhookDao webhookDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.alarmapp.data.dao.PluginChallengeDao pluginChallengeDao();
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u000bJ\u000e\u0010\f\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\tR\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lcom/alarmapp/data/database/AlarmDatabase$Companion;", "", "()V", "DATABASE_NAME", "", "INSTANCE", "Lcom/alarmapp/data/database/AlarmDatabase;", "create", "context", "Landroid/content/Context;", "inMemory", "", "getInstance", "data_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.alarmapp.data.database.AlarmDatabase getInstance(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.alarmapp.data.database.AlarmDatabase create(@org.jetbrains.annotations.NotNull()
        android.content.Context context, boolean inMemory) {
            return null;
        }
    }
}