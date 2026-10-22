package com.example.wellnessapp.ui.screens

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.SelfImprovement
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.core.net.toUri
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import com.example.wellnessapp.R


data class CalmVideo(
    val title: String,
    val category: String,
    val youtubeId: String,
    val icon: ImageVector
)

@Composable
fun CalmLibraryScreen(navController: NavController) {

    val context = LocalContext.current

    val categories = listOf(
        stringResource(R.string.all),
        stringResource(R.string.meditations),
        stringResource(R.string.music),
        stringResource(R.string.yoga)
    )
    val allLabel = stringResource(R.string.all)
    var selectedCategory by remember { mutableStateOf(allLabel) }

    val videos = listOf(
        CalmVideo("Yoga Flow 1", stringResource(R.string.yoga), "Sa-zau-bhPk", Icons.Outlined.SelfImprovement),
        CalmVideo("Yoga Flow 2", stringResource(R.string.yoga), "-BHiNygR9R4", Icons.Outlined.SelfImprovement),
        CalmVideo("Yoga Flow 3", stringResource(R.string.yoga), "WnSr8w4QEWo", Icons.Outlined.SelfImprovement),

        CalmVideo("Meditation 1", stringResource(R.string.meditations), "RS0-cbRTp1E", Icons.Outlined.Spa),
        CalmVideo("Meditation 2", stringResource(R.string.meditations), "2jjANhFm8ig", Icons.Outlined.Spa),
        CalmVideo("Meditation 3", stringResource(R.string.meditations), "HmRwsATh8YY", Icons.Outlined.Spa),

        CalmVideo("Relaxing Music 1", stringResource(R.string.music), "e3LWySXuKDA", Icons.Outlined.MusicNote),
        CalmVideo("Relaxing Music 2", stringResource(R.string.music), "XR1B1hAzPaI", Icons.Outlined.MusicNote),
        CalmVideo("Relaxing Music 3", stringResource(R.string.music), "l3HVvYbpR6w", Icons.Outlined.MusicNote)
    )

    val filtered =
        if (selectedCategory == stringResource(R.string.all)) videos else videos.filter { it.category == selectedCategory }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {

        Spacer(Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.calm_library_title),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            categories.forEach { cat ->
                FilterChip(
                    selected = selectedCategory == cat,
                    onClick = { selectedCategory = cat },
                    label = { Text(cat) },
                    shape = CircleShape,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        filtered.forEach { video ->
            CalmVideoCard(video) {
                val url = "https://www.youtube.com/watch?v=${video.youtubeId}"
                context.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
fun CalmVideoCard(video: CalmVideo, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            // ICON PREVIEW BLOCK
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = video.icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(64.dp)
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = video.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
