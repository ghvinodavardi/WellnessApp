package com.example.wellnessapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wellnessapp.data.OpenAIClient
import com.example.wellnessapp.ui.theme.WellnessAppTheme
import com.example.wellnessapp.viewmodel.*
import java.util.Locale

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            OpenAIClient.loadKey(this)

            val context = LocalContext.current

            val themeViewModel: ThemeViewModel =
                viewModel(factory = ThemeViewModelFactory(context))

            val languageViewModel: LanguageViewModel =
                viewModel(factory = LanguageViewModelFactory(context))

            val language by languageViewModel.language.collectAsState()

            LaunchedEffect(language) {
                updateLocaleIfNeeded(language)
            }

            WellnessAppTheme(themeViewModel = themeViewModel) {
                WellnessApp(
                    themeViewModel = themeViewModel,
                    languageViewModel = languageViewModel
                )
            }
        }
    }


    private fun updateLocaleIfNeeded(language: AppLanguage) {
        val newLocale = when (language) {
            AppLanguage.EN -> Locale("en")
            AppLanguage.UA -> Locale("uk")
        }

        val currentLocale = resources.configuration.locales[0]

        if (currentLocale.language != newLocale.language) {
            Locale.setDefault(newLocale)

            val config = resources.configuration
            config.setLocale(newLocale)

            @Suppress("DEPRECATION")
            resources.updateConfiguration(config, resources.displayMetrics)

            recreate()
        }
    }
}

@Composable
fun WellnessApp(
    themeViewModel: ThemeViewModel,
    languageViewModel: LanguageViewModel
) {
    NavigationComponent(
        themeViewModel = themeViewModel,
        languageViewModel = languageViewModel
    )
}
