package com.alarmapp.feature.alarm.ui.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alarmapp.domain.model.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmEditorScreen(
    state: AlarmEditorState,
    onTimeChange: (Int, Int) -> Unit,
    onToggleDay: (DayOfWeek) -> Unit,
    onLabelChange: (String) -> Unit,
    onChallengeChange: (ChallengeConfig?) -> Unit,
    onVolumeRampChange: (VolumeRamp?) -> Unit,
    onVibrationChange: (VibrationPattern) -> Unit,
    onSnoozeDurationChange: (Int) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showTimePicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (state.alarmId == null) "New Alarm" else "Edit Alarm",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onSave, enabled = !state.isSaving) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Save",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Big Time Display (tap to edit)
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showTimePicker = true }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val hour12 = when {
                        state.time.hour == 0 -> 12
                        state.time.hour > 12 -> state.time.hour - 12
                        else -> state.time.hour
                    }
                    val amPm = if (state.time.hour >= 12) "PM" else "AM"

                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "%d:%02d".format(hour12, state.time.minute),
                            fontSize = 64.sp,
                            fontWeight = FontWeight.Light,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = amPm,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                    }
                    Text(
                        text = "Tap to change time",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Day Selector
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Repeat Days",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    DayOfWeekSelector(
                        selectedDays = state.daysOfWeek,
                        onToggleDay = onToggleDay
                    )
                }
            }

            // Label
            OutlinedTextField(
                value = state.label,
                onValueChange = onLabelChange,
                label = { Text("Alarm Label") },
                placeholder = { Text("e.g. Work, Gym, Medicine") },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )

            // Wake-up Challenge
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Dismiss Challenge",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Require solving a task before the alarm can be turned off.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    val challengeOptions: List<Pair<String, ChallengeConfig?>> = listOf(
                        "None" to null,
                        "Math (Medium, 3 problems)" to ChallengeConfig.Math(MathDifficulty.MEDIUM, problemCount = 3),
                        "Memory (3x3 grid, 5 steps)" to ChallengeConfig.Memory(gridDimension = 3, sequenceLength = 5),
                        "Shake (30 shakes)" to ChallengeConfig.Shake(targetShakes = 30, sensitivity = 12.0f),
                        "NFC Tag" to ChallengeConfig.Nfc(registeredTagId = "default-nfc"),
                        "QR Code" to ChallengeConfig.Qr(expectedPayload = "default-qr")
                    )

                    challengeOptions.forEach { (title, config) ->
                        val isSelected = when {
                            state.challengeConfig == null && config == null -> true
                            state.challengeConfig is ChallengeConfig.Math && config is ChallengeConfig.Math -> true
                            state.challengeConfig is ChallengeConfig.Memory && config is ChallengeConfig.Memory -> true
                            state.challengeConfig is ChallengeConfig.Shake && config is ChallengeConfig.Shake -> true
                            state.challengeConfig is ChallengeConfig.Nfc && config is ChallengeConfig.Nfc -> true
                            state.challengeConfig is ChallengeConfig.Qr && config is ChallengeConfig.Qr -> true
                            else -> false
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onChallengeChange(config) }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { onChallengeChange(config) }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = title, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }

            // Volume Ramp
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Gradual Volume Ramp",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    val ramps: List<Pair<VolumeRamp?, String>> = listOf(
                        null to "Instant (Full volume)",
                        VolumeRamp(durationSeconds = 30) to "30 Seconds",
                        VolumeRamp(durationSeconds = 60) to "60 Seconds",
                        VolumeRamp(durationSeconds = 120) to "2 Minutes",
                        VolumeRamp(durationSeconds = 300) to "5 Minutes"
                    )

                    ramps.forEach { (ramp, label) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onVolumeRampChange(ramp) }
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = state.volumeRamp?.durationSeconds == ramp?.durationSeconds,
                                onClick = { onVolumeRampChange(ramp) }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = label, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }

            // Vibration Pattern
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Vibration Pattern",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    val patterns = listOf(
                        VibrationPattern.STANDARD to "Standard",
                        VibrationPattern.HEARTBEAT to "Heartbeat",
                        VibrationPattern.ESCALATING to "Escalating",
                        VibrationPattern.STACCATO to "Staccato",
                        VibrationPattern.NONE to "None"
                    )

                    patterns.forEach { (pat, label) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onVibrationChange(pat) }
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = state.vibrationPattern == pat,
                                onClick = { onVibrationChange(pat) }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = label, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }

            // Save Button
            Button(
                onClick = onSave,
                enabled = !state.isSaving,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
            ) {
                Text(
                    text = if (state.isSaving) "Saving..." else "Save Alarm",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    if (showTimePicker) {
        val pickerState = rememberTimePickerState(
            initialHour = state.time.hour,
            initialMinute = state.time.minute,
            is24Hour = false
        )
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    onTimeChange(pickerState.hour, pickerState.minute)
                    showTimePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("Cancel")
                }
            },
            text = {
                TimePicker(state = pickerState)
            }
        )
    }
}

@Composable
private fun DayOfWeekSelector(
    selectedDays: Set<DayOfWeek>,
    onToggleDay: (DayOfWeek) -> Unit,
    modifier: Modifier = Modifier
) {
    val days = listOf(
        DayOfWeek.MONDAY to "Mon",
        DayOfWeek.TUESDAY to "Tue",
        DayOfWeek.WEDNESDAY to "Wed",
        DayOfWeek.THURSDAY to "Thu",
        DayOfWeek.FRIDAY to "Fri",
        DayOfWeek.SATURDAY to "Sat",
        DayOfWeek.SUNDAY to "Sun"
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        days.forEach { (day, label) ->
            val isSelected = selectedDays.contains(day)
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceVariant
                    )
                    .clickable { onToggleDay(day) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label.first().toString(),
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
