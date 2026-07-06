package com.cpirt.app.features.event.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cpirt.app.domain.classes.entity.SchoolClass
import com.cpirt.app.features.event.viewmodel.EventPlayersState
import com.cpirt.app.features.event.viewmodel.EventViewModel
import com.cpirt.app.ui.theme.PrimaryButton

@Composable
fun EventPlayersView(
    schoolClass: SchoolClass,
    playersState: EventPlayersState,
    viewModel: EventViewModel,
    onSubmit: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text(
            text = "Участники от ${schoolClass.grade}${schoolClass.letter} класса",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(16.dp))
        PrimaryButton(
            text = "Сохранить",
            onClick = {
                viewModel.submitPlayersChanges()
                onSubmit()
            }
        )
        Spacer(Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items = schoolClass.members) {member ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Checkbox(
                        checked = if (playersState.current.contains(member.id)) {
                            !playersState.toRemove.contains(member.id)
                        } else {
                            playersState.toAdd.contains(member.id)
                        },
                        onCheckedChange = { viewModel.onPlayerToggle(member.id) }
                    )
                    Text(
                        text = member.fullname,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}