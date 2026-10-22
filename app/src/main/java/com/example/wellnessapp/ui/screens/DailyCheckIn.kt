package com.example.wellnessapp.ui.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.wellnessapp.R
import com.example.wellnessapp.viewmodel.JournalViewModel

private data class DailyQuestion(
    val id: Int,
    @StringRes val textRes: Int,
    val reversed: Boolean = false
)

enum class WellbeingResult {
    GOOD, OKAY, BAD
}

private val questions = listOf(
    DailyQuestion(1, R.string.q1),
    DailyQuestion(2, R.string.q2),
    DailyQuestion(3, R.string.q3),
    DailyQuestion(4, R.string.q4, reversed = true),
    DailyQuestion(5, R.string.q5, reversed = true),
    DailyQuestion(6, R.string.q6),
    DailyQuestion(7, R.string.q7),
    DailyQuestion(8, R.string.q8),
    DailyQuestion(9, R.string.q9),
    DailyQuestion(10, R.string.q10)
)


class DailyCheckInViewModel : ViewModel() {

    var index by mutableStateOf(0)
        private set

    var finished by mutableStateOf(false)
        private set

    private val answers = IntArray(questions.size) { -1 }

    fun answer(score: Int) {
        answers[index] = score
        if (index < questions.lastIndex) {
            index++
        } else {
            finished = true
        }
    }

    fun average(): Double =
        questions.mapIndexed { i, q ->
            val s = answers[i]
            if (q.reversed) 4 - s else s
        }.average()

    fun result(): WellbeingResult =
        when {
            average() >= 2.8 -> WellbeingResult.GOOD
            average() >= 1.5 -> WellbeingResult.OKAY
            else -> WellbeingResult.BAD
        }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyCheckInScreen(
    navController: NavController,
    journalViewModel: JournalViewModel,
    vm: DailyCheckInViewModel = viewModel()
) {

    val goodTitle = stringResource(R.string.result_good_title)
    val goodDesc = stringResource(R.string.result_good_desc)

    val okayTitle = stringResource(R.string.result_moderate_title)
    val okayDesc = stringResource(R.string.result_moderate_desc)

    val badTitle = stringResource(R.string.result_low_title)
    val badDesc = stringResource(R.string.result_low_desc)

    if (vm.finished) {
        LaunchedEffect(Unit) {
            val avg = vm.average()

            when (vm.result()) {
                WellbeingResult.GOOD -> {
                    journalViewModel.addDailyCheckIn(
                        title = goodTitle,
                        description = goodDesc,
                        score = avg
                    )
                    navController.navigate("daily_good") {
                        popUpTo("daily_checkin") { inclusive = true }
                    }
                }

                WellbeingResult.OKAY -> {
                    journalViewModel.addDailyCheckIn(
                        title = okayTitle,
                        description = okayDesc,
                        score = avg
                    )
                    navController.navigate("daily_ok") {
                        popUpTo("daily_checkin") { inclusive = true }
                    }
                }

                WellbeingResult.BAD -> {
                    journalViewModel.addDailyCheckIn(
                        title = badTitle,
                        description = badDesc,
                        score = avg
                    )
                    navController.navigate("daily_bad") {
                        popUpTo("daily_checkin") { inclusive = true }
                    }
                }
            }
        }
        return
    }

    val question = questions[vm.index]

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.daily_check_in_title)) }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Text(
                text = stringResource(
                    R.string.question_progress,
                    vm.index + 1,
                    questions.size
                ),
                style = MaterialTheme.typography.labelMedium
            )

            LinearProgressIndicator(
                progress = (vm.index + 1f) / questions.size,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = stringResource(question.textRes),
                style = MaterialTheme.typography.titleLarge
            )

            LikertAnswers { score ->
                vm.answer(score)
            }
        }
    }
}

@Composable
private fun LikertAnswers(
    onSelect: (Int) -> Unit
) {
    val answers = listOf(
        R.string.answer_0,
        R.string.answer_1,
        R.string.answer_2,
        R.string.answer_3,
        R.string.answer_4
    )

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        answers.forEachIndexed { index, res ->
            OutlinedButton(
                onClick = { onSelect(index) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(res))
            }
        }
    }
}
