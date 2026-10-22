package com.example.wellnessapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.wellnessapp.R

@Composable
fun SosScreen(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.immediate_danger_warning),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

            SosOptionCard(
                title = stringResource(R.string.call_emergency_services),
                subtitle = stringResource(R.string.emergency_911),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            )

            SosOptionCard(
                title = stringResource(R.string.call_loved_one),
                subtitle = stringResource(R.string.reach_out_trusted),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            )

            SosOptionCard(
                title = stringResource(R.string.crisis_hotline),
                subtitle = stringResource(R.string.crisis_988),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.crisis_instructions),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

            CrisisInstructionCard(
                title = stringResource(R.string.panic_attack),
                subtitle = stringResource(R.string.breathing_exercises_grounding),
                onClick = { navController.navigate("panicAttack") }
            )

            CrisisInstructionCard(
                title = stringResource(R.string.suicidal_thoughts),
                subtitle = stringResource(R.string.immediate_coping_strategies),
                onClick = { navController.navigate("suicidalThoughts") }
            )

            CrisisInstructionCard(
                title = stringResource(R.string.self_harm_urges),
                subtitle = stringResource(R.string.safe_alternatives_support),
                onClick = { navController.navigate("selfHarmUrges") }
            )
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}



@Composable
private fun SosOptionCard(title: String, subtitle: String, color: Color) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(color),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun CrisisInstructionCard(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(Modifier.padding(16.dp)) {

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}
