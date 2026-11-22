package com.alarmapp.platform.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import com.alarmapp.domain.model.Alarm
import com.alarmapp.domain.model.RingtoneSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

interface RingtonePlayer {
    fun play(alarm: Alarm)
    fun stop()
}

class RingtonePlayerImpl(private val context: Context) : RingtonePlayer {
    private var mediaPlayer: MediaPlayer? = null
    private var rampJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    override fun play(alarm: Alarm) {
        stop()

        if (alarm.ringtone is RingtoneSource.Silent) return

        val uri = when (val source = alarm.ringtone) {
            is RingtoneSource.Default -> RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            is RingtoneSource.System -> Uri.parse(source.uri)
            is RingtoneSource.File -> Uri.parse(source.path)
            is RingtoneSource.Silent -> return
        }

        val player = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            setDataSource(context, uri)
            isLooping = true
            prepare()
        }
        mediaPlayer = player

        val ramp = alarm.volumeRamp
        if (ramp != null && ramp.durationSeconds > 0) {
            player.setVolume(ramp.startVolume, ramp.startVolume)
            player.start()

            rampJob = scope.launch {
                val totalSteps = 20
                val stepDelayMs = (ramp.durationSeconds * 1000L) / totalSteps
                val volumeStep = (ramp.endVolume - ramp.startVolume) / totalSteps

                var currentVol = ramp.startVolume
                for (i in 1..totalSteps) {
                    delay(stepDelayMs)
                    if (!isActive) break
                    currentVol += volumeStep
                    val clamped = currentVol.coerceIn(0f, 1f)
                    mediaPlayer?.setVolume(clamped, clamped)
                }
            }
        } else {
            val volume = alarm.volume.coerceIn(0f, 1f)
            player.setVolume(volume, volume)
            player.start()
        }
    }

    override fun stop() {
        rampJob?.cancel()
        rampJob = null
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
        }
        mediaPlayer = null
    }
}
