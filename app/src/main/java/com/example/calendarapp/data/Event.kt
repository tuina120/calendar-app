package com.example.calendarapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String? = null,
    val startMillis: Long,
    val endMillis: Long,
    val allDay: Boolean = false,
    val timezone: String? = null,
    val rrule: String? = null
)
