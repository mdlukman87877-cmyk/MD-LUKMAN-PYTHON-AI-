package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = PythonBlueLight,
    onPrimary = Color(0xFF001B3F),
    primaryContainer = PythonBlueDark,
    onPrimaryContainer = Color(0xFFD6E4FF),
    secondary = PythonYellow,
    onSecondary = Color(0xFF422B00),
    secondaryContainer = PythonYellowDark,
    onSecondaryContainer = PythonYellowLight,
    tertiary = AccentCyan,
    background = DarkBackground,
    onBackground = Color(0xFFF1F5F9),
    surface = DarkSurface,
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = DarkSurfaceCard,
    onSurfaceVariant = Color(0xFFCBD5E1),
    error = AccentCoral,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = PythonBlue,
    onPrimary = Color.White,
    primaryContainer = PythonBlueContainer,
    onPrimaryContainer = Color(0xFF001B3F),
    secondary = PythonYellowDark,
    onSecondary = Color.White,
    secondaryContainer = PythonYellowLight,
    onSecondaryContainer = Color(0xFF422B00),
    tertiary = AccentCyan,
    background = LightBackground,
    onBackground = Color(0xFF0F172A),
    surface = LightSurface,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF334155),
    error = AccentCoral,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
