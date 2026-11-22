package com.alarmapp.domain.util

import com.alarmapp.domain.model.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toKotlinInstant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

object NextOccurrenceCalculator {

    /**
     * Calculates the next occurrence [Instant] for an alarm with the specified target time
     * and repeat days.
     *
     * @param targetTime The time of day the alarm should ring.
     * @param daysOfWeek The repeat days (empty means a one-shot alarm).
     * @param fromInstant The reference instant (usually "now").
     * @param zoneId The user's timezone.
     */
    fun calculateNextOccurrence(
        targetTime: LocalTime,
        daysOfWeek: Set<DayOfWeek>,
        fromInstant: Instant,
        zoneId: ZoneId = ZoneId.systemDefault()
    ): Instant {
        val javaFromInstant = fromInstant.toJavaInstant()
        val nowZoned = ZonedDateTime.ofInstant(javaFromInstant, zoneId)
        val today = nowZoned.toLocalDate()

        val javaLocalTime = java.time.LocalTime.of(
            targetTime.hour,
            targetTime.minute,
            targetTime.second,
            targetTime.nanosecond
        )

        // 1. One-shot alarm
        if (daysOfWeek.isEmpty()) {
            val todayOccurrence = ZonedDateTime.of(today, javaLocalTime, zoneId)
            return if (todayOccurrence.toInstant().isAfter(javaFromInstant)) {
                todayOccurrence.toInstant().toKotlinInstant()
            } else {
                val tomorrowOccurrence = ZonedDateTime.of(today.plusDays(1), javaLocalTime, zoneId)
                tomorrowOccurrence.toInstant().toKotlinInstant()
            }
        }

        // 2. Repeating alarm
        val targetIsoDays = daysOfWeek.map { it.isoValue }.toSet()

        // Check up to 8 consecutive days to find the earliest valid occurrence strictly after javaFromInstant
        var candidateDate: LocalDate = today
        for (i in 0..7) {
            val currentIsoDay = candidateDate.dayOfWeek.value
            if (currentIsoDay in targetIsoDays) {
                val candidateZoned = ZonedDateTime.of(candidateDate, javaLocalTime, zoneId)
                val candidateInstant = candidateZoned.toInstant()
                if (candidateInstant.isAfter(javaFromInstant)) {
                    return candidateInstant.toKotlinInstant()
                }
            }
            candidateDate = candidateDate.plusDays(1)
        }

        // Fallback (next week same day)
        val fallbackZoned = ZonedDateTime.of(today.plusDays(7), javaLocalTime, zoneId)
        return fallbackZoned.toInstant().toKotlinInstant()
    }
}
