package com.alarmapp.domain.util

import com.alarmapp.domain.model.MathDifficulty
import kotlin.random.Random

data class MathProblem(
    val question: String,
    val answer: Int
)

object ChallengeGenerator {

    /**
     * Generates a deterministic math problem given difficulty and a seed.
     */
    fun generateMathProblem(difficulty: MathDifficulty, seed: Long): MathProblem {
        val random = Random(seed)
        return when (difficulty) {
            MathDifficulty.EASY -> {
                val a = random.nextInt(5, 30)
                val b = random.nextInt(3, 20)
                val isAddition = random.nextBoolean()
                if (isAddition) {
                    MathProblem("$a + $b", a + b)
                } else {
                    val max = maxOf(a, b)
                    val min = minOf(a, b)
                    MathProblem("$max - $min", max - min)
                }
            }
            MathDifficulty.MEDIUM -> {
                val a = random.nextInt(6, 16)
                val b = random.nextInt(3, 12)
                val c = random.nextInt(5, 25)
                val product = a * b
                val isAdd = random.nextBoolean()
                if (isAdd) {
                    MathProblem("$a × $b + $c", product + c)
                } else {
                    MathProblem("$a × $b - $c", product - c)
                }
            }
            MathDifficulty.HARD -> {
                val a = random.nextInt(12, 35)
                val b = random.nextInt(7, 19)
                val c = random.nextInt(15, 60)
                val d = random.nextInt(2, 6)
                // (a * b) - (c / d)
                val cAdjusted = (c / d) * d
                val div = cAdjusted / d
                val prod = a * b
                MathProblem("($a × $b) - ($cAdjusted ÷ $d)", prod - div)
            }
        }
    }

    /**
     * Generates a deterministic memory sequence of grid cell indexes [0 until dimension * dimension].
     */
    fun generateMemorySequence(dimension: Int, sequenceLength: Int, seed: Long): List<Int> {
        val totalCells = dimension * dimension
        val random = Random(seed)
        return List(sequenceLength) {
            random.nextInt(0, totalCells)
        }
    }
}
