package com.cpirt.app.ui.components.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cpirt.app.domain.user.entity.UserPersonalInfo
import com.cpirt.app.ui.components.cards.UserCard
import com.cpirt.app.ui.theme.EmptyState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassUsersScreen(
    users: List<UserPersonalInfo>,
    onUserClick: (id: Int) -> Unit,
    innerPadding: PaddingValues
) {
    if (users.isEmpty()) {

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EmptyState(text = "Не удалось получить информацию о классе")
        }

        return
    }


    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),

    ) {
        items(items = users) { user ->
            UserCard(user = user, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), onUserClick = {onUserClick(user.id)})
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MyClassUsersScreenPreview() {
    ClassUsersScreen(
        users = emptyList(),
        onUserClick = {},
        innerPadding = PaddingValues()
    )
}
