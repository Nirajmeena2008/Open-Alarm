package com.alarmapp.domain.util

import com.alarmapp.domain.model.DayOfWeek
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toKotlinInstant
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

class NextOccurrenceCalculatorTest {

    private val zoneUtc = ZoneId.of("UTC")
    private val zoneNewYork = ZoneId.of("America/New_York")

    @Test
    fun `one-shot alarm in future today schedules for today`() {
        // Monday 2026-11-23 at 08:00 UTC
        val fromInstant = ZonedDateTime.of(2026, 11, 23, 8, 0, 0, 0, zoneUtc).toInstant().toKotlinInstant()
        val targetTime = LocalTime(9, 30) // 09:30 UTC

        val next = NextOccurrenceCalculator.calculateNextOccurrence(
            targetTime = targetTime,
            daysOfWeek = emptySet(),
            fromInstant = fromInstant,
            zoneId = zoneUtc
        )

        val expected = ZonedDateTime.of(2026, 11, 23, 9, 30, 0, 0, zoneUtc).toInstant().toKotlinInstant()
        assertThat(next).isEqualTo(expected)
    }

    @Test
    fun `one-shot alarm in past today rolls over to tomorrow`() {
        // Monday 2026-11-23 at 10:00 UTC
        val fromInstant = ZonedDateTime.of(2026, 11, 23, 10, 0, 0, 0, zoneUtc).toInstant().toKotlinInstant()
        val targetTime = LocalTime(7, 0) // 07:00 UTC (already passed)

        val next = NextOccurrenceCalculator.calculateNextOccurrence(
            targetTime = targetTime,
            daysOfWeek = emptySet(),
            fromInstant = fromInstant,
            zoneId = zoneUtc
        )

        val expected = ZonedDateTime.of(2026, 11, 24, 7, 0, 0, 0, zoneUtc).toInstant().toKotlinInstant()
        assertThat(next).isEqualTo(expected)
    }

    @Test
    fun `repeating alarm on weekdays skips weekend`() {
        // Friday 2026-11-27 at 10:00 UTC
        val friday = ZonedDateTime.of(2026, 11, 27, 10, 0, 0, 0, zoneUtc).toInstant().toKotlinInstant()
        val targetTime = LocalTime(8, 0) // Passed for Friday, repeats Mon-Fri

        val next = NextOccurrenceCalculator.calculateNextOccurrence(
            targetTime = targetTime,
            daysOfWeek = DayOfWeek.WEEKDAYS,
            fromInstant = friday,
            zoneId = zoneUtc
        )

        // Should jump over Saturday and Sunday to Monday 2026-11-30 at 08:00 UTC
        val expected = ZonedDateTime.of(2026, 11, 30, 8, 0, 0, 0, zoneUtc).toInstant().toKotlinInstant()
        assertThat(next).isEqualTo(expected)
    }

    @Test
    fun `repeating alarm today in future triggers today`() {
        // Monday 2026-11-23 at 06:00 UTC
        val mondayMorning = ZonedDateTime.of(2026, 11, 23, 6, 0, 0, 0, zoneUtc).toInstant().toKotlinInstant()
        val targetTime = LocalTime(7, 0)

        val next = NextOccurrenceCalculator.calculateNextOccurrence(
            targetTime = targetTime,
            daysOfWeek = DayOfWeek.WEEKDAYS,
            fromInstant = mondayMorning,
            zoneId = zoneUtc
        )

        val expected = ZonedDateTime.of(2026, 11, 23, 7, 0, 0, 0, zoneUtc).toInstant().toKotlinInstant()
        assertThat(next).isEqualTo(expected)
    }

    @Test
    fun `DST spring forward handles clock gap without crashing or missing`() {
        // In America/New_York, 2026-03-08 clocks spring forward from 02:00 to 03:00 (02:30 does not exist locally)
        // From Saturday night 2026-03-07 23:00
        val saturdayNight = ZonedDateTime.of(2026, 3, 7, 23, 0, 0, 0, zoneNewYork).toInstant().toKotlinInstant()
        val targetTime = LocalTime(2, 30) // In the gap

        val next = NextOccurrenceCalculator.calculateNextOccurrence(
            targetTime = targetTime,
            daysOfWeek = setOf(DayOfWeek.SUNDAY),
            fromInstant = saturdayNight,
            zoneId = zoneNewYork
        )

        // ZonedDateTime adjusts gap time to valid time (03:30 or 03:00)
        assertThat(next.toEpochMilliseconds()).isGreaterThan(saturdayNight.toEpochMilliseconds())
    }

    @Test
    fun `DST fall back handles repeat hour smoothly`() {
        // In America/New_York, 2026-11-01 clocks fall back from 02:00 to 01:00 (01:30 occurs twice)
        val saturdayEve = ZonedDateTime.of(2026, 10, 31, 20, 0, 0, 0, zoneNewYork).toInstant().toKotlinInstant()
        val targetTime = LocalTime(1, 30)

        val next = NextOccurrenceCalculator.calculateNextOccurrence(
            targetTime = targetTime,
            daysOfWeek = setOf(DayOfWeek.SUNDAY),
            fromInstant = saturdayEve,
            zoneId = zoneNewYork
        )

        assertThat(next.toEpochMilliseconds()).isGreaterThan(saturdayEve.toEpochMilliseconds())
    }
}
