package com.cpirt.app.features.home.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp
import com.cpirt.app.domain.events.entity.Event
import com.cpirt.app.domain.user.entity.UserRole
import com.cpirt.app.features.event.view.EventHost
import com.cpirt.app.features.home.viewmodel.HomeViewModel
import com.cpirt.app.ui.components.cards.EventCard
import com.cpirt.app.ui.theme.AppScaffold
import com.cpirt.app.ui.theme.PrimaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(
    events: List<Event>,
    onBackClick: () -> Unit,
    viewmodel: HomeViewModel,
    role: UserRole
) {
    var selectedEvent by remember { mutableStateOf<Event?>(null) }
    var event by remember { mutableStateOf<Event?>(null) }
    var isDeleteEventModalOpen by remember { mutableStateOf(false) }

    if (isDeleteEventModalOpen && event != null) {
        AlertDialog(
            onDismissRequest = {
                isDeleteEventModalOpen = false
                event = null
            },
            title = {Text(
                text = "Удаление мероприятия",
                style = MaterialTheme.typography.titleLarge
            )},
            text = {Text(
                text = "Вы уверены, что хотите удалить мероприятие? Это действие нельзя отменить",
                style = MaterialTheme.typography.bodyLarge
            )},
            confirmButton = {
                PrimaryButton(
                    text = "Удалить",
                    onClick = {
                        viewmodel.deleteEvent(event!!.id)
                        isDeleteEventModalOpen = false
                        event = null
                    }
                )
            },
            dismissButton = {
                PrimaryButton(
                    text = "Отмена",
                    onClick = {
                        isDeleteEventModalOpen = false
                        event = null
                    }
                )
            }
        )
    }

    AppScaffold(
        modifier = Modifier.fillMaxSize(),
        hasBackButton = true,
        onBackClick = onBackClick,
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .blur(if (selectedEvent == null) 0.dp else 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items = events) { item ->
                EventCard(
                    event = item,
                    isActive = false,
                    onEventClick = {selectedEvent = item},
                    onDelete = {
                        event = item
                        isDeleteEventModalOpen = true
                    },
                    onComplete = {},
                    role = role
                )
            }
        }

        EventHost(
            event = selectedEvent,
            onDismiss = { selectedEvent = null }
        )
    }
}