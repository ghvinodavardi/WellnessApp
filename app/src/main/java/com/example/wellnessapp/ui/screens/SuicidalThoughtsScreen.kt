package com.example.wellnessapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.wellnessapp.R

@Composable
fun SuicidalThoughtsScreen(navController: NavController) {

    val steps = listOf(
        stringResource(R.string.reach_out) to stringResource(R.string.reach_out_crisis),
        stringResource(R.string.remove_harm) to stringResource(R.string.ensure_items_moved_away),
        stringResource(R.string.mindfulness) to stringResource(R.string.take_slow_breaths),
        stringResource(R.string.write_it_down) to stringResource(R.string.journal_feelings),
        stringResource(R.string.get_help) to stringResource(R.string.contact_healthcare)
    )

    SuicideGuideScreen(
        title = stringResource(R.string.managing_suicidal_thoughts),
        steps = steps,
        navController = navController
    )
}

@Composable
fun SuicideGuideScreen(
    title: String,
    steps: List<Pair<String, String>>,
    navController: NavController
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.step_by_step_guide),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            steps.forEachIndexed { index, pair ->
                SuicideStepCard(
                    stepNumber = index + 1,
                    title = pair.first,
                    description = pair.second
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}


@Composable
fun SuicideStepCard(stepNumber: Int, title: String, description: String) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            Text(
                text = stringResource(R.string.step, stepNumber),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
