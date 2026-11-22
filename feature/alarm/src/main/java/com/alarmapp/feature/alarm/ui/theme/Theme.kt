package com.alarmapp.feature.alarm.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val DeepBlack = Color(0xFF0F0F14)
val SurfaceDark = Color(0xFF1A1A24)
val SurfaceHighlight = Color(0xFF252533)
val PrimaryAmber = Color(0xFFFFB300)
val PrimaryAmberVariant = Color(0xFFFFA000)
val SecondaryTeal = Color(0xFF26A69A)
val ErrorRed = Color(0xFFEF5350)
val TextPrimary = Color(0xFFF0F0F5)
val TextSecondary = Color(0xFF9E9EAA)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryAmber,
    onPrimary = Color.Black,
    primaryContainer = PrimaryAmberVariant,
    onPrimaryContainer = Color.White,
    secondary = SecondaryTeal,
    onSecondary = Color.Black,
    background = DeepBlack,
    onBackground = TextPrimary,
    surface = SurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceHighlight,
    onSurfaceVariant = TextSecondary,
    error = ErrorRed,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFE65100),
    onPrimary = Color.White,
    secondary = Color(0xFF00695C),
    onSecondary = Color.White,
    background = Color(0xFFF9F9FB),
    onBackground = Color(0xFF1E1E24),
    surface = Color.White,
    onSurface = Color(0xFF1E1E24)
)

@Composable
fun AlarmTheme(
    darkTheme: Boolean = true, // Default to dark for AMOLED battery conservation & nighttime eyes
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}
