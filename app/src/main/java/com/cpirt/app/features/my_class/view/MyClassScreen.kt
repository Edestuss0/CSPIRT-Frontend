package com.cpirt.app.features.my_class.view

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.cpirt.app.entities.SchoolClass
import com.cpirt.app.entities.FullNameSchema
import com.cpirt.app.entities.UserAvatarInfo
import com.cpirt.app.entities.UserInfo
import com.cpirt.app.entities.UserPersonalInfo
import com.cpirt.app.entities.UserRole
import com.cpirt.app.features.my_class.viewmodel.MyClassState
import com.cpirt.app.features.my_class.viewmodel.MyClassViewModel
import com.cpirt.app.ui.components.AppSnackbarHost
import com.cpirt.app.ui.components.LoadingScreen

@Composable
fun MyClassScreen(
    viewModel: MyClassViewModel = hiltViewModel(),
    state: MyClassState,
    onUsersClick: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

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
        MyClassContent(
            innerPadding = innerPadding,
            schoolClassInfo = state.schoolClassInfo,
            userInfo = state.userInfo,
            onUsersClick = onUsersClick
        )
    }
}

@Composable
private fun MyClassContent(
    innerPadding: PaddingValues,
    schoolClassInfo: SchoolClass?,
    userInfo: UserInfo?,
    onUsersClick: () -> Unit
) {

    val scrollState = rememberScrollState()

    if (schoolClassInfo == null || userInfo == null) {
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
                    text = "Не удалось получить информацию о классе",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
            }
        }
        return
    }

    val meInList = buildList {
        val sortedMembers = schoolClassInfo.members.sortedByDescending { it.rating }
        val index = sortedMembers.indexOfFirst { it.id == userInfo.user.id }

        if (index > 0)
            add(sortedMembers[index - 1])

        add(sortedMembers[index])

        if (index < sortedMembers.lastIndex)
            add(sortedMembers[index + 1])
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState)
    ) {
        Spacer(Modifier.height(48.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)) {
                Text(
                    text = "${schoolClassInfo.grade} ${schoolClassInfo.letter} Класс",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "${schoolClassInfo.members.size} Учеников",
                    fontSize = 16.sp,
                    modifier = Modifier.padding()
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Классный руководитель:",
                    fontSize = 16.sp,
                    modifier = Modifier.padding()
                )
                Text(
                    text = if (schoolClassInfo.teacher != null) "${schoolClassInfo.teacher.name} ${schoolClassInfo.teacher.lastName}" else schoolClassInfo.teacherLogin,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding()
                )
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)) {
                Text(
                    text = "Общий рейтинг класса:",
                    fontSize = 16.sp,
                    modifier = Modifier.padding()
                )
                Text(
                    text = (schoolClassInfo.classTotalRating + schoolClassInfo.userTotalRating).toString(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding()
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)) {
                meInList.forEach {
                    MemberItem(
                        member = it,
                        isMe = it.id == userInfo.user.id
                    )
                }
                TextButton(
                    onClick = {onUsersClick()},
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Все ученики")
                }
            }
        }
        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun MemberItem(
    member: UserPersonalInfo,
    isMe: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${member.name} ${member.lastName}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                )

                if (isMe) {
                    Text(
                        text = "Вы",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            Spacer(Modifier.height(4.dp))

            Text(
                text = member.rating.toString(),
                fontSize = 16.sp,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MyClassContentPreview() {
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
    val user2 = UserInfo(
        user = UserPersonalInfo(
            id = 2,
            name = "Alisa",
            lastName = "Zhidkova",
            avatar = UserAvatarInfo(valid = false, string = ""),
            fullName = listOf(FullNameSchema(name = "Veronika", lastName = "Traktaristka")),
            login = "traktar",
            rating = 124,
            role = UserRole.Owner,
            className = "3A",
            classId = 1488,
        ),
        events = emptyList(),
        notes = emptyList(),
        complaints = emptyList(),
        classTeacher = null
    )
    val user3 = UserInfo(
        user = UserPersonalInfo(
            id = 1,
            name = "Margasisa",
            lastName = "Zhidkova",
            avatar = UserAvatarInfo(valid = false, string = ""),
            fullName = listOf(FullNameSchema(name = "Veronika", lastName = "Traktaristka")),
            login = "traktar",
            rating = 5000,
            role = UserRole.Owner,
            className = "3A",
            classId = 1488,
        ),
        events = emptyList(),
        notes = emptyList(),
        complaints = emptyList(),
        classTeacher = null
    )
    val schoolClassInfo = SchoolClass(
        id = 41,
        name = "16A",
        grade = 16,
        letter = "A",
        firstQuarterComplete = 3,
        secondQuarterComplete = 1,
        thirdQuarterComplete = 0,
        quarterComplete = 7,
        teacherLogin = "Piratka",
        teacher = null,
        members = listOf(userInfo.user, user2.user, user3.user),
        userTotalRating = 1400,
        classTotalRating = 88
    )

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {innerPadding ->
        MyClassContent(
            innerPadding = innerPadding,
            schoolClassInfo = schoolClassInfo,
            userInfo = userInfo,
            onUsersClick = {}
        )
    }
}