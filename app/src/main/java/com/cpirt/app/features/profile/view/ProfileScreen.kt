package com.cpirt.app.features.profile.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.cpirt.app.core.utils.toImageBitmap
import com.cpirt.app.domain.user.entity.UserInfo
import com.cpirt.app.domain.user.entity.UserPersonalInfo
import com.cpirt.app.domain.user.entity.UserRole
import com.cpirt.app.domain.user.entity.toDisplayName
import com.cpirt.app.features.profile.viewmodel.ProfileViewModel
import com.cpirt.app.ui.components.snackbar.AppSnackbarHost
import com.cpirt.app.ui.components.screens.LoadingScreen
import com.cpirt.app.ui.theme.AppCard
import com.cpirt.app.ui.theme.AppScaffold
import com.cpirt.app.ui.theme.EmptyState

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = state.snackbarMessage) {
        state.snackbarMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.onMessageShown()
        }
    }

    AppScaffold(
        snackbarHost = { AppSnackbarHost(snackbarHostState) },
            modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        when {
            state.isLoading && state.userInfo == null -> LoadingScreen()
            else -> {
                PullToRefreshBox(
                    onRefresh = { viewModel.loadData(true) },
                    isRefreshing = state.isLoading
                ) {
                    ProfileContent(
                        innerPadding = innerPadding,
                        userInfo = state.userInfo,
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileContent(
    innerPadding: PaddingValues,
    userInfo: UserInfo?,
) {
    if (userInfo == null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            EmptyState(text = "Не удалось получить информацию об аккаунте")
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(innerPadding),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            ProfileHeader(userInfo.user)
        }

        item {
            RatingCard(userInfo.user.rating)
        }

        item {
            ProfileInfoCard(userInfo.user)
        }
    }
}

@Composable
private fun ProfileHeader(
    user: UserPersonalInfo
) {
    val avatar = remember(user.avatar) {
        user.avatar?.toImageBitmap()
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (avatar != null) {
            Image(
                bitmap = avatar,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
            )
        } else {
            AppCard(modifier = Modifier.size(96.dp)) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = user.fullname,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun RatingCard(
    rating: Int
) {
    AppCard(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "Рейтинг",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = rating.toString(),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Показатель поведения, активности и вклада в школьную жизнь",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ProfileInfoRow(
    icon: ImageVector,
    title: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Box(
                modifier = Modifier.size(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null
                )
            }
        }

        Spacer(Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
private fun ProfileInfoCard(
    user: UserPersonalInfo
) {
    AppCard(
        modifier = Modifier.fillMaxWidth(),
    ) {

        ProfileInfoRow(
            icon = Icons.Default.School,
            title = "Класс",
            value = user.className
        )

        HorizontalDivider()

        ProfileInfoRow(
            icon = Icons.Default.AlternateEmail,
            title = "Логин",
            value = user.login
        )

        HorizontalDivider()

        ProfileInfoRow(
            icon = Icons.Default.Person,
            title = "Роль",
            value = user.role.toDisplayName()
        )
    }
}



@Preview
@Composable
private fun ProfileContentPreview() {
    val userInfo = UserInfo(
        user = UserPersonalInfo(
            id = 3,
            name = "Veronika",
            lastName = "Traktaristka",
            avatar = null,
            login = "traktar",
            rating = 4251,
            role = UserRole.Owner,
            className = "3A",
            classId = 1488,
        ),
        events = emptyList(),
        notes = emptyList(),
        complaints = emptyList(),
        classTeacher = null
    )


    AppScaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        ProfileContent(
            innerPadding = innerPadding,
            userInfo = userInfo,
        )
    }
}
