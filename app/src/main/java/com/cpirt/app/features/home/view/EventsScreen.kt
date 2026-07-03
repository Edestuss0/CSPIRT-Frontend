package com.cpirt.app.features.home.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp
import com.cpirt.app.domain.events.entity.Event
import com.cpirt.app.features.event.view.EventHost
import com.cpirt.app.ui.components.cards.EventCard
import com.cpirt.app.ui.theme.AppScaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(
    events: List<Event>,
    onBackClick: () -> Unit
) {
    var selectedEvent by remember { mutableStateOf<Event?>(null) }

    AppScaffold(
        modifier = Modifier.fillMaxSize(),
        hasBackButton = true,
        onBackClick = onBackClick,
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp).blur(if (selectedEvent == null) 0.dp else 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items = events) { event ->
                EventCard(
                    event = event,
                    isActive = false,
                    onEventClick = {selectedEvent = event}
                )
            }
        }

        EventHost(
            event = selectedEvent,
            onDismiss = { selectedEvent = null }
        )
    }
}