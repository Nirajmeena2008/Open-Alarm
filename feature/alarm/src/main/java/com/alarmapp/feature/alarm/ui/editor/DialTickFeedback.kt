package com.alarmapp.feature.alarm.ui.editor

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Build
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import com.alarmapp.feature.alarm.R

class DialTickFeedback(
    private val context: Context,
    private val view: View
) {
    private val soundPool: SoundPool = SoundPool.Builder()
        .setMaxStreams(6)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        )
        .build()

    private var soundId: Int = 0
    private var isLoaded: Boolean = false
    private var lastPlayTime: Long = 0L

    init {
        soundPool.setOnLoadCompleteListener { _, sampleId, status ->
            if (status == 0) {
                isLoaded = true
            }
        }
        try {
            soundId = soundPool.load(context, R.raw.dial_tick, 1)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun playTick(pitch: Float = 1.0f) {
        val now = System.currentTimeMillis()
        // Minimum 16ms between ticks (~60 fps) to avoid audio stuttering during very fast flings
        if (now - lastPlayTime >= 14) {
            lastPlayTime = now
            if (isLoaded && soundId != 0) {
                soundPool.play(soundId, 0.4f, 0.4f, 1, 0, pitch)
            }
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    view.performHapticFeedback(HapticFeedbackConstants.SEGMENT_TICK)
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
                } else {
                    view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                }
            } catch (_: Exception) {}
        }
    }

    fun release() {
        soundPool.release()
    }
}

@Composable
fun rememberDialTickFeedback(): DialTickFeedback {
    val context = LocalContext.current
    val view = LocalView.current
    val feedback = remember(context, view) { DialTickFeedback(context, view) }
    DisposableEffect(feedback) {
        onDispose {
            feedback.release()
        }
    }
    return feedback
}
