package com.alarmapp.data.converter

import com.alarmapp.domain.model.ChallengeConfig
import com.alarmapp.domain.model.DayOfWeek
import com.alarmapp.domain.model.DismissType
import com.alarmapp.domain.model.MathDifficulty
import com.alarmapp.domain.model.RingtoneSource
import com.alarmapp.domain.model.VibrationPattern
import com.alarmapp.domain.model.VolumeRamp
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalTime
import org.junit.jupiter.api.Test

class ConvertersTest {

    private val converters = Converters()

    @Test
    fun `Instant conversion preserves epoch millis`() {
        val instant = Instant.parse("2026-11-22T14:30:00Z")
        val millis = converters.fromInstant(instant)
        val restored = converters.toInstant(millis)

        assertThat(restored).isEqualTo(instant)
    }

    @Test
    fun `LocalTime conversion preserves hour and minute`() {
        val time = LocalTime(7, 45, 30)
        val string = converters.fromLocalTime(time)
        val restored = converters.toLocalTime(string)

        assertThat(restored).isEqualTo(time)
    }

    @Test
    fun `DayOfWeek set conversion preserves all elements`() {
        val days = setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)
        val encoded = converters.fromDayOfWeekSet(days)
        val restored = converters.toDayOfWeekSet(encoded)

        assertThat(restored).containsExactlyElementsIn(days)
    }

    @Test
    fun `RingtoneSource conversion handles Default and System`() {
        val defaultRingtone = RingtoneSource.Default
        val defaultRestored = converters.toRingtoneSource(converters.fromRingtoneSource(defaultRingtone))
        assertThat(defaultRestored).isEqualTo(defaultRingtone)

        val systemRingtone = RingtoneSource.System("Chimes", "content://media/audio/123")
        val systemRestored = converters.toRingtoneSource(converters.fromRingtoneSource(systemRingtone))
        assertThat(systemRestored).isEqualTo(systemRingtone)
    }

    @Test
    fun `VolumeRamp conversion preserves duration and volume levels`() {
        val ramp = VolumeRamp(durationSeconds = 90, startVolume = 0.2f, endVolume = 0.9f)
        val encoded = converters.fromVolumeRamp(ramp)
        val restored = converters.toVolumeRamp(encoded)

        assertThat(restored).isEqualTo(ramp)
    }

    @Test
    fun `ChallengeConfig conversion handles Math and Nfc`() {
        val mathConfig = ChallengeConfig.Math(difficulty = MathDifficulty.HARD, problemCount = 5)
        val mathRestored = converters.toChallengeConfig(converters.fromChallengeConfig(mathConfig))
        assertThat(mathRestored).isEqualTo(mathConfig)

        val nfcConfig = ChallengeConfig.Nfc(registeredTagId = "TAG-001-A", tagName = "Kitchen")
        val nfcRestored = converters.toChallengeConfig(converters.fromChallengeConfig(nfcConfig))
        assertThat(nfcRestored).isEqualTo(nfcConfig)
    }

    @Test
    fun `Enum converters fallback safely to defaults on invalid input`() {
        assertThat(converters.toDismissType("INVALID")).isEqualTo(DismissType.STANDARD)
        assertThat(converters.toVibrationPattern("UNKNOWN")).isEqualTo(VibrationPattern.STANDARD)
    }
}
