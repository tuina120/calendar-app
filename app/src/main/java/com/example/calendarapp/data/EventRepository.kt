package com.example.calendarapp.data

import android.content.Context
import kotlinx.coroutines.flow.Flow

class EventRepository(context: Context) {
    private val db = CalendarDatabase.getDatabase(context)
    private val dao = db.eventDao()

    fun getAll(): Flow<List<Event>> = dao.getAll()

    suspend fun insert(event: Event): Long = dao.insert(event)
    suspend fun update(event: Event) = dao.update(event)
    suspend fun delete(event: Event) = dao.delete(event)

    suspend fun getBetween(from: Long, to: Long): List<Event> = dao.getBetween(from, to)
}
