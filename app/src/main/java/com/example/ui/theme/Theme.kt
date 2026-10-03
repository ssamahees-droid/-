package com.example.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = DarkGreen,
    onPrimary = WarmWhite,
    primaryContainer = SageLight,
    onPrimaryContainer = DeepForest,
    secondary = SageGreen,
    onSecondary = DeepForest,
    secondaryContainer = WarmBeige,
    onSecondaryContainer = TextDark,
    tertiary = WarmTerracotta,
    onTertiary = WarmWhite,
    tertiaryContainer = SoftPeach,
    onTertiaryContainer = TextDark,
    background = CreamBackground,
    onBackground = TextDark,
    surface = CreamBackground,
    onSurface = TextDark,
    surfaceVariant = WarmBeige,
    onSurfaceVariant = TextMuted,
    surfaceContainerHighest = SurfaceElevated,
    outline = DividerColor,
    outlineVariant = SageLight
)

private val DarkColorScheme = darkColorScheme(
    primary = SoftLeaf,
    onPrimary = DeepForest,
    primaryContainer = DarkGreen,
    onPrimaryContainer = SageLight,
    secondary = SageGreen,
    onSecondary = DeepForest,
    secondaryContainer = DeepForest,
    onSecondaryContainer = SageLight,
    tertiary = WarmTerracotta,
    onTertiary = DeepForest,
    background = DeepForest,
    onBackground = CreamBackground,
    surface = Color(0xFF1B2E24),
    onSurface = CreamBackground,
    surfaceVariant = Color(0xFF284435),
    onSurfaceVariant = SageLight,
    outline = Color(0xFF3D5C4C)
)

@Composable
fun NasmatHayatTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = colorScheme.background.toArgb()
                window.navigationBarColor = colorScheme.background.toArgb()
                WindowCompat.getInsetsController(window, view).apply {
                    isAppearanceLightStatusBars = !darkTheme
                    isAppearanceLightNavigationBars = !darkTheme
                }
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
