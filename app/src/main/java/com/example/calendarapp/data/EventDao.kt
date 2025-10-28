package com.example.calendarapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Query("SELECT * FROM events ORDER BY startMillis ASC")
    fun getAll(): Flow<List<Event>>

    @Query("SELECT * FROM events WHERE startMillis BETWEEN :from AND :to ORDER BY startMillis ASC")
    suspend fun getBetween(from: Long, to: Long): List<Event>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: Event): Long

    @Update
    suspend fun update(event: Event)

    @Delete
    suspend fun delete(event: Event)
}
