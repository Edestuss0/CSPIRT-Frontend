package com.cpirt.app.ui.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cpirt.app.core.utils.toDate
import com.cpirt.app.domain.user.entity.Complaint
import com.cpirt.app.ui.theme.AppBadge
import com.cpirt.app.ui.theme.AppCard

@Composable
fun ComplaintCard(
    modifier: Modifier = Modifier,
    complaint: Complaint,
    isMe: Boolean = false
) {
    AppCard(modifier = modifier.fillMaxWidth()) {
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
}
