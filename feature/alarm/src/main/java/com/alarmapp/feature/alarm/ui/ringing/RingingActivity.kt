package com.alarmapp.feature.alarm.ui.ringing

import android.app.KeyguardManager
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alarmapp.feature.alarm.ui.ringing.challenges.*
import com.alarmapp.feature.alarm.ui.theme.AlarmTheme
import com.alarmapp.platform.alarm.AlarmSchedulerImpl
import com.alarmapp.platform.audio.RingtonePlayer
import com.alarmapp.platform.audio.RingtonePlayerImpl
import kotlin.math.sqrt

class RingingActivity : ComponentActivity(), SensorEventListener {

    private lateinit var ringtonePlayer: RingtonePlayer
    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null
    private var lastAcceleration = 0f
    private var currentAcceleration = 0f
    private var shakeThreshold = 12f

    private var onShakeCallback: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupLockScreenFlags()

        ringtonePlayer = RingtonePlayerImpl(applicationContext)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        val alarmId = intent.getStringExtra(AlarmSchedulerImpl.EXTRA_ALARM_ID) ?: ""

        setContent {
            AlarmTheme {
                RingingScreenContent(
                    alarmId = alarmId,
                    onStopAudio = { ringtonePlayer.stop() },
                    onFinishActivity = { finish() },
                    registerShakeListener = { callback ->
                        onShakeCallback = callback
                        sensorManager?.registerListener(
                            this,
                            accelerometer,
                            SensorManager.SENSOR_DELAY_UI
                        )
                    }
                )
            }
        }
    }

    private fun setupLockScreenFlags() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as? KeyguardManager
            keyguardManager?.requestDismissKeyguard(this, null)
        } else {
            @Suppress("DEPRECATION")
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
            )
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        lastAcceleration = currentAcceleration
        currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
        val delta = currentAcceleration - lastAcceleration

        if (delta > shakeThreshold) {
            onShakeCallback?.invoke()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onDestroy() {
        super.onDestroy()
        ringtonePlayer.stop()
        sensorManager?.unregisterListener(this)
    }
}

@Composable
fun RingingScreenContent(
    alarmId: String,
    onStopAudio: () -> Unit,
    onFinishActivity: () -> Unit,
    registerShakeListener: (() -> Unit) -> Unit,
    modifier: Modifier = Modifier
) {
    // In preview or without injection, we can mock or use viewModel
    var isDismissed by remember { mutableStateOf(false) }
    var isSnoozed by remember { mutableStateOf(false) }

    LaunchedEffect(isDismissed, isSnoozed) {
        if (isDismissed || isSnoozed) {
            onStopAudio()
            onFinishActivity()
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 48.dp)
            ) {
                Text(
                    text = "ALARM RINGING",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Wake Up!",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            // Simple dismissal or challenge
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { isDismissed = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = "Dismiss Alarm",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = { isSnoozed = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Snooze (10 mins)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
