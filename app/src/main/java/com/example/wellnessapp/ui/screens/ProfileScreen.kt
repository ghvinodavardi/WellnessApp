package com.example.wellnessapp.ui.screens

import android.net.Uri
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.wellnessapp.R
import com.example.wellnessapp.viewmodel.AppLanguage
import com.example.wellnessapp.viewmodel.LanguageViewModel
import com.example.wellnessapp.viewmodel.ThemeViewModel
import com.example.wellnessapp.ui.theme.ThemeMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    themeViewModel: ThemeViewModel,
    languageViewModel: LanguageViewModel
) {
    val themeMode by themeViewModel.themeMode.collectAsState()
    val appLanguage by languageViewModel.language.collectAsState()

    val scrollState = rememberScrollState()
    var avatarUri by remember { mutableStateOf<Uri?>(null) }

    // Image picker
    val registryOwner = LocalActivityResultRegistryOwner.current
    val registry = registryOwner?.activityResultRegistry
    val imagePicker = remember(registry) {
        registry?.register(
            "avatar_picker",
            ActivityResultContracts.GetContent()
        ) { uri -> avatarUri = uri }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.profile)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
                .navigationBarsPadding()
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                            .clickable { imagePicker?.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        if (avatarUri != null) {
                            AsyncImage(
                                model = avatarUri,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(55.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(12.dp))



                    Text(
                        text = stringResource(R.string.manage_settings),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center

                    )
                }
            }

            SettingsCard(stringResource(R.string.integrations)) {
                var healthEnabled by remember { mutableStateOf(true) }
                var cycleEnabled by remember { mutableStateOf(false) }

                IntegrationRow(
                    icon = "📈",
                    title = stringResource(R.string.apple_health_google_fit),
                    subtitle = stringResource(R.string.track_daily_steps),
                    checked = healthEnabled,
                    onCheckedChange = { healthEnabled = it }
                )

                Divider()

                IntegrationRow(
                    icon = "💚",
                    title = stringResource(R.string.cycle_tracking),
                    subtitle = stringResource(R.string.sync_with_flo),
                    checked = cycleEnabled,
                    onCheckedChange = { cycleEnabled = it }
                )
            }

            SettingsCard(stringResource(R.string.app_appearance)) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(Modifier.weight(1f)) {
                        Text(stringResource(R.string.dark_mode), style = MaterialTheme.typography.titleMedium)
                        Text(
                            stringResource(R.string.switch_theme),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }

                    Switch(
                        checked = themeMode == ThemeMode.DARK,
                        onCheckedChange = {
                            val newMode =
                                if (themeMode == ThemeMode.DARK) ThemeMode.LIGHT else ThemeMode.DARK
                            themeViewModel.toggleTheme(newMode)
                        }
                    )
                }
            }

            SettingsCard(stringResource(R.string.language)) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    LanguageButton("EN", appLanguage == AppLanguage.EN) {
                        languageViewModel.setLanguage(AppLanguage.EN)
                    }
                    LanguageButton("UA", appLanguage == AppLanguage.UA) {
                        languageViewModel.setLanguage(AppLanguage.UA)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}


@Composable
fun SettingsCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(22.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(14.dp))
            content()
        }
    }
}

@Composable
fun IntegrationRow(
    icon: String,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Text(icon)
        }

        Spacer(Modifier.width(16.dp))

        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            Text(
                subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
fun LanguageButton(label: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(
                if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
                else MaterialTheme.colorScheme.surfaceVariant
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 12.dp)
    ) {
        Text(label, color = MaterialTheme.colorScheme.onSurface)
    }
}
