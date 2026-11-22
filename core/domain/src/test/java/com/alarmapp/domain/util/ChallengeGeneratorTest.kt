package com.alarmapp.domain.util

import com.alarmapp.domain.model.ChallengeConfig
import com.alarmapp.domain.model.MathDifficulty
import com.alarmapp.domain.usecase.alarm.ValidateChallengeAnswerUseCase
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class ChallengeGeneratorTest {

    private val validator = ValidateChallengeAnswerUseCase()

    @Test
    fun `generateMathProblem produces deterministic output for same seed`() {
        val problem1 = ChallengeGenerator.generateMathProblem(MathDifficulty.EASY, 42L)
        val problem2 = ChallengeGenerator.generateMathProblem(MathDifficulty.EASY, 42L)

        assertThat(problem1.question).isEqualTo(problem2.question)
        assertThat(problem1.answer).isEqualTo(problem2.answer)
    }

    @Test
    fun `generateMathProblem answers validate correctly`() {
        for (difficulty in MathDifficulty.entries) {
            val problem = ChallengeGenerator.generateMathProblem(difficulty, 12345L)
            val isValid = validator(
                config = ChallengeConfig.Math(difficulty),
                userAnswer = problem.answer.toString(),
                expectedAnswer = problem.answer.toString()
            )
            assertThat(isValid).isTrue()
        }
    }

    @Test
    fun `generateMemorySequence produces sequence bounded by dimensions`() {
        val sequence = ChallengeGenerator.generateMemorySequence(dimension = 3, sequenceLength = 6, seed = 999L)
        assertThat(sequence).hasSize(6)
        for (cell in sequence) {
            assertThat(cell).isAtLeast(0)
            assertThat(cell).isLessThan(9)
        }
    }

    @Test
    fun `validator handles NFC, QR, and Shake properly`() {
        // NFC check (case-insensitive)
        val nfcConfig = ChallengeConfig.Nfc(registeredTagId = "04:A1:B2:C3:D4")
        assertThat(validator(nfcConfig, "04:a1:b2:c3:d4", "")).isTrue()
        assertThat(validator(nfcConfig, "wrong_tag", "")).isFalse()

        // QR check
        val qrConfig = ChallengeConfig.Qr(expectedPayload = "ALARM_BATHROOM_QR_2026")
        assertThat(validator(qrConfig, "ALARM_BATHROOM_QR_2026", "")).isTrue()
        assertThat(validator(qrConfig, "INVALID_QR", "")).isFalse()

        // Shake check
        val shakeConfig = ChallengeConfig.Shake(targetShakes = 25)
        assertThat(validator(shakeConfig, "25", "")).isTrue()
        assertThat(validator(shakeConfig, "30", "")).isTrue()
        assertThat(validator(shakeConfig, "24", "")).isFalse()
    }
}
