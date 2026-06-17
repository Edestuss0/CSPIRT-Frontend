package com.cpirt.app.ui.components.cards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cpirt.app.entities.UserPersonalInfo

@Composable
fun UserCard(
    user: UserPersonalInfo,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text(
                text = "${user.name} ${user.lastName}",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = user.rating.toString(),
                fontSize = 16.sp,
            )
        }
    }
}