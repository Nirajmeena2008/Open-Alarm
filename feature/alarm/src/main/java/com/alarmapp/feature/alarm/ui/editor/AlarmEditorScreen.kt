package com.alarmapp.feature.alarm.ui.editor

import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alarmapp.domain.model.*
import com.alarmapp.feature.alarm.ui.theme.*
import java.io.File
import java.io.FileOutputStream
import kotlin.math.cos
import kotlin.math.sin

enum class EditorScreenMode {
    CHOOSE_TIME,
    CHOOSE_GAME
}

@Composable
fun AlarmEditorScreen(
    state: AlarmEditorState,
    onTimeChange: (Int, Int) -> Unit,
    onToggleDay: (DayOfWeek) -> Unit,
    onLabelChange: (String) -> Unit,
    onRingtoneChange: (RingtoneSource) -> Unit = {},
    onChallengeChange: (ChallengeConfig?) -> Unit,
    onVolumeRampChange: (VolumeRamp?) -> Unit,
    onVibrationChange: (VibrationPattern) -> Unit,
    onSnoozeDurationChange: (Int) -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit = {},
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var screenMode by remember { mutableStateOf(EditorScreenMode.CHOOSE_TIME) }
    var showSoundDialog by remember { mutableStateOf(false) }
    var showRepeatDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showGradualDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val audioPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val contentResolver = context.contentResolver
            var displayName = "Custom Tone"
            contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (nameIndex != -1) {
                        val name = cursor.getString(nameIndex)
                        if (!name.isNullOrBlank()) {
                            displayName = name
                        }
                    }
                }
            }

            try {
                val ringtonesDir = File(context.filesDir, "custom_ringtones").apply { mkdirs() }
                val safeFileName = displayName.replace("[^a-zA-Z0-9._-]".toRegex(), "_")
                val destFile = File(ringtonesDir, safeFileName)
                contentResolver.openInputStream(uri)?.use { input ->
                    FileOutputStream(destFile).use { output ->
                        input.copyTo(output)
                    }
                }
                onRingtoneChange(RingtoneSource.File(title = displayName, path = destFile.absolutePath))
            } catch (_: Exception) {
                onRingtoneChange(RingtoneSource.File(title = displayName, path = uri.toString()))
            }
        }
    }

    val isLight = MaterialTheme.colorScheme.background == MinimalBackground
    val bottomBtnBg = if (isLight) MinimalBlack else MaterialTheme.colorScheme.surfaceVariant
    val bottomBtnFg = if (isLight) MinimalWhite else MaterialTheme.colorScheme.onSurface

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            // Minimalist Bottom Bar matching UI mockup
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left circular button (Cancel)
                    Surface(
                        onClick = onBack,
                        shape = CircleShape,
                        color = if (isLight) Color(0xFFD4D4D8) else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.size(54.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Cancel",
                                tint = if (isLight) MinimalBlack else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    // Center mode text / navigation button
                    Surface(
                        onClick = {
                            screenMode = if (screenMode == EditorScreenMode.CHOOSE_TIME) {
                                EditorScreenMode.CHOOSE_GAME
                            } else {
                                EditorScreenMode.CHOOSE_TIME
                            }
                        },
                        shape = RoundedCornerShape(20.dp),
                        color = Color.Transparent,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    ) {
                        Text(
                            text = if (screenMode == EditorScreenMode.CHOOSE_TIME) "Choose time" else "Choose the Game",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    // Right circular button (Confirm & Save)
                    Surface(
                        onClick = onSave,
                        shape = CircleShape,
                        color = bottomBtnBg,
                        shadowElevation = 8.dp,
                        modifier = Modifier.size(54.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Save",
                                tint = bottomBtnFg,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    }
                }
            }
        },
        modifier = modifier
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            AnimatedContent(
                targetState = screenMode,
                transitionSpec = {
                    if (targetState == EditorScreenMode.CHOOSE_GAME) {
                        slideInHorizontally { it } + fadeIn() togetherWith slideOutHorizontally { -it } + fadeOut()
                    } else {
                        slideInHorizontally { -it } + fadeIn() togetherWith slideOutHorizontally { it } + fadeOut()
                    }
                },
                label = "EditorScreenMode"
            ) { mode ->
                when (mode) {
                    EditorScreenMode.CHOOSE_TIME -> {
                        ChooseTimeScreen(
                            hour = state.time.hour,
                            minute = state.time.minute,
                            onHourChange = { newHour -> onTimeChange(newHour, state.time.minute) },
                            onMinuteChange = { newMinute -> onTimeChange(state.time.hour, newMinute) },
                            ringtone = state.ringtone,
                            snoozeMinutes = state.snoozeDurationMinutes,
                            daysOfWeek = state.daysOfWeek,
                            challengeConfig = state.challengeConfig,
                            volumeRamp = state.volumeRamp,
                            onOpenSound = { showSoundDialog = true },
                            onOpenGradual = { showGradualDialog = true },
                            onCycleSnooze = {
                                val next = when (state.snoozeDurationMinutes) {
                                    5 -> 10
                                    10 -> 15
                                    15 -> 5
                                    else -> 10
                                }
                                onSnoozeDurationChange(next)
                            },
                            onOpenRepeat = { showRepeatDialog = true },
                            onSwitchToGame = { screenMode = EditorScreenMode.CHOOSE_GAME },
                            isEditingExisting = state.alarmId != null,
                            onDeleteClick = { showDeleteDialog = true }
                        )
                    }
                    EditorScreenMode.CHOOSE_GAME -> {
                        ChooseGameScreen(
                            activeChallenge = state.challengeConfig,
                            alarmTime = state.time,
                            onSelectChallenge = onChallengeChange,
                            onSwitchToTime = { screenMode = EditorScreenMode.CHOOSE_TIME }
                        )
                    }
                }
            }
        }
    }

    // Sound Selection Dialog
    if (showSoundDialog) {
        AlertDialog(
            onDismissRequest = { showSoundDialog = false },
            title = { Text("Alarm Tone", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onRingtoneChange(RingtoneSource.Default)
                                showSoundDialog = false
                            }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = state.ringtone is RingtoneSource.Default,
                            onClick = null
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Default System Alarm", fontWeight = FontWeight.Medium)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showSoundDialog = false
                                audioPickerLauncher.launch("audio/*")
                            }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = state.ringtone is RingtoneSource.File,
                            onClick = null
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Custom Tone from Device", fontWeight = FontWeight.Medium)
                            if (state.ringtone is RingtoneSource.File) {
                                Text(
                                    text = (state.ringtone as RingtoneSource.File).title,
                                    fontSize = 12.sp,
                                    color = MinimalTextMuted
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onRingtoneChange(RingtoneSource.Silent)
                                showSoundDialog = false
                            }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = state.ringtone is RingtoneSource.Silent,
                            onClick = null
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Silent (Vibrate only)", fontWeight = FontWeight.Medium)
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                    // Gradual Volume toggle in Sound dialog
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showSoundDialog = false
                                showGradualDialog = true
                            }
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Gradual Volume Increase", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                            Text(
                                text = if (state.volumeRamp != null) "Increases to 100% over ${state.volumeRamp?.durationSeconds}s" else "Disabled (Immediate full volume)",
                                fontSize = 12.sp,
                                color = MinimalTextMuted
                            )
                        }
                        Switch(
                            checked = state.volumeRamp != null,
                            onCheckedChange = { enable ->
                                onVolumeRampChange(if (enable) VolumeRamp(durationSeconds = 60) else null)
                            }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSoundDialog = false }) {
                    Text("Done", fontWeight = FontWeight.Bold, color = MinimalBlack)
                }
            }
        )
    }

    // Repeat Days Dialog
    if (showRepeatDialog) {
        AlertDialog(
            onDismissRequest = { showRepeatDialog = false },
            title = { Text("Repeat Days", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    DayOfWeek.entries.forEach { day ->
                        val isChecked = day in state.daysOfWeek
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onToggleDay(day) }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = { onToggleDay(day) }
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(day.name.lowercase().replaceFirstChar { it.uppercase() })
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showRepeatDialog = false }) {
                    Text("OK", fontWeight = FontWeight.Bold, color = MinimalBlack)
                }
            }
        )
    }

    // Delete Confirmation Dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Alarm", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to delete this alarm?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDelete()
                    }
                ) {
                    Text("Delete", color = MinimalRedAccent, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Gradual Volume Increase Dialog
    if (showGradualDialog) {
        val durations = listOf(
            0 to "Off (Immediate full volume)",
            15 to "15 seconds (Quick fade-in)",
            30 to "30 seconds (Gentle)",
            60 to "60 seconds (Standard)",
            120 to "2 minutes (Slow fade-in)"
        )
        AlertDialog(
            onDismissRequest = { showGradualDialog = false },
            title = { Text("Gradual Volume Increase", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Alarm sound starts softly at 10% volume and gradually increases to 100% full volume.",
                        fontSize = 13.sp,
                        color = MinimalTextMuted
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    durations.forEach { (sec, label) ->
                        val isSelected = if (sec == 0) state.volumeRamp == null else state.volumeRamp?.durationSeconds == sec
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (sec == 0) {
                                        onVolumeRampChange(null)
                                    } else {
                                        onVolumeRampChange(VolumeRamp(durationSeconds = sec))
                                    }
                                    showGradualDialog = false
                                }
                                .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = isSelected, onClick = null)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(label, fontSize = 14.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showGradualDialog = false }) {
                    Text("Done", fontWeight = FontWeight.Bold, color = MinimalBlack)
                }
            }
        )
    }
}

// ---------------------------------------------------------
// SCREEN 2: CHOOSE TIME (Dual Hourglass Convex Wheels)
// ---------------------------------------------------------

@Composable
fun ChooseTimeScreen(
    hour: Int,
    minute: Int,
    onHourChange: (Int) -> Unit,
    onMinuteChange: (Int) -> Unit,
    ringtone: RingtoneSource,
    snoozeMinutes: Int,
    daysOfWeek: Set<DayOfWeek>,
    challengeConfig: ChallengeConfig?,
    volumeRamp: VolumeRamp? = VolumeRamp(),
    onOpenSound: () -> Unit,
    onOpenGradual: () -> Unit = {},
    onCycleSnooze: () -> Unit,
    onOpenRepeat: () -> Unit,
    onSwitchToGame: () -> Unit,
    isEditingExisting: Boolean = false,
    onDeleteClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Dual Convex Wheels with Degree-by-degree "toci" sound and smooth animation
        CurvedDualWheelTimePicker(
            hour = hour,
            minute = minute,
            onHourChange = onHourChange,
            onMinuteChange = onMinuteChange,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        // Lower Controls Section (Quick Settings + Challenge + Delete)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Three Quick Setting Action Items matching UI mockup: Sound, Snooze, Repeat
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Sound
                val soundText = when (ringtone) {
                    is RingtoneSource.File -> ringtone.title.take(10)
                    is RingtoneSource.System -> ringtone.title.take(10)
                    is RingtoneSource.Default -> "Wake up"
                    is RingtoneSource.Silent -> "Silent"
                }
                QuickSettingItem(
                    icon = Icons.Default.MusicNote,
                    title = "Sound",
                    subtitle = soundText,
                    onClick = onOpenSound,
                    modifier = Modifier.weight(1f)
                )

                // Snooze
                QuickSettingItem(
                    icon = Icons.Default.NotificationsNone,
                    title = "Snooze",
                    subtitle = "Every $snoozeMinutes min",
                    onClick = onCycleSnooze,
                    modifier = Modifier.weight(1f)
                )

                // Repeat
                val repeatText = if (daysOfWeek.isEmpty()) "No" else "${daysOfWeek.size} days"
                QuickSettingItem(
                    icon = Icons.Default.Repeat,
                    title = "Repeat",
                    subtitle = repeatText,
                    onClick = onOpenRepeat,
                    modifier = Modifier.weight(1f)
                )
            }

            // Challenge Shortcut Button
            Surface(
                onClick = onSwitchToGame,
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Extension,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Challenge: ${challengeConfig?.let { it::class.simpleName } ?: "None"}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            if (isEditingExisting) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    onClick = onDeleteClick,
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFFDE8E8),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Delete Alarm",
                            tint = MinimalRedAccent,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Delete Alarm",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MinimalRedAccent
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}



@Composable
fun QuickSettingItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = subtitle,
            fontSize = 11.sp,
            color = MinimalTextMuted
        )
    }
}

// ---------------------------------------------------------
// SCREEN 3: CHOOSE THE GAME (Challenges Carousel & Dial)
// ---------------------------------------------------------

@Composable
fun ChooseGameScreen(
    activeChallenge: ChallengeConfig?,
    alarmTime: kotlinx.datetime.LocalTime,
    onSelectChallenge: (ChallengeConfig?) -> Unit,
    onSwitchToTime: () -> Unit,
    modifier: Modifier = Modifier
) {
    val challengeTypes = listOf(
        "Memory" to ChallengeConfig.Memory(gridDimension = 3, sequenceLength = 5),
        "Math" to ChallengeConfig.Math(difficulty = MathDifficulty.MEDIUM, problemCount = 3),
        "Shake" to ChallengeConfig.Shake(targetShakes = 30, sensitivity = 12f),
        "NFC Tag" to ChallengeConfig.Nfc(registeredTagId = "ALARM_NFC"),
        "Standard" to null
    )

    var selectedIndex by remember {
        val idx = challengeTypes.indexOfFirst {
            when (it.second) {
                null -> activeChallenge == null
                is ChallengeConfig.Memory -> activeChallenge is ChallengeConfig.Memory
                is ChallengeConfig.Math -> activeChallenge is ChallengeConfig.Math
                is ChallengeConfig.Shake -> activeChallenge is ChallengeConfig.Shake
                is ChallengeConfig.Nfc -> activeChallenge is ChallengeConfig.Nfc
                else -> false
            }
        }
        mutableIntStateOf(if (idx != -1) idx else 0)
    }

    val isLight = MaterialTheme.colorScheme.background == MinimalBackground

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Header: "Choose" (bold black) "the Game" (bold light grey)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Choose",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "the Game",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isLight) MinimalTextLight else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            TextButton(onClick = onSwitchToTime) {
                Text("Time", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Horizontal Game Card Carousel
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            itemsIndexed(challengeTypes) { index, pair ->
                val (title, config) = pair
                val isSelected = index == selectedIndex

                Surface(
                    onClick = {
                        selectedIndex = index
                        onSelectChallenge(config)
                    },
                    shape = RoundedCornerShape(24.dp),
                    color = if (isSelected) (if (isLight) MinimalWhite else MaterialTheme.colorScheme.surface) else (if (isLight) Color(0xFFF1F1F4) else MaterialTheme.colorScheme.surfaceVariant),
                    shadowElevation = if (isSelected) 6.dp else 1.dp,
                    modifier = Modifier
                        .width(260.dp)
                        .height(240.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(18.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        // Visual Game Representation Card
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            when (config) {
                                is ChallengeConfig.Memory, null -> {
                                    // 12x12 Dot Matrix Pattern from Mockup
                                    DotMatrixPatternPreview()
                                }
                                is ChallengeConfig.Math -> {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("24 × 3 + 15", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text("Solve to dismiss", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                                is ChallengeConfig.Shake -> {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(
                                            imageVector = Icons.Default.Vibration,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.size(48.dp)
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text("30 Shakes", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                    }
                                }
                                is ChallengeConfig.Nfc -> {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(
                                            imageVector = Icons.Default.Nfc,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.size(48.dp)
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text("Scan Registered Tag", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                                    }
                                }
                                else -> {}
                            }
                        }

                        // Selected indicator badge
                        Text(
                            text = if (isSelected) "Active Challenge" else "Tap to Select",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Circular Dial Canvas with Centered Digital Time
        val onSurfaceColor = MaterialTheme.colorScheme.onSurface
        val dialTickSecondary = if (isLight) Color(0xFFC4C4CA) else Color(0xFF44444E)
        val innerCircleColor = if (isLight) Color(0xFFE0E0E6) else Color(0xFF2E2E36)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(200.dp)) {
                val radius = size.minDimension / 2f
                val center = Offset(size.width / 2f, size.height / 2f)

                // Outer tick circle
                val totalTicks = 60
                for (i in 0 until totalTicks) {
                    val angleDeg = i * (360f / totalTicks) - 90f
                    val angleRad = Math.toRadians(angleDeg.toDouble())
                    val isHour = i % 5 == 0
                    val tickLen = if (isHour) 12f else 6f

                    val startX = center.x + (radius - tickLen) * cos(angleRad).toFloat()
                    val startY = center.y + (radius - tickLen) * sin(angleRad).toFloat()
                    val endX = center.x + radius * cos(angleRad).toFloat()
                    val endY = center.y + radius * sin(angleRad).toFloat()

                    drawLine(
                        color = if (isHour) onSurfaceColor else dialTickSecondary,
                        start = Offset(startX, startY),
                        end = Offset(endX, endY),
                        strokeWidth = if (isHour) 2.5f else 1.2f
                    )
                }

                // Inner circle outline
                drawCircle(
                    color = innerCircleColor,
                    radius = radius - 16f,
                    style = Stroke(width = 1.5f)
                )
            }

            // Center digital clock inside the dial (e.g. "10:30")
            val hour12 = when {
                alarmTime.hour == 0 -> 12
                alarmTime.hour > 12 -> alarmTime.hour - 12
                else -> alarmTime.hour
            }
            Text(
                text = "%d:%02d".format(hour12, alarmTime.minute),
                fontSize = 36.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

// 12x12 Dot Matrix Preview matching the mockup
@Composable
fun DotMatrixPatternPreview() {
    val isLight = MaterialTheme.colorScheme.background == MinimalBackground
    val dotColor = MaterialTheme.colorScheme.onSurface
    Canvas(
        modifier = Modifier
            .size(150.dp)
            .padding(4.dp)
    ) {
        val rows = 12
        val cols = 12
        val stepX = size.width / (cols - 1)
        val stepY = size.height / (rows - 1)

        for (r in 0 until rows) {
            for (c in 0 until cols) {
                val cx = c * stepX
                val cy = r * stepY
                // Highlighting center pattern
                val isPattern = (r in 4..7 && c in 4..7)
                drawCircle(
                    color = if (isPattern) dotColor else (if (isLight) Color(0xFFC0C0C8) else Color(0xFF44444E)),
                    radius = if (isPattern) 3.5f else 2.2f,
                    center = Offset(cx, cy)
                )
            }
        }
    }
}
