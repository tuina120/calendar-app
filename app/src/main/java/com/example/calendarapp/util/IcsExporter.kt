package com.example.calendarapp.util

import android.content.Context
import com.example.calendarapp.data.Event
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

object IcsExporter {
    private val ICS_DATE = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    fun exportEventsToIcs(context: Context, events: List<Event>, filename: String = "calendar_export.ics"): File {
        val file = File(context.cacheDir, filename)
        FileOutputStream(file).use { out ->
            out.write("BEGIN:VCALENDAR\r\n".toByteArray())
            out.write("VERSION:2.0\r\n".toByteArray())
            out.write("PRODID:-//Example CalendarApp//EN\r\n".toByteArray())

            events.forEach { e ->
                out.write("BEGIN:VEVENT\r\n".toByteArray())
                val uid = "event-${e.id}@calendarapp"
                out.write("UID:$uid\r\n".toByteArray())

                val dtStart = ICS_DATE.format(Date(e.startMillis))
                val dtEnd = ICS_DATE.format(Date(e.endMillis))
                out.write("DTSTART:$dtStart\r\n".toByteArray())
                out.write("DTEND:$dtEnd\r\n".toByteArray())

                out.write("SUMMARY:${escapeText(e.title)}\r\n".toByteArray())
                e.description?.let { out.write("DESCRIPTION:${escapeText(it)}\r\n".toByteArray()) }
                out.write("END:VEVENT\r\n".toByteArray())
            }

            out.write("END:VCALENDAR\r\n".toByteArray())
        }
        return file
    }

    private fun escapeText(input: String): String {
        return input.replace("\\", "\\\\").replace("\n", "\\n").replace(",", "\\,")
    }
}
