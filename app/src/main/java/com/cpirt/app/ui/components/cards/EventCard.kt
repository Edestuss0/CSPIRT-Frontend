package com.cpirt.app.ui.components.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.cpirt.app.domain.events.entity.Event
import com.cpirt.app.domain.events.entity.toLabel
import com.cpirt.app.ui.theme.AppBadge
import com.cpirt.app.ui.theme.AppCard

@Composable
fun EventCard(
    event: Event,
    modifier: Modifier = Modifier,
    onEventClick: () -> Unit = {},
    isActive: Boolean = true
) {
    AppCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onEventClick)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = event.title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = event.description,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (!isActive) {
                AppBadge(text = event.status.toLabel())
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant
            )

            AppBadge(
                text = "${event.classes.size} Классов"
            )
        }
    }
}