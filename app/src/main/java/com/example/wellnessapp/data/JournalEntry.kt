package com.example.wellnessapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journal_entries")
data class JournalEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,

    val date: String,
    val time: String,
    val type: String = "note",
    val title: String? = null,
    val result: String? = null,
    val score: Int? = null,
    val text: String? = null
)
