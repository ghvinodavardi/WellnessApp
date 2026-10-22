package com.example.wellnessapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.wellnessapp.data.JournalEntry
import com.example.wellnessapp.data.JournalRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate


data class JournalUiState(
    val selectedDate: LocalDate = LocalDate.now(),
    val entries: List<JournalEntry> = emptyList()
)


@OptIn(ExperimentalCoroutinesApi::class)
class JournalViewModel(
    private val repository: JournalRepository
) : ViewModel() {



    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()


    val entries: StateFlow<List<JournalEntry>> =
        _selectedDate
            .flatMapLatest { date ->
                repository.entriesForDate(date)
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )


    val uiState: StateFlow<JournalUiState> =
        combine(
            selectedDate,
            entries
        ) { date, entries ->
            JournalUiState(
                selectedDate = date,
                entries = entries
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            JournalUiState()
        )


    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun addNote(text: String) {
        val date = _selectedDate.value
        viewModelScope.launch {
            repository.addNote(
                text = text,
                date = date
            )
        }
    }

    fun addDailyCheckIn(
        title: String,
        description: String,
        score: Double
    ) {
        val date = _selectedDate.value
        viewModelScope.launch {
            repository.addSurvey(
                title = title,
                result = description,
                score = score.toInt(),
                date = date
            )
        }
    }

    fun addSurveyResult(
        result: String,
        score: Int?,
        title: String = "Daily Survey"
    ) {
        val date = _selectedDate.value
        viewModelScope.launch {
            repository.addSurvey(
                title = title,
                result = result,
                score = score,
                date = date
            )
        }
    }


    class Factory(
        private val repository: JournalRepository
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return JournalViewModel(repository) as T
        }
    }
}
