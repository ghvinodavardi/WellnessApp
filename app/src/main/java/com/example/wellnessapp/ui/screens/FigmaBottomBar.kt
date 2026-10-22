package com.example.wellnessapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.wellnessapp.Screen

@Composable
fun FigmaBottomBar(
    screens: List<Screen>,
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp
    ) {
        screens.forEach { screen ->

            val isSelected = currentRoute?.startsWith(screen.route) == true

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(screen.route) },

                icon = {
                    if (isSelected) {
                        Surface(
                            modifier = Modifier.size(36.dp),    // уменьшаем hit-box
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer,
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = screen.icon,
                                    contentDescription = stringResource(screen.labelRes),
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    } else {
                        Icon(
                            imageVector = screen.icon,
                            contentDescription = stringResource(screen.labelRes),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },

                label = {
                    Text(
                        text = stringResource(screen.labelRes),
                        style = MaterialTheme.typography.labelMedium,
                        color = if (isSelected)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },

                alwaysShowLabel = true
            )
        }
    }
}
