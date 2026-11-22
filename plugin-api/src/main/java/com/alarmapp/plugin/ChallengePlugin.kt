package com.alarmapp.plugin

import kotlinx.serialization.Serializable

@Serializable
data class ChallengePluginMetadata(
    val id: String,
    val name: String,
    val description: String,
    val version: String,
    val author: String
)

sealed interface ChallengeExecutionResult {
    data object Success : ChallengeExecutionResult
    data class Failure(val reason: String) : ChallengeExecutionResult
    data object Cancelled : ChallengeExecutionResult
}

interface DismissChallengePlugin {
    val metadata: ChallengePluginMetadata
    suspend fun startChallenge(configJson: String): ChallengeExecutionResult
}
