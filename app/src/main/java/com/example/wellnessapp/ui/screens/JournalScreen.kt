package com.example.wellnessapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.wellnessapp.R
import com.example.wellnessapp.data.JournalEntry
import com.example.wellnessapp.viewmodel.JournalViewModel
import java.time.*
import java.time.format.TextStyle
import java.util.*
import androidx.compose.ui.graphics.Color


@Composable
fun JournalScreen(
    navController: NavController,
    journalViewModel: JournalViewModel
) {
    val selectedDate by journalViewModel.selectedDate.collectAsState()
    val entries by journalViewModel.entries.collectAsState()

    var showFullCalendar by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Text(
                text = stringResource(R.string.my_journal),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(20.dp))

            MiniCalendarBar(
                selectedDate = selectedDate,
                onDateSelected = { journalViewModel.selectDate(it) },
                onOpenFullCalendar = { showFullCalendar = true }
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (entries.isEmpty()) {
                EmptyJournalState()
            } else {
                EntriesList(entries = entries)
            }
        }

        // FAB (+ button)
        FloatingActionButton(
            onClick = { navController.navigate("newJournalEntry") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.add))
        }
    }

    if (showFullCalendar) {
        FullCalendarDialog(
            initialDate = selectedDate,
            onDismiss = { showFullCalendar = false },
            onDateSelected = { date ->
                journalViewModel.selectDate(date)
                showFullCalendar = false
            }
        )
    }
}

@Composable
private fun EmptyJournalState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.no_entries_for_day),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.tap_to_add_thoughts),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun EntriesList(entries: List<JournalEntry>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(entries) { entry ->
            JournalEntryCard(entry)
        }
    }
}

@Composable
private fun JournalEntryCard(entry: JournalEntry) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 2.dp,
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = entry.time,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(6.dp))

            if (entry.type == "survey") {
                Text(
                    text = entry.title ?: stringResource(R.string.daily_survey),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.result, entry.result ?: "—"),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Text(
                    text = entry.text.orEmpty(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun MiniCalendarBar(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onOpenFullCalendar: () -> Unit
) {
    val monthText = selectedDate.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
        .replaceFirstChar { it.titlecase(Locale.getDefault()) } +
            " ${selectedDate.year}"

    val startOfWeek = selectedDate.with(DayOfWeek.MONDAY)
    val daysOfWeek = (0..6).map { startOfWeek.plusDays(it.toLong()) }

    Column {

        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onDateSelected(selectedDate.minusWeeks(1)) }) {
                Icon(Icons.Filled.ChevronLeft, contentDescription = null)
            }

            Text(
                text = monthText,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f).clickable { onOpenFullCalendar() }
            )

            IconButton(onClick = { onDateSelected(selectedDate.plusWeeks(1)) }) {
                Icon(Icons.Filled.ChevronRight, contentDescription = null)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            daysOfWeek.forEach { day ->
                val isSelected = day == selectedDate

                Column(
                    modifier = Modifier.width(40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        day.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primaryContainer
                                else Color.Transparent
                            )
                            .clickable { onDateSelected(day) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            day.dayOfMonth.toString(),
                            color = if (isSelected)
                                MaterialTheme.colorScheme.onPrimaryContainer
                            else
                                MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FullCalendarDialog(
    initialDate: LocalDate,
    onDismiss: () -> Unit,
    onDateSelected: (LocalDate) -> Unit
) {
    var visibleMonth by remember { mutableStateOf(YearMonth.from(initialDate)) }

    val title = visibleMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
        .replaceFirstChar { it.titlecase(Locale.getDefault()) } +
            " ${visibleMonth.year}"

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { visibleMonth = visibleMonth.minusMonths(1) }) {
                    Icon(Icons.Filled.ChevronLeft, contentDescription = null)
                }

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                IconButton(onClick = { visibleMonth = visibleMonth.plusMonths(1) }) {
                    Icon(Icons.Filled.ChevronRight, contentDescription = null)
                }
            }
        },
        text = {
            CalendarGrid(
                visibleMonth = visibleMonth,
                onSelect = {
                    onDateSelected(it)
                    onDismiss()
                }
            )
        }
    )
}

@Composable
private fun CalendarGrid(
    visibleMonth: YearMonth,
    onSelect: (LocalDate) -> Unit
) {
    val firstDay = visibleMonth.atDay(1)
    val firstIndex = (firstDay.dayOfWeek.value + 6) % 7
    val days = visibleMonth.lengthOfMonth()
    val rows = ((firstIndex + days) + 6) / 7

    Column {

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf(
                stringResource(R.string.day_mo),
                stringResource(R.string.day_tu),
                stringResource(R.string.day_we),
                stringResource(R.string.day_th),
                stringResource(R.string.day_fr),
                stringResource(R.string.day_sa),
                stringResource(R.string.day_su)
            ).forEach {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.width(32.dp)
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        var day = 1

        repeat(rows) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                repeat(7) { i ->
                    val index = it * 7 + i

                    if (index < firstIndex || day > days) {
                        Box(Modifier.size(32.dp))
                    } else {
                        val date = visibleMonth.atDay(day)

                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .clickable { onSelect(date) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = day.toString(),
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        day++
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}
