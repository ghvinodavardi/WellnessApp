package com.example.wellnessapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wellnessapp.R

@Composable
fun ResultBadScreen(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp)
    ) {

        Spacer(Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.emergency_help),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surface,
                    RoundedCornerShape(24.dp)
                )
                .padding(22.dp)
        ) {
            Text(
                text = stringResource(R.string.immediate_danger_warning),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp
            )
        }

        Spacer(Modifier.height(28.dp))

        EmergencyCard(
            title = stringResource(R.string.call_emergency_services),
            subtitle = stringResource(R.string.emergency_911),
            onClick = {}
        )

        Spacer(Modifier.height(14.dp))

        EmergencyCard(
            title = stringResource(R.string.call_loved_one),
            subtitle = stringResource(R.string.reach_out_trusted),
            onClick = {}
        )

        Spacer(Modifier.height(14.dp))

        EmergencyCard(
            title = stringResource(R.string.crisis_hotline),
            subtitle = stringResource(R.string.crisis_988),
            onClick = {}
        )


    }
}

@Composable
private fun EmergencyCard(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                RoundedCornerShape(26.dp)
            )
            .clickable(onClick = onClick)
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(54.dp)
                .background(
                    MaterialTheme.colorScheme.primary,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text("!", color = MaterialTheme.colorScheme.onPrimary)
        }

        Spacer(Modifier.width(16.dp))

        Column {
            Text(
                title,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                subtitle,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
