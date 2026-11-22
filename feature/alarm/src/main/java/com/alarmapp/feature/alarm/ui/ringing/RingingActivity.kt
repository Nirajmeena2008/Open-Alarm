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
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.alarmapp.data.database.AlarmDatabase
import com.alarmapp.data.repository.AlarmRepositoryImpl
import com.alarmapp.domain.model.Alarm
import com.alarmapp.domain.model.AlarmId
import com.alarmapp.domain.usecase.alarm.DismissAlarmUseCase
import com.alarmapp.domain.usecase.alarm.SnoozeAlarmUseCase
import com.alarmapp.feature.alarm.ui.ringing.challenges.*
import com.alarmapp.feature.alarm.ui.theme.AlarmTheme
import com.alarmapp.platform.alarm.AlarmSchedulerImpl
import com.alarmapp.platform.audio.RingtonePlayer
import com.alarmapp.platform.audio.RingtonePlayerImpl
import com.alarmapp.platform.notification.AlarmNotificationManagerImpl
import kotlinx.coroutines.launch
import kotlin.math.sqrt

class RingingActivity : ComponentActivity(), SensorEventListener {

    private lateinit var ringtonePlayer: RingtonePlayer
    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null
    private var lastAcceleration = 0f
    private var currentAcceleration = 0f
    private var shakeThreshold = 12f
    private var onShakeCallback: (() -> Unit)? = null

    private val viewModel: RingingViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val db = AlarmDatabase.getInstance(applicationContext)
                val alarmRepo = AlarmRepositoryImpl(db.alarmDao())
                val scheduler = AlarmSchedulerImpl(applicationContext, alarmRepo)
                val snoozeUseCase = SnoozeAlarmUseCase(alarmRepo)
                val dismissUseCase = DismissAlarmUseCase(alarmRepo)
                return RingingViewModel(
                    alarmRepository = alarmRepo,
                    snoozeAlarmUseCase = snoozeUseCase,
                    dismissAlarmUseCase = dismissUseCase,
                    alarmScheduler = scheduler
                ) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupLockScreenFlags()

        ringtonePlayer = RingtonePlayerImpl(applicationContext)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        val alarmId = intent.getStringExtra(AlarmSchedulerImpl.EXTRA_ALARM_ID) ?: ""
        viewModel.initAlarm(alarmId)

        // Start ringing immediately
        lifecycleScope.launch {
            val db = AlarmDatabase.getInstance(applicationContext)
            val alarmRepo = AlarmRepositoryImpl(db.alarmDao())
            val alarm = if (alarmId.isNotBlank()) alarmRepo.getAlarm(AlarmId(alarmId)) else null
            if (alarm != null) {
                ringtonePlayer.play(alarm)
            } else {
                val now = kotlinx.datetime.Clock.System.now()
                val fallbackAlarm = Alarm(
                    id = AlarmId("fallback"),
                    label = "Alarm",
                    time = kotlinx.datetime.LocalTime(0, 0),
                    isEnabled = true,
                    volume = 1.0f,
                    createdAt = now,
                    updatedAt = now
                )
                ringtonePlayer.play(fallbackAlarm)
            }
        }

        onShakeCallback = {
            viewModel.onShakeDetected()
        }

        setContent {
            AlarmTheme {
                val uiState by viewModel.uiState.collectAsState()

                LaunchedEffect(uiState.isDismissed, uiState.isSnoozed) {
                    if (uiState.isDismissed || uiState.isSnoozed) {
                        ringtonePlayer.stop()
                        val notificationManager = AlarmNotificationManagerImpl(applicationContext)
                        notificationManager.dismissRingingNotification(alarmId)
                        finish()
                    }
                }

                LaunchedEffect(uiState.challengeState) {
                    if (uiState.challengeState is RingingChallengeState.ShakeChallenge) {
                        sensorManager?.registerListener(
                            this@RingingActivity,
                            accelerometer,
                            SensorManager.SENSOR_DELAY_UI
                        )
                    }
                }

                RingingScreenContent(
                    uiState = uiState,
                    onMathDigit = { viewModel.onMathInput(it) },
                    onMathBackspace = { viewModel.onMathBackspace() },
                    onMathSubmit = { viewModel.onMathSubmit() },
                    onMemoryClick = { viewModel.onMemoryCellClick(it) },
                    onNfcSimulate = {
                        val nfc = uiState.challengeState as? RingingChallengeState.NfcChallenge
                        viewModel.onNfcScanned(nfc?.expectedTagId ?: "")
                    },
                    onDismiss = { viewModel.dismiss() },
                    onSnooze = { viewModel.snooze() }
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
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
            )
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
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
    uiState: RingingUiState,
    onMathDigit: (String) -> Unit,
    onMathBackspace: () -> Unit,
    onMathSubmit: () -> Unit,
    onMemoryClick: (Int) -> Unit,
    onNfcSimulate: () -> Unit,
    onDismiss: () -> Unit,
    onSnooze: () -> Unit,
    modifier: Modifier = Modifier
) {
    val alarm = uiState.alarm
    val timeText = if (alarm != null) {
        val hour = alarm.time.hour
        val minute = alarm.time.minute
        val amPm = if (hour < 12) "AM" else "PM"
        val displayHour = when {
            hour == 0 -> 12
            hour > 12 -> hour - 12
            else -> hour
        }
        String.format("%d:%02d %s", displayHour, minute, amPm)
    } else {
        "Alarm"
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
            // Header
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 36.dp)
            ) {
                Text(
                    text = "ALARM RINGING",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = timeText,
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                if (!alarm?.label.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = alarm!!.label,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Challenge or Centerpiece
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                when (val challenge = uiState.challengeState) {
                    is RingingChallengeState.MathChallenge -> {
                        MathChallengeContent(
                            challenge = challenge,
                            onDigitClick = onMathDigit,
                            onBackspace = onMathBackspace,
                            onSubmit = onMathSubmit
                        )
                    }
                    is RingingChallengeState.MemoryChallenge -> {
                        MemoryChallengeContent(
                            challenge = challenge,
                            onCellClick = onMemoryClick
                        )
                    }
                    is RingingChallengeState.ShakeChallenge -> {
                        ShakeChallengeContent(challenge = challenge)
                    }
                    is RingingChallengeState.NfcChallenge -> {
                        NfcChallengeContent(
                            challenge = challenge,
                            onSimulateScan = onNfcSimulate
                        )
                    }
                    else -> {
                        Text(
                            text = "Wake Up!",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Action Buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val hasActiveChallenge = uiState.challengeState !is RingingChallengeState.None

                if (!hasActiveChallenge) {
                    Button(
                        onClick = onDismiss,
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
                }

                if (alarm == null || alarm.currentSnoozeCount < alarm.maxSnoozeCount) {
                    OutlinedButton(
                        onClick = onSnooze,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "Snooze (${alarm?.snoozeDurationMinutes ?: 10} mins)",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
