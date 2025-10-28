package com.example.calendarapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import com.example.calendarapp.data.Event
import com.example.calendarapp.data.EventRepository
import com.example.calendarapp.ui.CalendarTheme
import com.example.calendarapp.util.IcsExporter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : ComponentActivity() {
    private lateinit var repo: EventRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repo = EventRepository(applicationContext)

        setContent {
            CalendarTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    CalendarScreen(repo)
                }
            }
        }
    }
}

@Composable
fun CalendarScreen(repo: EventRepository) {
    val eventsState = remember { mutableStateListOf<Event>() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        repo.getAll().collect { list ->
            eventsState.clear()
            eventsState.addAll(list)
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Calendar App") }) },
        floatingActionButton = {
            Column {
                FloatingActionButton(onClick = {
                    // add sample event
                    scope.launch {
                        val now = System.currentTimeMillis()
                        val evt = Event(
                            title = "New Event",
                            description = "Sample event",
                            startMillis = now + 60 * 60 * 1000,
                            endMillis = now + 2 * 60 * 60 * 1000
                        )
                        repo.insert(evt)
                    }
                }) { Icon(Icons.Default.Add, contentDescription = "Add") }

                Spacer(modifier = Modifier.height(8.dp))

                FloatingActionButton(onClick = {
                    // export
                    scope.launch {
                        val events = repo.getBetween(0, Long.MAX_VALUE)
                        if (events.isNotEmpty()) {
                            try {
                                val file = IcsExporter.exportEventsToIcs(context, events, "calendar_export_${System.currentTimeMillis()}.ics")
                                val uri: Uri = FileProvider.getUriForFile(context, "com.example.calendarapp.fileprovider", file)
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/calendar"
                                    putExtra(Intent.EXTRA_STREAM, uri)
                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                }
                                // If context is not an Activity, add NEW_TASK to avoid crash
                                if (context !is android.app.Activity) shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                context.startActivity(Intent.createChooser(shareIntent, "Share calendar"))
                            } catch (t: Throwable) {
                                // swallow for now; in real app show snackbar/toast
                                t.printStackTrace()
                            }
                        }
                    }
                }) { Icon(Icons.Default.Share, contentDescription = "Export") }
            }
        }
    ) { padding ->
        if (eventsState.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No events yet. Tap + to add a sample event.")
            }
        } else {
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)) {
                items(eventsState) { e ->
                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(e.title, style = MaterialTheme.typography.h6)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(Date(e.startMillis).toString(), style = MaterialTheme.typography.body2)
                        }
                    }
                }
            }
        }
    }
}
