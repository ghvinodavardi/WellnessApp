package com.example.wellnessapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wellnessapp.data.ThemePreferences
import com.example.wellnessapp.ui.theme.ThemeMode
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThemeViewModel(private val preferences: ThemePreferences) : ViewModel() {

    val themeMode: StateFlow<ThemeMode> =
        preferences.themeMode.map { it }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = ThemeMode.SYSTEM
            )

    fun toggleTheme(mode: ThemeMode) {
        viewModelScope.launch {
            preferences.saveThemeMode(mode)
        }
    }
}