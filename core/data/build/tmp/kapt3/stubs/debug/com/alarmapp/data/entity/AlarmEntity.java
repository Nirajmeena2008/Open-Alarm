package com.alarmapp.data.entity;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b9\b\u0087\b\u0018\u00002\u00020\u0001B\u00a9\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n\u0012\u0006\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\u000f\u0012\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011\u0012\u0006\u0010\u0012\u001a\u00020\u0013\u0012\u0006\u0010\u0014\u001a\u00020\u0015\u0012\b\u0010\u0016\u001a\u0004\u0018\u00010\u0017\u0012\u0006\u0010\u0018\u001a\u00020\u0019\u0012\u0006\u0010\u001a\u001a\u00020\u0019\u0012\u0006\u0010\u001b\u001a\u00020\u0019\u0012\u0006\u0010\u001c\u001a\u00020\b\u0012\u0006\u0010\u001d\u001a\u00020\u0019\u0012\b\u0010\u001e\u001a\u0004\u0018\u00010\u001f\u0012\u0006\u0010 \u001a\u00020\u001f\u0012\u0006\u0010!\u001a\u00020\u001f\u00a2\u0006\u0002\u0010\"J\t\u0010@\u001a\u00020\u0003H\u00c6\u0003J\t\u0010A\u001a\u00020\u0015H\u00c6\u0003J\u000b\u0010B\u001a\u0004\u0018\u00010\u0017H\u00c6\u0003J\t\u0010C\u001a\u00020\u0019H\u00c6\u0003J\t\u0010D\u001a\u00020\u0019H\u00c6\u0003J\t\u0010E\u001a\u00020\u0019H\u00c6\u0003J\t\u0010F\u001a\u00020\bH\u00c6\u0003J\t\u0010G\u001a\u00020\u0019H\u00c6\u0003J\u000b\u0010H\u001a\u0004\u0018\u00010\u001fH\u00c6\u0003J\t\u0010I\u001a\u00020\u001fH\u00c6\u0003J\t\u0010J\u001a\u00020\u001fH\u00c6\u0003J\t\u0010K\u001a\u00020\u0003H\u00c6\u0003J\t\u0010L\u001a\u00020\u0006H\u00c6\u0003J\t\u0010M\u001a\u00020\bH\u00c6\u0003J\u000f\u0010N\u001a\b\u0012\u0004\u0012\u00020\u000b0\nH\u00c6\u0003J\t\u0010O\u001a\u00020\rH\u00c6\u0003J\t\u0010P\u001a\u00020\u000fH\u00c6\u0003J\u000b\u0010Q\u001a\u0004\u0018\u00010\u0011H\u00c6\u0003J\t\u0010R\u001a\u00020\u0013H\u00c6\u0003J\u00d3\u0001\u0010S\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\b2\u000e\b\u0002\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n2\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010\u000e\u001a\u00020\u000f2\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u00132\b\b\u0002\u0010\u0014\u001a\u00020\u00152\n\b\u0002\u0010\u0016\u001a\u0004\u0018\u00010\u00172\b\b\u0002\u0010\u0018\u001a\u00020\u00192\b\b\u0002\u0010\u001a\u001a\u00020\u00192\b\b\u0002\u0010\u001b\u001a\u00020\u00192\b\b\u0002\u0010\u001c\u001a\u00020\b2\b\b\u0002\u0010\u001d\u001a\u00020\u00192\n\b\u0002\u0010\u001e\u001a\u0004\u0018\u00010\u001f2\b\b\u0002\u0010 \u001a\u00020\u001f2\b\b\u0002\u0010!\u001a\u00020\u001fH\u00c6\u0001J\u0013\u0010T\u001a\u00020\b2\b\u0010U\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010V\u001a\u00020\u0019H\u00d6\u0001J\t\u0010W\u001a\u00020\u0003H\u00d6\u0001R\u0013\u0010\u0016\u001a\u0004\u0018\u00010\u0017\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010$R\u0011\u0010 \u001a\u00020\u001f\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010&R\u0011\u0010\u001b\u001a\u00020\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010(R\u0017\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010*R\u0011\u0010\u0014\u001a\u00020\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010,R\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b-\u0010.R\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010/R\u0011\u0010\u001c\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010/R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b0\u0010.R\u0011\u0010\u001a\u001a\u00020\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b1\u0010(R\u0013\u0010\u001e\u001a\u0004\u0018\u00010\u001f\u00a2\u0006\b\n\u0000\u001a\u0004\b2\u0010&R\u0011\u0010\f\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b3\u00104R\u0011\u0010\u001d\u001a\u00020\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b5\u0010(R\u0011\u0010\u0018\u001a\u00020\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b6\u0010(R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b7\u00108R\u0011\u0010!\u001a\u00020\u001f\u00a2\u0006\b\n\u0000\u001a\u0004\b9\u0010&R\u0011\u0010\u0012\u001a\u00020\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b:\u0010;R\u0011\u0010\u000e\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b<\u0010=R\u0013\u0010\u0010\u001a\u0004\u0018\u00010\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b>\u0010?\u00a8\u0006X"}, d2 = {"Lcom/alarmapp/data/entity/AlarmEntity;", "", "id", "", "label", "time", "Lkotlinx/datetime/LocalTime;", "isEnabled", "", "daysOfWeek", "", "Lcom/alarmapp/domain/model/DayOfWeek;", "ringtone", "Lcom/alarmapp/domain/model/RingtoneSource;", "volume", "", "volumeRamp", "Lcom/alarmapp/domain/model/VolumeRamp;", "vibration", "Lcom/alarmapp/domain/model/VibrationPattern;", "dismissType", "Lcom/alarmapp/domain/model/DismissType;", "challengeConfig", "Lcom/alarmapp/domain/model/ChallengeConfig;", "snoozeDurationMinutes", "", "maxSnoozeCount", "currentSnoozeCount", "isSmartWakeEnabled", "smartWakeWindowMinutes", "nextTriggerTime", "Lkotlinx/datetime/Instant;", "createdAt", "updatedAt", "(Ljava/lang/String;Ljava/lang/String;Lkotlinx/datetime/LocalTime;ZLjava/util/Set;Lcom/alarmapp/domain/model/RingtoneSource;FLcom/alarmapp/domain/model/VolumeRamp;Lcom/alarmapp/domain/model/VibrationPattern;Lcom/alarmapp/domain/model/DismissType;Lcom/alarmapp/domain/model/ChallengeConfig;IIIZILkotlinx/datetime/Instant;Lkotlinx/datetime/Instant;Lkotlinx/datetime/Instant;)V", "getChallengeConfig", "()Lcom/alarmapp/domain/model/ChallengeConfig;", "getCreatedAt", "()Lkotlinx/datetime/Instant;", "getCurrentSnoozeCount", "()I", "getDaysOfWeek", "()Ljava/util/Set;", "getDismissType", "()Lcom/alarmapp/domain/model/DismissType;", "getId", "()Ljava/lang/String;", "()Z", "getLabel", "getMaxSnoozeCount", "getNextTriggerTime", "getRingtone", "()Lcom/alarmapp/domain/model/RingtoneSource;", "getSmartWakeWindowMinutes", "getSnoozeDurationMinutes", "getTime", "()Lkotlinx/datetime/LocalTime;", "getUpdatedAt", "getVibration", "()Lcom/alarmapp/domain/model/VibrationPattern;", "getVolume", "()F", "getVolumeRamp", "()Lcom/alarmapp/domain/model/VolumeRamp;", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "toString", "data_debug"})
@androidx.room.Entity(tableName = "alarms")
public final class AlarmEntity {
    @androidx.room.PrimaryKey()
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String id = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String label = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.datetime.LocalTime time = null;
    private final boolean isEnabled = false;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Set<com.alarmapp.domain.model.DayOfWeek> daysOfWeek = null;
    @org.jetbrains.annotations.NotNull()
    private final com.alarmapp.domain.model.RingtoneSource ringtone = null;
    private final float volume = 0.0F;
    @org.jetbrains.annotations.Nullable()
    private final com.alarmapp.domain.model.VolumeRamp volumeRamp = null;
    @org.jetbrains.annotations.NotNull()
    private final com.alarmapp.domain.model.VibrationPattern vibration = null;
    @org.jetbrains.annotations.NotNull()
    private final com.alarmapp.domain.model.DismissType dismissType = null;
    @org.jetbrains.annotations.Nullable()
    private final com.alarmapp.domain.model.ChallengeConfig challengeConfig = null;
    private final int snoozeDurationMinutes = 0;
    private final int maxSnoozeCount = 0;
    private final int currentSnoozeCount = 0;
    private final boolean isSmartWakeEnabled = false;
    private final int smartWakeWindowMinutes = 0;
    @org.jetbrains.annotations.Nullable()
    private final kotlinx.datetime.Instant nextTriggerTime = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.datetime.Instant createdAt = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.datetime.Instant updatedAt = null;
    
    public AlarmEntity(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    java.lang.String label, @org.jetbrains.annotations.NotNull()
    kotlinx.datetime.LocalTime time, boolean isEnabled, @org.jetbrains.annotations.NotNull()
    java.util.Set<? extends com.alarmapp.domain.model.DayOfWeek> daysOfWeek, @org.jetbrains.annotations.NotNull()
    com.alarmapp.domain.model.RingtoneSource ringtone, float volume, @org.jetbrains.annotations.Nullable()
    com.alarmapp.domain.model.VolumeRamp volumeRamp, @org.jetbrains.annotations.NotNull()
    com.alarmapp.domain.model.VibrationPattern vibration, @org.jetbrains.annotations.NotNull()
    com.alarmapp.domain.model.DismissType dismissType, @org.jetbrains.annotations.Nullable()
    com.alarmapp.domain.model.ChallengeConfig challengeConfig, int snoozeDurationMinutes, int maxSnoozeCount, int currentSnoozeCount, boolean isSmartWakeEnabled, int smartWakeWindowMinutes, @org.jetbrains.annotations.Nullable()
    kotlinx.datetime.Instant nextTriggerTime, @org.jetbrains.annotations.NotNull()
    kotlinx.datetime.Instant createdAt, @org.jetbrains.annotations.NotNull()
    kotlinx.datetime.Instant updatedAt) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getLabel() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.datetime.LocalTime getTime() {
        return null;
    }
    
    public final boolean isEnabled() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Set<com.alarmapp.domain.model.DayOfWeek> getDaysOfWeek() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.alarmapp.domain.model.RingtoneSource getRingtone() {
        return null;
    }
    
    public final float getVolume() {
        return 0.0F;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.alarmapp.domain.model.VolumeRamp getVolumeRamp() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.alarmapp.domain.model.VibrationPattern getVibration() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.alarmapp.domain.model.DismissType getDismissType() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.alarmapp.domain.model.ChallengeConfig getChallengeConfig() {
        return null;
    }
    
    public final int getSnoozeDurationMinutes() {
        return 0;
    }
    
    public final int getMaxSnoozeCount() {
        return 0;
    }
    
    public final int getCurrentSnoozeCount() {
        return 0;
    }
    
    public final boolean isSmartWakeEnabled() {
        return false;
    }
    
    public final int getSmartWakeWindowMinutes() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final kotlinx.datetime.Instant getNextTriggerTime() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.datetime.Instant getCreatedAt() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.datetime.Instant getUpdatedAt() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.alarmapp.domain.model.DismissType component10() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.alarmapp.domain.model.ChallengeConfig component11() {
        return null;
    }
    
    public final int component12() {
        return 0;
    }
    
    public final int component13() {
        return 0;
    }
    
    public final int component14() {
        return 0;
    }
    
    public final boolean component15() {
        return false;
    }
    
    public final int component16() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final kotlinx.datetime.Instant component17() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.datetime.Instant component18() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.datetime.Instant component19() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.datetime.LocalTime component3() {
        return null;
    }
    
    public final boolean component4() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Set<com.alarmapp.domain.model.DayOfWeek> component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.alarmapp.domain.model.RingtoneSource component6() {
        return null;
    }
    
    public final float component7() {
        return 0.0F;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.alarmapp.domain.model.VolumeRamp component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.alarmapp.domain.model.VibrationPattern component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.alarmapp.data.entity.AlarmEntity copy(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    java.lang.String label, @org.jetbrains.annotations.NotNull()
    kotlinx.datetime.LocalTime time, boolean isEnabled, @org.jetbrains.annotations.NotNull()
    java.util.Set<? extends com.alarmapp.domain.model.DayOfWeek> daysOfWeek, @org.jetbrains.annotations.NotNull()
    com.alarmapp.domain.model.RingtoneSource ringtone, float volume, @org.jetbrains.annotations.Nullable()
    com.alarmapp.domain.model.VolumeRamp volumeRamp, @org.jetbrains.annotations.NotNull()
    com.alarmapp.domain.model.VibrationPattern vibration, @org.jetbrains.annotations.NotNull()
    com.alarmapp.domain.model.DismissType dismissType, @org.jetbrains.annotations.Nullable()
    com.alarmapp.domain.model.ChallengeConfig challengeConfig, int snoozeDurationMinutes, int maxSnoozeCount, int currentSnoozeCount, boolean isSmartWakeEnabled, int smartWakeWindowMinutes, @org.jetbrains.annotations.Nullable()
    kotlinx.datetime.Instant nextTriggerTime, @org.jetbrains.annotations.NotNull()
    kotlinx.datetime.Instant createdAt, @org.jetbrains.annotations.NotNull()
    kotlinx.datetime.Instant updatedAt) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}