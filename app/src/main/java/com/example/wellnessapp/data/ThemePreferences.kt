package com.example.wellnessapp.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.wellnessapp.ui.theme.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class ThemePreferences(private val context: Context) {
    private val THEME_KEY = intPreferencesKey("theme_mode")

    val themeMode: Flow<ThemeMode> = context.dataStore.data.map { preferences ->
        when (preferences[THEME_KEY] ?: ThemeMode.SYSTEM.ordinal) {
            ThemeMode.LIGHT.ordinal -> ThemeMode.LIGHT
            ThemeMode.DARK.ordinal -> ThemeMode.DARK
            else -> ThemeMode.SYSTEM
        }
    }

    suspend fun saveThemeMode(mode: ThemeMode) {
        context.dataStore.edit { settings ->
            settings[THEME_KEY] = mode.ordinal
        }
    }
}