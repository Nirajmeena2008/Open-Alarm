package com.alarmapp.feature.alarm.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Minimalist Design Palette (Matching UI Mockup)
val MinimalBackground = Color(0xFFE4E4E6)
val MinimalSurface = Color(0xFFF2F2F4)
val MinimalCard = Color(0xFFF9F9FA)
val MinimalCardDarker = Color(0xFFDEDEE2)
val MinimalBlack = Color(0xFF111114)
val MinimalWhite = Color(0xFFFFFFFF)
val MinimalTextMuted = Color(0xFF8E8E93)
val MinimalTextLight = Color(0xFFAAAAAF)
val MinimalRedAccent = Color(0xFFFF3B30)
val MinimalPillSelected = Color(0xFFFFFFFF)
val MinimalPillUnselected = Color(0x00000000)

private val LightColorScheme = lightColorScheme(
    primary = MinimalBlack,
    onPrimary = MinimalWhite,
    primaryContainer = MinimalSurface,
    onPrimaryContainer = MinimalBlack,
    secondary = MinimalRedAccent,
    onSecondary = MinimalWhite,
    background = MinimalBackground,
    onBackground = MinimalBlack,
    surface = MinimalSurface,
    onSurface = MinimalBlack,
    surfaceVariant = MinimalCardDarker,
    onSurfaceVariant = MinimalTextMuted,
    error = MinimalRedAccent,
    onError = MinimalWhite
)

private val DarkColorScheme = darkColorScheme(
    primary = MinimalWhite,
    onPrimary = MinimalBlack,
    primaryContainer = Color(0xFF222226),
    onPrimaryContainer = MinimalWhite,
    secondary = MinimalRedAccent,
    onSecondary = MinimalWhite,
    background = Color(0xFF141417),
    onBackground = Color(0xFFF4F4F6),
    surface = Color(0xFF1C1C20),
    onSurface = Color(0xFFF4F4F6),
    surfaceVariant = Color(0xFF2A2A30),
    onSurfaceVariant = Color(0xFF9E9EA8),
    error = MinimalRedAccent,
    onError = MinimalWhite
)

private val AmoledColorScheme = darkColorScheme(
    primary = MinimalWhite,
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF161616),
    onPrimaryContainer = MinimalWhite,
    secondary = MinimalRedAccent,
    onSecondary = MinimalWhite,
    background = Color(0xFF000000),
    onBackground = Color(0xFFFFFFFF),
    surface = Color(0xFF101010),
    onSurface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFF1C1C1E),
    onSurfaceVariant = Color(0xFFAAAAAF),
    error = MinimalRedAccent,
    onError = MinimalWhite
)

@Composable
fun AlarmTheme(
    themeMode: String = "Light",
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = when {
        themeMode.startsWith("AMOLED") -> AmoledColorScheme
        themeMode == "Dark" || darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}
