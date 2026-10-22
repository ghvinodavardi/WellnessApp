package com.example.wellnessapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wellnessapp.data.LanguagePreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LanguageViewModel(private val prefs: LanguagePreferences) : ViewModel() {

    val language: StateFlow<AppLanguage> =
        prefs.language.map { stored ->
            if (stored == "UA") AppLanguage.UA else AppLanguage.EN
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            AppLanguage.EN
        )

    fun setLanguage(lang: AppLanguage) {
        viewModelScope.launch {
            prefs.saveLanguage(lang.name)
        }
    }
}
