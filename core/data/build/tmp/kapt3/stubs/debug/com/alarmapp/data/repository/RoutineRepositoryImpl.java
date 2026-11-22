package com.alarmapp.data.repository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0096@\u00a2\u0006\u0002\u0010\tJ\u0018\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000b2\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0014\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000e0\u000bH\u0016J\u0016\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\fH\u0096@\u00a2\u0006\u0002\u0010\u0011R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2 = {"Lcom/alarmapp/data/repository/RoutineRepositoryImpl;", "Lcom/alarmapp/domain/repository/RoutineRepository;", "dao", "Lcom/alarmapp/data/dao/RoutineDao;", "(Lcom/alarmapp/data/dao/RoutineDao;)V", "deleteRoutine", "", "id", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getRoutine", "Lkotlinx/coroutines/flow/Flow;", "Lcom/alarmapp/domain/model/Routine;", "getRoutines", "", "saveRoutine", "routine", "(Lcom/alarmapp/domain/model/Routine;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "data_debug"})
public final class RoutineRepositoryImpl implements com.alarmapp.domain.repository.RoutineRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.alarmapp.data.dao.RoutineDao dao = null;
    
    public RoutineRepositoryImpl(@org.jetbrains.annotations.NotNull()
    com.alarmapp.data.dao.RoutineDao dao) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.alarmapp.domain.model.Routine>> getRoutines() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<com.alarmapp.domain.model.Routine> getRoutine(@org.jetbrains.annotations.NotNull()
    java.lang.String id) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object saveRoutine(@org.jetbrains.annotations.NotNull()
    com.alarmapp.domain.model.Routine routine, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteRoutine(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}