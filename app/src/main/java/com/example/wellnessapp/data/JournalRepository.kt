package com.example.wellnessapp.data

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
class JournalRepository(
    private val dao: JournalDao
) {
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    fun entriesForDate(date: LocalDate): Flow<List<JournalEntry>> {
        val dateString = date.format(dateFormatter)
        return dao.entriesForDate(dateString)
    }
    suspend fun addNote(
        text: String,
        date: LocalDate
    ) {
        val nowTime = LocalTime.now()
        val entry = JournalEntry(
            date = date.format(dateFormatter),
            time = nowTime.format(timeFormatter),
            type = "note",
            text = text
        )
        dao.insert(entry)
    }
    suspend fun addSurvey(
        title: String,
        result: String,
        score: Int?,
        date: LocalDate
    ) {
        val nowTime = LocalTime.now()
        val entry = JournalEntry(
            date = date.format(dateFormatter),
            time = nowTime.format(timeFormatter),
            type = "survey",
            title = title,
            result = result,
            score = score,
            text = null
        )
        dao.insert(entry)
    }
}