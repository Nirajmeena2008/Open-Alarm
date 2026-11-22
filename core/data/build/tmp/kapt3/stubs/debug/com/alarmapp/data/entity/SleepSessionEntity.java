package com.alarmapp.data.entity;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0014\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B?\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n\u0012\b\u0010\f\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\rJ\t\u0010\u0018\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\u0005H\u00c6\u0003J\u000b\u0010\u001a\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\t\u0010\u001b\u001a\u00020\bH\u00c6\u0003J\u000f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u000b0\nH\u00c6\u0003J\u000b\u0010\u001d\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003JO\u0010\u001e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u0007\u001a\u00020\b2\u000e\b\u0002\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n2\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0003H\u00c6\u0001J\u0013\u0010\u001f\u001a\u00020 2\b\u0010!\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\"\u001a\u00020#H\u00d6\u0001J\t\u0010$\u001a\u00020\u0003H\u00d6\u0001R\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0013\u0010\f\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0011R\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0017\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u000f\u00a8\u0006%"}, d2 = {"Lcom/alarmapp/data/entity/SleepSessionEntity;", "", "id", "", "startTime", "Lkotlinx/datetime/Instant;", "endTime", "quality", "Lcom/alarmapp/domain/model/SleepQuality;", "stages", "", "Lcom/alarmapp/domain/model/SleepStageRecord;", "notes", "(Ljava/lang/String;Lkotlinx/datetime/Instant;Lkotlinx/datetime/Instant;Lcom/alarmapp/domain/model/SleepQuality;Ljava/util/List;Ljava/lang/String;)V", "getEndTime", "()Lkotlinx/datetime/Instant;", "getId", "()Ljava/lang/String;", "getNotes", "getQuality", "()Lcom/alarmapp/domain/model/SleepQuality;", "getStages", "()Ljava/util/List;", "getStartTime", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "", "other", "hashCode", "", "toString", "data_debug"})
@androidx.room.Entity(tableName = "sleep_sessions")
public final class SleepSessionEntity {
    @androidx.room.PrimaryKey()
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String id = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.datetime.Instant startTime = null;
    @org.jetbrains.annotations.Nullable()
    private final kotlinx.datetime.Instant endTime = null;
    @org.jetbrains.annotations.NotNull()
    private final com.alarmapp.domain.model.SleepQuality quality = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.alarmapp.domain.model.SleepStageRecord> stages = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String notes = null;
    
    public SleepSessionEntity(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlinx.datetime.Instant startTime, @org.jetbrains.annotations.Nullable()
    kotlinx.datetime.Instant endTime, @org.jetbrains.annotations.NotNull()
    com.alarmapp.domain.model.SleepQuality quality, @org.jetbrains.annotations.NotNull()
    java.util.List<com.alarmapp.domain.model.SleepStageRecord> stages, @org.jetbrains.annotations.Nullable()
    java.lang.String notes) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.datetime.Instant getStartTime() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final kotlinx.datetime.Instant getEndTime() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.alarmapp.domain.model.SleepQuality getQuality() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.alarmapp.domain.model.SleepStageRecord> getStages() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getNotes() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.datetime.Instant component2() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final kotlinx.datetime.Instant component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.alarmapp.domain.model.SleepQuality component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.alarmapp.domain.model.SleepStageRecord> component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.alarmapp.data.entity.SleepSessionEntity copy(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlinx.datetime.Instant startTime, @org.jetbrains.annotations.Nullable()
    kotlinx.datetime.Instant endTime, @org.jetbrains.annotations.NotNull()
    com.alarmapp.domain.model.SleepQuality quality, @org.jetbrains.annotations.NotNull()
    java.util.List<com.alarmapp.domain.model.SleepStageRecord> stages, @org.jetbrains.annotations.Nullable()
    java.lang.String notes) {
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