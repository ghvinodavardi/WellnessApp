package com.example.wellnessapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao {

    @Query("SELECT * FROM journal_entries WHERE date = :date ORDER BY time DESC")
    fun entriesForDate(date: String): Flow<List<JournalEntry>>

    @Insert
    suspend fun insert(entry: JournalEntry)
}
