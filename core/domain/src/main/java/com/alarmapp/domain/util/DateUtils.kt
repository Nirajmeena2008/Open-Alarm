package com.alarmapp.domain.util

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalTime

object DateUtils {

    fun formatRemainingTime(now: Instant, target: Instant): String {
        val diffMs = target.toEpochMilliseconds() - now.toEpochMilliseconds()
        if (diffMs <= 0) return "Due now"

        val totalMinutes = diffMs / (1000 * 60)
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60

        return when {
            hours > 0 && minutes > 0 -> "in ${hours}h ${minutes}m"
            hours > 0 -> "in ${hours}h"
            minutes > 0 -> "in ${minutes}m"
            else -> "in less than a minute"
        }
    }

    fun formatLocalTime(time: LocalTime, is24Hour: Boolean = false): String {
        return if (is24Hour) {
            val h = time.hour.toString().padStart(2, '0')
            val m = time.minute.toString().padStart(2, '0')
            "$h:$m"
        } else {
            val period = if (time.hour >= 12) "PM" else "AM"
            val hour12 = when (time.hour % 12) {
                0 -> 12
                else -> time.hour % 12
            }
            val m = time.minute.toString().padStart(2, '0')
            "$hour12:$m $period"
        }
    }

    /**
     * Calculates sleep debt in minutes:
     * (targetMinutesPerDay * days) - actualSleptMinutes
     */
    fun calculateSleepDebt(
        targetMinutesPerDay: Int,
        daysCount: Int,
        totalSleptMinutes: Long
    ): Long {
        val expectedMinutes = targetMinutesPerDay.toLong() * daysCount
        return expectedMinutes - totalSleptMinutes
    }
}
