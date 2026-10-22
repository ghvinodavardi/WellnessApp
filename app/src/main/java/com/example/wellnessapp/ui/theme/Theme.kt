package com.example.wellnessapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.example.wellnessapp.viewmodel.ThemeViewModel

enum class ThemeMode {
    LIGHT, DARK, SYSTEM
}

@Composable
fun WellnessAppTheme(
    themeViewModel: ThemeViewModel,
    content: @Composable () -> Unit
) {
    val themeMode by themeViewModel.themeMode.collectAsState()

    val darkMode = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    val DarkColors = darkColorScheme(
        primary = Color(0xFF6BC99A),
        secondary = Color(0xFF4DB380),
        background = Color(0xFF0F1C17),
        surface = Color(0xFF1B2A24),
        surfaceVariant = Color(0xFF244135),
        onPrimary = Color(0xFF0F1C17),
        onBackground = Color(0xFFD8F5E6),
        onSurface = Color(0xFFD8F5E6)
    )

    val LightColors = lightColorScheme(
        primary = Color(0xFF2D7552),
        secondary = Color(0xFF6BC99A),
        background = Color(0xFFF5FAF8),
        surface = Color.White,
        surfaceVariant = Color(0xFFE4F5EB),
        onPrimary = Color.White,
        onBackground = Color(0xFF163628),
        onSurface = Color(0xFF163628)
    )

    MaterialTheme(
        colorScheme = if (darkMode) DarkColors else LightColors,
        typography = MaterialTheme.typography,  // ✔ стандартная, нет ошибок
        shapes = MaterialTheme.shapes,          // ✔ стандартная, нет ошибок
        content = content
    )
}
