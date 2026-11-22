package com.alarmapp.data.repository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0014\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006H\u0016J\u0016\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\bH\u0096@\u00a2\u0006\u0002\u0010\fJ\u0016\u0010\r\u001a\u00020\n2\u0006\u0010\u000e\u001a\u00020\u000fH\u0096@\u00a2\u0006\u0002\u0010\u0010R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcom/alarmapp/data/repository/PluginRepositoryImpl;", "Lcom/alarmapp/domain/repository/PluginRepository;", "dao", "Lcom/alarmapp/data/dao/PluginChallengeDao;", "(Lcom/alarmapp/data/dao/PluginChallengeDao;)V", "getInstalledPlugins", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/alarmapp/domain/model/PluginChallenge;", "registerPlugin", "", "plugin", "(Lcom/alarmapp/domain/model/PluginChallenge;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "unregisterPlugin", "pluginId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "data_debug"})
public final class PluginRepositoryImpl implements com.alarmapp.domain.repository.PluginRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.alarmapp.data.dao.PluginChallengeDao dao = null;
    
    public PluginRepositoryImpl(@org.jetbrains.annotations.NotNull()
    com.alarmapp.data.dao.PluginChallengeDao dao) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.alarmapp.domain.model.PluginChallenge>> getInstalledPlugins() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object registerPlugin(@org.jetbrains.annotations.NotNull()
    com.alarmapp.domain.model.PluginChallenge plugin, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object unregisterPlugin(@org.jetbrains.annotations.NotNull()
    java.lang.String pluginId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}