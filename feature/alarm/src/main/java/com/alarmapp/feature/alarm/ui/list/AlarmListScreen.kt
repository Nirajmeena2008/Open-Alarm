package com.alarmapp.feature.alarm.ui.list

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alarmapp.domain.model.Alarm
import com.alarmapp.domain.model.AlarmId
import com.alarmapp.domain.model.ChallengeConfig
import com.alarmapp.domain.model.DayOfWeek
import com.alarmapp.domain.model.RingtoneSource
import com.alarmapp.feature.alarm.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.format.TextStyle
import java.util.Locale
import kotlin.math.abs

@Composable
fun AlarmListScreen(
    uiState: AlarmListUiState,
    onToggleAlarm: (AlarmId, Boolean) -> Unit,
    onDeleteAlarm: (AlarmId) -> Unit,
    onEditAlarm: (String) -> Unit,
    onAddAlarm: () -> Unit,
    onCalendarClick: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onNavigateToDeveloper: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Current live time
    var currentTime by remember {
        mutableStateOf(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()))
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            currentTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        }
    }

    // Days row state
    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val todayIndex = currentTime.dayOfWeek.ordinal.coerceIn(0, 6)
    var selectedDayIndex by remember { mutableIntStateOf(todayIndex) }

    // Find next upcoming alarm and calculate minutes difference
    val nowMinutes = currentTime.hour * 60 + currentTime.minute
    val nextAlarm = uiState.alarms.filter { it.isEnabled }.minByOrNull { alarm ->
        val alarmMinutes = alarm.time.hour * 60 + alarm.time.minute
        var diff = alarmMinutes - nowMinutes
        if (diff <= 0) diff += 24 * 60
        diff
    }

    val countdownText = if (nextAlarm != null) {
        val alarmMinutes = nextAlarm.time.hour * 60 + nextAlarm.time.minute
        var diff = alarmMinutes - nowMinutes
        if (diff <= 0) diff += 24 * 60
        val hours = diff / 60
        val mins = diff % 60
        when {
            hours > 0 && mins > 0 -> "$hours h $mins min"
            hours > 0 -> "$hours hours"
            else -> "$mins min"
        }
    } else {
        null
    }

    val isLight = MaterialTheme.colorScheme.background == MinimalBackground
    val dockBg = if (isLight) MinimalBlack else MaterialTheme.colorScheme.surfaceVariant
    val dockFg = if (isLight) MinimalWhite else MaterialTheme.colorScheme.onSurface

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            // Floating Dock Bar matching the UI design
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left circular calendar button
                    Surface(
                        onClick = onCalendarClick,
                        shape = CircleShape,
                        color = dockBg,
                        shadowElevation = 8.dp,
                        modifier = Modifier.size(54.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = "Calendar / Routines",
                                tint = dockFg,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    // Center timeline capsule dock
                    Surface(
                        shape = RoundedCornerShape(27.dp),
                        color = dockBg,
                        shadowElevation = 8.dp,
                        modifier = Modifier
                            .weight(1f)
                            .height(54.dp)
                            .padding(horizontal = 14.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Current time label
                            Text(
                                text = String.format("%02d:%02d", currentTime.hour, currentTime.minute),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = dockFg
                            )

                            // Mini ruler canvas
                            Canvas(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(28.dp)
                                    .padding(horizontal = 8.dp)
                            ) {
                                val tickCount = 20
                                val step = size.width / (tickCount - 1)
                                val currentRatio = (currentTime.hour * 60 + currentTime.minute) / (24f * 60f)
                                val cursorX = size.width * currentRatio

                                for (i in 0 until tickCount) {
                                    val x = i * step
                                    val isMajor = i % 5 == 0
                                    val top = if (isMajor) 4f else 10f
                                    val bottom = if (isMajor) size.height - 4f else size.height - 10f
                                    drawLine(
                                        color = Color(0xFF55555C),
                                        start = Offset(x, top),
                                        end = Offset(x, bottom),
                                        strokeWidth = 1.5f
                                    )
                                }

                                // Red indicator line
                                drawLine(
                                    color = MinimalRedAccent,
                                    start = Offset(cursorX, 0f),
                                    end = Offset(cursorX, size.height),
                                    strokeWidth = 2.5f
                                )
                            }

                            // Next alarm time label
                            val nextTimeStr = if (nextAlarm != null) {
                                String.format("%02d:%02d", nextAlarm.time.hour, nextAlarm.time.minute)
                            } else {
                                "--:--"
                            }
                            Text(
                                text = nextTimeStr,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFAAAAAF)
                            )
                        }
                    }

                    // Right circular '+' button
                    Surface(
                        onClick = onAddAlarm,
                        shape = CircleShape,
                        color = dockBg,
                        shadowElevation = 8.dp,
                        modifier = Modifier.size(54.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Alarm",
                                tint = dockFg,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }
        },
        modifier = modifier
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Top Header: "Alarm" + Dropdown Arrow
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Alarm",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )

                var showMenu by remember { mutableStateOf(false) }

                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Options",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Sleep & Routines") },
                            onClick = {
                                showMenu = false
                                onCalendarClick()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Settings") },
                            onClick = {
                                showMenu = false
                                onNavigateToSettings()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Developer Settings") },
                            onClick = {
                                showMenu = false
                                onNavigateToDeveloper()
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Massive Digital Clock (e.g. "09:41")
            val formattedTime = String.format("%02d:%02d", currentTime.hour, currentTime.minute)
            Text(
                text = formattedTime,
                fontSize = 72.sp,
                fontWeight = FontWeight.Light,
                letterSpacing = (-2).sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            // Subtitle: "The next alarm clock in X min"
            val annotatedCountdown = buildAnnotatedString {
                append("The next alarm clock in ")
                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)) {
                    append(countdownText ?: "none set")
                }
            }
            Text(
                text = annotatedCountdown,
                fontSize = 14.sp,
                color = MinimalTextMuted
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 24-Hour Timeline / Ruler Bar Graph
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = if (isLight) Color(0xFFEDEDF0) else MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(136.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Top Legend / Context Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "24-HOUR TIMELINE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MinimalTextMuted,
                            letterSpacing = 0.5.sp
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Current time legend indicator
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .clip(CircleShape)
                                        .background(MinimalRedAccent)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Now",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            // Alarm legend indicator
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.onSurface)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Alarm",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    val alarmPinColor = MaterialTheme.colorScheme.onSurface

                    // Center Ruler Canvas
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                    ) {
                        val totalMinutes = 24 * 60f
                        val currentMin = currentTime.hour * 60 + currentTime.minute
                        val currentRatio = currentMin / totalMinutes

                        // Draw ticks for 48 half-hour slots
                        val ticks = 48
                        val step = size.width / ticks

                        for (i in 0..ticks) {
                            val x = i * step
                            val isSixHour = i % 12 == 0
                            val isHour = i % 2 == 0
                            val top = when {
                                isSixHour -> 0f
                                isHour -> 8f
                                else -> 18f
                            }
                            val bottom = size.height - top

                            drawLine(
                                color = if (isLight) (if (isSixHour) Color(0xFFAAAAAF) else Color(0xFFCFCFD5)) else (if (isSixHour) Color(0xFF88888F) else Color(0xFF3A3A42)),
                                start = Offset(x, top),
                                end = Offset(x, bottom),
                                strokeWidth = if (isSixHour) 1.8f else 1.2f,
                                cap = StrokeCap.Round
                            )
                        }

                        // Draw scheduled alarm lines with pin heads
                        uiState.alarms.forEach { alarm ->
                            if (alarm.isEnabled) {
                                val alarmMin = alarm.time.hour * 60 + alarm.time.minute
                                val alarmRatio = alarmMin / totalMinutes
                                val alarmX = size.width * alarmRatio

                                drawLine(
                                    color = alarmPinColor,
                                    start = Offset(alarmX, 0f),
                                    end = Offset(alarmX, size.height),
                                    strokeWidth = 2.5f,
                                    cap = StrokeCap.Round
                                )

                                // Solid black pin head
                                drawCircle(
                                    color = alarmPinColor,
                                    radius = 4.5f,
                                    center = Offset(alarmX, 4f)
                                )
                            }
                        }

                        // Draw Red needle for current time with precision indicator head
                        val cursorX = size.width * currentRatio
                        drawLine(
                            color = MinimalRedAccent,
                            start = Offset(cursorX, 0f),
                            end = Offset(cursorX, size.height),
                            strokeWidth = 3f,
                            cap = StrokeCap.Round
                        )

                        // Red pin head with white center dot
                        drawCircle(
                            color = MinimalRedAccent,
                            radius = 5.5f,
                            center = Offset(cursorX, 4f)
                        )
                        drawCircle(
                            color = Color.White,
                            radius = 2f,
                            center = Offset(cursorX, 4f)
                        )
                    }

                    // Bottom Landmark Hour Labels
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val landmarks = listOf("12 AM", "6 AM", "12 PM", "6 PM", "12 AM")
                        landmarks.forEach { label ->
                            Text(
                                text = label,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MinimalTextMuted
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Day of Week Selector Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(if (isLight) Color(0xFFE2E2E6) else MaterialTheme.colorScheme.surface)
                    .padding(vertical = 4.dp, horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                days.forEachIndexed { index, day ->
                    val isSelected = index == selectedDayIndex
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(18.dp))
                            .background(if (isSelected) (if (isLight) MinimalWhite else MaterialTheme.colorScheme.surfaceVariant) else Color.Transparent)
                            .then(if (isSelected) Modifier.shadow(2.dp, RoundedCornerShape(18.dp)) else Modifier)
                            .clickable { selectedDayIndex = index }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = day,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Horizontal Alarm Cards Carousel
            if (uiState.alarms.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No alarms set for this day\nTap '+' to create one",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = MinimalTextMuted,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            } else {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
                ) {
                    items(uiState.alarms, key = { it.id.value }) { alarm ->
                        MinimalAlarmCard(
                            alarm = alarm,
                            onToggle = { isChecked -> onToggleAlarm(alarm.id, isChecked) },
                            onClick = { onEditAlarm(alarm.id.value) },
                            onDelete = { onDeleteAlarm(alarm.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MinimalAlarmCard(
    alarm: Alarm,
    onToggle: (Boolean) -> Unit,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDeleteConfirm by remember { mutableStateOf(false) }

    val hour12 = when {
        alarm.time.hour == 0 -> 12
        alarm.time.hour > 12 -> alarm.time.hour - 12
        else -> alarm.time.hour
    }
    val amPm = if (alarm.time.hour < 12) "AM" else "PM"

    val soundTitle = when (val ringtone = alarm.ringtone) {
        is RingtoneSource.File -> ringtone.title
        is RingtoneSource.System -> ringtone.title
        is RingtoneSource.Default -> "Default Tone"
        is RingtoneSource.Silent -> "Silent"
    }

    val challengeLabel = when (alarm.challengeConfig) {
        is ChallengeConfig.Math -> "Math Game"
        is ChallengeConfig.Memory -> "Memory Game"
        is ChallengeConfig.Shake -> "Shake Game"
        is ChallengeConfig.Nfc -> "NFC Tag"
        is ChallengeConfig.Qr -> "QR Code"
        is ChallengeConfig.Plugin -> "Plugin"
        null -> null
    }

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (alarm.isEnabled) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (alarm.isEnabled) 6.dp else 1.dp),
        modifier = modifier
            .width(260.dp)
            .height(140.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onClick() },
                    onLongPress = { showDeleteConfirm = true }
                )
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Row with Digital Time & Play/Toggle Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "%d:%02d".format(hour12, alarm.time.minute),
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (alarm.isEnabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = amPm,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (alarm.isEnabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Visible Delete Button
                    Surface(
                        onClick = { showDeleteConfirm = true },
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "Delete Alarm",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // Play / Toggle Button (Triangle Play Icon)
                    Surface(
                        onClick = { onToggle(!alarm.isEnabled) },
                        shape = CircleShape,
                        color = if (alarm.isEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (alarm.isEnabled) Icons.Default.PlayArrow else Icons.Default.Stop,
                                contentDescription = if (alarm.isEnabled) "Disable" else "Enable",
                                tint = if (alarm.isEnabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // Subtitle: Sound & Challenge Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "• $soundTitle",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    modifier = Modifier.weight(1f, fill = false)
                )

                if (challengeLabel != null) {
                    Text(
                        text = challengeLabel,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFEBEBEF))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete Alarm", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to delete this alarm?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete()
                        showDeleteConfirm = false
                    }
                ) {
                    Text("Delete", color = MinimalRedAccent, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
