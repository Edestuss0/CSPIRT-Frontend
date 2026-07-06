package com.cpirt.app.features.user.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cpirt.app.domain.user.entity.Complaint
import com.cpirt.app.domain.user.entity.UserRole
import com.cpirt.app.ui.components.cards.ComplaintCard
import com.cpirt.app.ui.theme.AppScaffold
import com.cpirt.app.ui.theme.EmptyState
import com.cpirt.app.ui.theme.PrimaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserComplaintsScreen(
    complaints: List<Complaint>,
    onBackClick: () -> Unit,
    role: UserRole,
    onDelete: (id: Int) -> Unit
) {
    AppScaffold(
        modifier = Modifier.fillMaxSize(),
        onBackClick = onBackClick,
        hasBackButton = true
    ) {innerPadding ->

        var modalOpen by remember { mutableStateOf(false) }
        var id by remember { mutableStateOf<Int?>(null) }

        if (modalOpen && id != null) {
            AlertDialog(
                onDismissRequest = {modalOpen = false},
                title = {Text(
                    text = "Удаление заметки",
                    style = MaterialTheme.typography.titleLarge
                )},
                text = {Text(
                    text = "Вы уверены, что хотите удалить заметку? Это действие нельзя отменить",
                    style = MaterialTheme.typography.bodyLarge
                )},
                confirmButton = {
                    PrimaryButton(
                        text = "Удалить",
                        onClick = {
                            onDelete(id!!)
                            modalOpen = false
                            id = null
                        }
                    )
                },
                dismissButton = {
                    PrimaryButton(
                        text = "Отмена",
                        onClick = {
                            modalOpen = false
                            id = null
                        }
                    )
                }
            )
        }

        if (complaints.isEmpty()) {

            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                EmptyState(text = "Жалобы о пользователе не найдены")
            }

            return@AppScaffold
        }


        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 16.dp)
        ) {
            items(items = complaints) { complaint ->
                ComplaintCard(
                    complaint = complaint,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    role = role,
                    onDelete = {
                        id = complaint.id
                        modalOpen = true
                    }
                )
            }
        }
    }
}
