package com.cpirt.app.features.profile.ui

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.cpirt.app.entities.FullNameSchema
import com.cpirt.app.entities.UserAvatarInfo
import com.cpirt.app.entities.UserInfo
import com.cpirt.app.entities.UserPersonalInfo
import com.cpirt.app.entities.UserRole
import com.cpirt.app.entities.toDisplayName
import com.cpirt.app.features.profile.viewmodel.ProfileViewModel
import com.cpirt.app.ui.components.AppSnackbarHost
import com.cpirt.app.ui.components.LoadingScreen

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

    if (state.isLoading) {
        LoadingScreen()
        return
    }

    Scaffold(
        snackbarHost = { AppSnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) {innerPadding ->
        ProfileContent(
            innerPadding = innerPadding,
            userInfo = state.userInfo
        )
    }
}

@Composable
private fun ProfileContent(
    innerPadding: PaddingValues = PaddingValues(),
    userInfo: UserInfo?
) {
    if (userInfo == null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Не удалось получить информацию об аккаунте",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
            }
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(innerPadding)
            .padding(horizontal = 16.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(48.dp))
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            modifier = Modifier
                .size(72.dp)
                .padding(16.dp)
        )
        Text(
            text = "${userInfo.user.name} ${userInfo.user.lastName}",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(48.dp))
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Рейтинг:",
                        fontSize = 24.sp,
                    )
                    Text(text = userInfo.user.rating.toString(), fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                    Text(
                        text = "Показатель поведения, активности и вклада в школьную жизнь",
                        fontSize = 16.sp,
                    )
                }
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            ProfileValueCard(
                icon = Icons.Default.School,
                valueName = "Класс:",
                value = userInfo.user.className
            )
            Divider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = Color.White.copy(alpha = 0.5f),
            )
            ProfileValueCard(
                icon = Icons.Default.AlternateEmail,
                valueName = "Логин:",
                value = userInfo.user.login
            )
            Divider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = Color.White.copy(alpha = 0.5f),
            )
            ProfileValueCard(
                icon = Icons.Default.Person,
                valueName = "Роль:",
                value = userInfo.user.role.toDisplayName()
            )
        }
    }
}

@Composable
private fun ProfileValueCard(
    modifier: Modifier = Modifier,
    valueName: String,
    value: String,
    icon: ImageVector? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row() {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                )
            }
            Text(
                text = valueName,
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
        Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileValueCardPreview() {
    ProfileValueCard(valueName = "Ваш рейтинг", value =  "2313")
}

@Preview
@Composable
private fun ProfileContentPreview() {
    val userInfo = UserInfo(
        user = UserPersonalInfo(
            id = 3,
            name = "Veronika",
            lastName = "Traktaristka",
            avatar = UserAvatarInfo(valid = false, string = ""),
            fullName = listOf(FullNameSchema(name = "Veronika", lastName = "Traktaristka")),
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


    Scaffold(modifier = Modifier.fillMaxSize()) {innerPadding ->
        ProfileContent(
            innerPadding = innerPadding,
            userInfo = userInfo
        )
    }
}