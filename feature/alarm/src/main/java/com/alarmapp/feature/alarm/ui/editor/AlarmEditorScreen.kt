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
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var screenMode by remember { mutableStateOf(EditorScreenMode.CHOOSE_TIME) }
    var showSoundDialog by remember { mutableStateOf(false) }
    var showRepeatDialog by remember { mutableStateOf(false) }
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

    Scaffold(
        containerColor = MinimalBackground,
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
                        color = Color(0xFFD4D4D8),
                        modifier = Modifier.size(54.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Cancel",
                                tint = MinimalBlack,
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
                            color = MinimalBlack
                        )
                    }

                    // Right circular button (Confirm & Save)
                    Surface(
                        onClick = onSave,
                        shape = CircleShape,
                        color = MinimalBlack,
                        shadowElevation = 8.dp,
                        modifier = Modifier.size(54.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Save",
                                tint = MinimalWhite,
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
                            onOpenSound = { showSoundDialog = true },
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
                            onSwitchToGame = { screenMode = EditorScreenMode.CHOOSE_GAME }
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
    onOpenSound: () -> Unit,
    onCycleSnooze: () -> Unit,
    onOpenRepeat: () -> Unit,
    onSwitchToGame: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Dual Hourglass Convex Curve Wheels Canvas Container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            // Curved background silhouette
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                val midX = w / 2f
                val midY = h / 2f

                // Draw smooth off-white curved lens for Left (Hours)
                val leftPath = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(midX - 20f, 0f)
                    cubicTo(
                        midX - 30f, h * 0.25f,
                        midX - 10f, midY - 60f,
                        midX - 8f, midY
                    )
                    cubicTo(
                        midX - 10f, midY + 60f,
                        midX - 30f, h * 0.75f,
                        midX - 20f, h
                    )
                    lineTo(0f, h)
                    close()
                }
                drawPath(leftPath, color = Color(0xFFF1F1F4))

                // Draw smooth off-white curved lens for Right (Minutes)
                val rightPath = Path().apply {
                    moveTo(w, 0f)
                    lineTo(midX + 20f, 0f)
                    cubicTo(
                        midX + 30f, h * 0.25f,
                        midX + 10f, midY - 60f,
                        midX + 8f, midY
                    )
                    cubicTo(
                        midX + 10f, midY + 60f,
                        midX + 30f, h * 0.75f,
                        midX + 20f, h
                    )
                    lineTo(w, h)
                    close()
                }
                drawPath(rightPath, color = Color(0xFFF1F1F4))
            }

            // Dual Columns (Hours & Minutes)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left Column: Hours (00..23)
                HourColumnPicker(
                    selectedHour = hour,
                    onSelect = onHourChange,
                    modifier = Modifier.weight(1f)
                )

                // Right Column: Minutes (00..59)
                MinuteColumnPicker(
                    selectedMinute = minute,
                    onSelect = onMinuteChange,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Three Quick Setting Action Cards: Sound, Snooze, Repeat
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
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

        // Challenge Shortcut Button (Navigate to Screen 3)
        Surface(
            onClick = onSwitchToGame,
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFDFDFE3),
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
                        tint = MinimalBlack,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Challenge: ${challengeConfig?.let { it::class.simpleName } ?: "None"}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MinimalBlack
                    )
                }

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = MinimalBlack
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun HourColumnPicker(
    selectedHour: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var dragAccumulator by remember { mutableFloatStateOf(0f) }

    Column(
        modifier = modifier
            .fillMaxHeight(0.65f)
            .pointerInput(selectedHour) {
                detectVerticalDragGestures { _, dragAmount ->
                    dragAccumulator += dragAmount
                    if (dragAccumulator > 40f) {
                        onSelect((selectedHour - 1 + 24) % 24)
                        dragAccumulator = 0f
                    } else if (dragAccumulator < -40f) {
                        onSelect((selectedHour + 1) % 24)
                        dragAccumulator = 0f
                    }
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        val offsets = listOf(3, 2, 1, 0, -1, -2, -3)

        offsets.forEach { offset ->
            val h = (selectedHour + offset + 24) % 24
            val isCurrent = offset == 0
            val alpha = when (kotlin.math.abs(offset)) {
                0 -> 1f
                1 -> 0.65f
                2 -> 0.35f
                else -> 0.18f
            }

            Box(
                modifier = Modifier
                    .width(76.dp)
                    .height(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .then(
                        if (isCurrent) Modifier.border(1.5f.dp, Color(0xFFC0C0C6), RoundedCornerShape(14.dp))
                        else Modifier
                    )
                    .clickable { onSelect(h) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "%02d".format(h),
                    fontSize = if (isCurrent) 38.sp else 28.sp,
                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                    color = MinimalBlack.copy(alpha = alpha)
                )
            }
        }
    }
}

@Composable
fun MinuteColumnPicker(
    selectedMinute: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var dragAccumulator by remember { mutableFloatStateOf(0f) }

    Column(
        modifier = modifier
            .fillMaxHeight(0.65f)
            .pointerInput(selectedMinute) {
                detectVerticalDragGestures { _, dragAmount ->
                    dragAccumulator += dragAmount
                    if (dragAccumulator > 40f) {
                        onSelect((selectedMinute - 1 + 60) % 60)
                        dragAccumulator = 0f
                    } else if (dragAccumulator < -40f) {
                        onSelect((selectedMinute + 1) % 60)
                        dragAccumulator = 0f
                    }
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        val offsets = listOf(3, 2, 1, 0, -1, -2, -3)

        offsets.forEach { offset ->
            val m = (selectedMinute + offset + 60) % 60
            val isCurrent = offset == 0
            val alpha = when (kotlin.math.abs(offset)) {
                0 -> 1f
                1 -> 0.65f
                2 -> 0.35f
                else -> 0.18f
            }

            Box(
                modifier = Modifier
                    .width(76.dp)
                    .height(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .then(
                        if (isCurrent) Modifier.border(1.5f.dp, Color(0xFFC0C0C6), RoundedCornerShape(14.dp))
                        else Modifier
                    )
                    .clickable { onSelect(m) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "%02d".format(m),
                    fontSize = if (isCurrent) 38.sp else 28.sp,
                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                    color = MinimalBlack.copy(alpha = alpha)
                )
            }
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
            tint = MinimalBlack,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = MinimalBlack
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
                    color = MinimalBlack
                )
                Text(
                    text = "the Game",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    color = MinimalTextLight
                )
            }

            TextButton(onClick = onSwitchToTime) {
                Text("Time", fontWeight = FontWeight.Bold, color = MinimalBlack)
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
                    color = if (isSelected) MinimalWhite else Color(0xFFF1F1F4),
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
                            color = MinimalBlack
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
                                        Text("24 × 3 + 15", fontSize = 26.sp, fontWeight = FontWeight.Bold)
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text("Solve to dismiss", fontSize = 13.sp, color = MinimalTextMuted)
                                    }
                                }
                                is ChallengeConfig.Shake -> {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(
                                            imageVector = Icons.Default.Vibration,
                                            contentDescription = null,
                                            tint = MinimalBlack,
                                            modifier = Modifier.size(48.dp)
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text("30 Shakes", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                                is ChallengeConfig.Nfc -> {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(
                                            imageVector = Icons.Default.Nfc,
                                            contentDescription = null,
                                            tint = MinimalBlack,
                                            modifier = Modifier.size(48.dp)
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text("Scan Registered Tag", fontSize = 14.sp, fontWeight = FontWeight.Medium)
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
                            color = if (isSelected) MinimalBlack else MinimalTextLight
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Circular Dial Canvas with Centered Digital Time
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
                        color = if (isHour) MinimalBlack else Color(0xFFC4C4CA),
                        start = Offset(startX, startY),
                        end = Offset(endX, endY),
                        strokeWidth = if (isHour) 2.5f else 1.2f
                    )
                }

                // Inner circle outline
                drawCircle(
                    color = Color(0xFFE0E0E6),
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
                color = MinimalBlack
            )
        }
    }
}

// 12x12 Dot Matrix Preview matching the mockup
@Composable
fun DotMatrixPatternPreview() {
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
                    color = if (isPattern) MinimalBlack else Color(0xFFC0C0C8),
                    radius = if (isPattern) 3.5f else 2.2f,
                    center = Offset(cx, cy)
                )
            }
        }
    }
}
