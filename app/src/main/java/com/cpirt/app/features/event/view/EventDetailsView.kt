package com.cpirt.app.features.event.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cpirt.app.domain.events.entity.Event
import com.cpirt.app.domain.events.entity.toLabel
import com.cpirt.app.ui.theme.AppCard
import com.cpirt.app.ui.theme.PrimaryButton

@Composable
fun EventDetailsView(
    event: Event,
    onClassesClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.9f).padding(16.dp)
    ) {
        AppCard {
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = event.description,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(16.dp))
                InfoRow(label = "Статус:", value = event.status.toLabel())
                InfoRow(label = "Награда за участие:", value = event.ratingReward.toString())
                InfoRow(label = "Начало:", value = event.startedAt)
                InfoRow(label = "Классов учавствует:", value = event.classes.size.toString())
                InfoRow(label = "Участников:", value = event.players.size.toString())
            }
        }
        Spacer(Modifier.height(16.dp))
        PrimaryButton(
            text = "Список учавствующих классов",
            onClick = onClassesClick
        )
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String
) {
    HorizontalDivider()
    Spacer(Modifier.height(16.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium
        )
    }
    Spacer(Modifier.height(16.dp))
}