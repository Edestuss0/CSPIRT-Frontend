package com.cpirt.app.ui.components.cards

import androidx.compose.foundation.clickable
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
import com.cpirt.app.domain.user.entity.UserPersonalInfo
import com.cpirt.app.domain.user.entity.toDisplayName
import com.cpirt.app.ui.theme.AppBadge
import com.cpirt.app.ui.theme.AppCard

@Composable
fun UserCard(
    user: UserPersonalInfo,
    modifier: Modifier = Modifier,
    onUserClick: () -> Unit = {}
) {
    AppCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onUserClick() }
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
                Text(
                    text = "${user.name} ${user.lastName}",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = user.role.toDisplayName(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            AppBadge(text = "Рейтинг: ${user.rating}")
        }
    }
}
