package com.cpirt.app.features.user.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cpirt.app.core.utils.toImageBitmap
import com.cpirt.app.entities.FullNameSchema
import com.cpirt.app.entities.UserAvatarInfo
import com.cpirt.app.entities.UserInfo
import com.cpirt.app.entities.UserPersonalInfo
import com.cpirt.app.entities.UserRole
import com.cpirt.app.entities.toDisplayName
import com.cpirt.app.features.user.viewmodel.UserState
import com.cpirt.app.features.user.viewmodel.UserViewModel
import com.cpirt.app.ui.components.AppSnackbarHost

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    viewModel: UserViewModel,
    state: UserState,
    onNotesClick: () -> Unit,
    onComplaintsClick: () -> Unit,
    onBackClick: () -> Unit,
) {

    val ratingSheetState = rememberModalBottomSheetState()
    val addNoteSheetState = rememberModalBottomSheetState()

    LaunchedEffect(state.addNoteState.show) {
        if (state.addNoteState.show) {
            addNoteSheetState.show()
        } else {
            addNoteSheetState.hide()
        }
    }

    LaunchedEffect(state.changeRatingState.show) {
        if (state.changeRatingState.show) {
            ratingSheetState.show()
        } else {
            ratingSheetState.hide()
        }
    }

    if (state.changeRatingState.show || ratingSheetState.isVisible) {
        ModalBottomSheet(
            onDismissRequest = {viewModel.onChangeRatingChangeModalVisibility()},
            sheetState = ratingSheetState
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                label = { Text("Дельта изменения рейтинга") },
                shape = RoundedCornerShape(16.dp),
                value = state.changeRatingState.rating,
                onValueChange = { newValue: String ->
                    val isValidInteger = newValue.isEmpty() ||
                            newValue == "-" ||
                            newValue.toIntOrNull() != null
                    if (isValidInteger) {
                        viewModel.onChangeRatingRatingInput(newValue)
                    }
                },
            )
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                label = { Text("Причина изменения рейтинга") },
                shape = RoundedCornerShape(16.dp),
                value = state.changeRatingState.reason,
                onValueChange = { newValue: String ->
                    viewModel.onChangeRatingReasonInput(newValue)
                },
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { viewModel.changeRating() },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Text(text = "Изменить рейтинг", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(16.dp))
        }
    }

    if (state.addNoteState.show || addNoteSheetState.isVisible) {
        ModalBottomSheet(
            onDismissRequest = {viewModel.onChangeAddNoteModalVisibility()},
            sheetState = addNoteSheetState
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                label = { Text("Текст заметки") },
                shape = RoundedCornerShape(16.dp),
                value = state.addNoteState.content,
                onValueChange = { newValue: String ->
                    viewModel.onAddNoteContentInput(newValue)
                },
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { viewModel.addNote() },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Text(text = "Добавить заметку", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(16.dp))
        }
    }

    val snackbarHostState = SnackbarHostState()

    LaunchedEffect(key1 = state.snackbarMessage) {
        state.snackbarMessage?.let {message ->
            snackbarHostState.showSnackbar(message)
            viewModel.onMessageShown()
        }
    }

    Scaffold(
        snackbarHost = { AppSnackbarHost(snackbarHostState ) },
        topBar = {
            TopAppBar(
                title = {
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ChevronLeft,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) {innerPadding ->
        UserContent(
            innerPadding = innerPadding,
            userInfo = state.userInfo,
            profileInfo = state.profileInfo,
            onNotesClick = onNotesClick,
            onComplaintsClick = onComplaintsClick,
            onChangeRatingClick = {viewModel.onChangeRatingChangeModalVisibility()},
            onAddNoteClick = {viewModel.onChangeAddNoteModalVisibility()}
        )
    }
}

@Composable
private fun UserContent(
    innerPadding: PaddingValues,
    userInfo: UserInfo?,
    profileInfo: UserInfo?,
    onNotesClick: () -> Unit,
    onComplaintsClick: () -> Unit,
    onChangeRatingClick: () -> Unit,
    onAddNoteClick: () -> Unit
) {
    if (userInfo == null || profileInfo == null) {
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
                    text = "Не удалось получить информацию о пользователе",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
            }
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        item {
            UserHeader(userInfo.user)
        }

        item {
            RatingCard(userInfo.user.rating)
        }

        item {
            UserInfoCard(userInfo.user)
        }

        item {
            UserTransitions(
                role = profileInfo.user.role,
                onNotesClick = onNotesClick,
                onComplaintsClick = onComplaintsClick
            )
        }

        item {
            UserActions(
                role = profileInfo.user.role,
                onChangeRatingClick = onChangeRatingClick,
                onAddNoteClick = onAddNoteClick
            )
        }
    }
}

@Composable
private fun UserHeader(
    user: UserPersonalInfo
) {
    val avatar = remember(user.avatar.string) {
        user.avatar.string.toImageBitmap()
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
            Card(
                shape = CircleShape,
                modifier = Modifier.size(96.dp)
            ) {
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
            text = "${user.name} ${user.lastName}",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun RatingCard(
    rating: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp)
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
private fun UserInfoRow(
    icon: ImageVector,
    title: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
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
private fun UserInfoCard(
    user: UserPersonalInfo
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp)
    ) {

        UserInfoRow(
            icon = Icons.Default.School,
            title = "Класс",
            value = user.className
        )

        HorizontalDivider()

        UserInfoRow(
            icon = Icons.Default.AlternateEmail,
            title = "Логин",
            value = user.login
        )

        HorizontalDivider()

        UserInfoRow(
            icon = Icons.Default.Person,
            title = "Роль",
            value = user.role.toDisplayName()
        )
    }
}

@Composable
private fun UserTransitions(
    role: UserRole,
    onNotesClick: () -> Unit,
    onComplaintsClick: () -> Unit
) {
    if (role == UserRole.User) return

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp)
    ) {
        if (role == UserRole.Helper || role == UserRole.Admin || role == UserRole.Owner) {
            UserTransiteButton(
                title = "Список заметок",
                onClick = onNotesClick
            )
            HorizontalDivider()
        }
        if (role == UserRole.Admin || role == UserRole.Owner) {
            UserTransiteButton(
                title = "Список жалоб",
                onClick = onComplaintsClick
            )
            HorizontalDivider()
        }
    }
}

@Composable
private fun UserActions(
    role: UserRole,
    onChangeRatingClick: () -> Unit,
    onAddNoteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp)
    ) {
        UserActionButton(
            title = "Написать жалобу",
            onClick = {}
        )
        if (role != UserRole.User) {
            HorizontalDivider()
            UserActionButton(
                title = "Написать заметку",
                onClick = {onAddNoteClick()}
            )
        }
        if (role != UserRole.User && role != UserRole.Helper) {
            HorizontalDivider()
            UserActionButton(
                title = "Изменить рейтинг",
                onClick = {onChangeRatingClick()}
            )
        }
    }
}

@Composable
private fun UserActionButton(
    title: String,
    onClick: () -> Unit
) {
    TextButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Composable
private fun UserTransiteButton(
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth().padding(vertical = 8.dp, horizontal = 16.dp).clip(CircleShape).clickable{onClick()},
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(Modifier.width(16.dp))

        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Box(
                modifier = Modifier.size(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null
                )
            }
        }
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
            avatar = UserAvatarInfo(valid = false, string = ""),
            fullName = listOf(FullNameSchema(name = "Veronika", lastName = "Traktaristka")),
            login = "traktar",
            rating = 4251,
            role = UserRole.User,
            className = "3A",
            classId = 1488,
        ),
        events = emptyList(),
        notes = emptyList(),
        complaints = emptyList(),
        classTeacher = null
    )

    val profileInfo = UserInfo(
        user = UserPersonalInfo(
            id = 3,
            name = "Margasisa",
            lastName = "hachatryan",
            avatar = UserAvatarInfo(valid = false, string = ""),
            fullName = listOf(FullNameSchema(name = "margasisa", lastName = "fdsfds")),
            login = "margas",
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


    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        UserContent(
            innerPadding = innerPadding,
            userInfo = userInfo,
            profileInfo = profileInfo,
            onNotesClick = {},
            onComplaintsClick = {},
            onChangeRatingClick = {},
            onAddNoteClick = {}
        )
    }
}