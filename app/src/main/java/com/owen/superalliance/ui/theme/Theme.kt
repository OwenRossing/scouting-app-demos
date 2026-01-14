package com.owen.superalliance.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = DarkAccent,
    secondary = DarkMuted,
    tertiary = DarkGood,
    background = DarkBackground,
    surface = DarkPanel,
    surfaceVariant = DarkSurfaceVariant,
    surfaceBright = DarkSurfaceBright,
    onPrimary = DarkText,
    onSecondary = DarkText,
    onTertiary = DarkBackground,
    onBackground = DarkText,
    onSurface = DarkText,
    error = DarkBad,
    onError = DarkText,
    outline = DarkStroke
)

@Composable
fun SuperallianceTheme(
    darkTheme: Boolean = true, // Always use dark theme for scouting
    // Dynamic color is disabled for consistent mission-critical appearance
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}