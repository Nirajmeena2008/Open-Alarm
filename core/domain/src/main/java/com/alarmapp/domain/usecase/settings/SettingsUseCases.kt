package com.alarmapp.domain.usecase.settings

import com.alarmapp.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class GetSettingsUseCase(private val repository: SettingsRepository) {
    fun getString(key: String, default: String): Flow<String> = repository.getString(key, default)
    fun getBoolean(key: String, default: Boolean): Flow<Boolean> = repository.getBoolean(key, default)
    fun getInt(key: String, default: Int): Flow<Int> = repository.getInt(key, default)
}

class UpdateSettingsUseCase(private val repository: SettingsRepository) {
    suspend fun setString(key: String, value: String) = repository.setString(key, value)
    suspend fun setBoolean(key: String, value: Boolean) = repository.setBoolean(key, value)
    suspend fun setInt(key: String, value: Int) = repository.setInt(key, value)
}
