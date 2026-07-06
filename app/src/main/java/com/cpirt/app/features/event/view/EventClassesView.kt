package com.cpirt.app.features.event.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cpirt.app.domain.classes.entity.SchoolClass
import com.cpirt.app.ui.components.cards.ClassCard
import com.cpirt.app.ui.theme.EmptyState

@Composable
fun EventClassesView(
    classes: List<SchoolClass>?,
    onClassClick: (item: SchoolClass) -> Unit
) {
    if (classes?.isEmpty() == true || classes == null) {
        Column(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.9f)) {
            EmptyState("Классы, учавствующие в мероприятии не найдены")
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Учавствующие классы:",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        items(items = classes) { item ->
            ClassCard(
                schoolClass = item,
                onClick = {onClassClick(item)}
            )
        }
    }
}