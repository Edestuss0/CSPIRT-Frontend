package com.cpirt.app.ui.components.cards

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import com.cpirt.app.core.utils.toDate
import com.cpirt.app.domain.user.entity.Complaint
import com.cpirt.app.domain.user.entity.UserRole
import com.cpirt.app.ui.theme.AppBadge
import com.cpirt.app.ui.theme.AppCard

@Composable
fun ComplaintCard(
    modifier: Modifier = Modifier,
    complaint: Complaint,
    isMe: Boolean = false,
    role: UserRole,
    onDelete: (id: Int) -> Unit
) {
    var isModalOpen by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxWidth()) {
        AppCard(
            modifier = Modifier.fillMaxWidth().combinedClickable(
                    onLongClick = { if (role == UserRole.Owner || role == UserRole.Admin) { isModalOpen = true } },
                    onClick = {}
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (!isMe) {
                            Text(
                                text = "От ${complaint.authorName}",
                                style = MaterialTheme.typography.titleSmall
                            )
                        }

                        AppBadge(text = complaint.createdAt.toDate() ?: "Неизвестно")
                    }

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = complaint.content,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        DropdownMenu(
            expanded = isModalOpen,
            onDismissRequest = {isModalOpen = false},
            shape = MaterialTheme.shapes.large
        ) {
            DropdownMenuItem(
                text = {Text(text = "Удалить жалобу")},
                onClick = {onDelete(complaint.id)}
            )
        }
    }
}
