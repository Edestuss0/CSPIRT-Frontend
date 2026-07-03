package com.cpirt.app.features.class_details.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cpirt.app.domain.classes.entity.SchoolClass
import com.cpirt.app.domain.user.entity.UserPersonalInfo
import com.cpirt.app.features.class_details.viewmodel.ClassDetailState
import com.cpirt.app.ui.theme.AppBadge
import com.cpirt.app.ui.theme.AppCard
import com.cpirt.app.ui.theme.EmptyState
import com.cpirt.app.ui.theme.PrimaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassDetailsSheet(
    state: ClassDetailState,
    onUsersClick: () -> Unit,
    onScheduleClick: () -> Unit
) {
    val classData = state.classData ?: return

    Column(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.9f).padding(16.dp)
    ) {
        ClassHeader(classData)
        Spacer(Modifier.height(16.dp))
        ClassTop(classData, onUsersClick)
        Spacer(Modifier.height(16.dp))
        PrimaryButton(
            text = "К расписанию",
            onClick = onScheduleClick,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
@Composable
private fun MemberItem(
    member: UserPersonalInfo,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${member.name} ${member.lastName}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(4.dp))

        AppBadge(text = "Рейтинг: ${member.rating}")
    }
}

@Composable
private fun ClassHeader(
    classData: SchoolClass
) {
    AppCard(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "${classData.grade} ${classData.letter} Класс",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))

            AppBadge(text = "${classData.members.size} учеников")

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Классный руководитель",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = classData.teacher?.let {
                    "${it.name} ${it.lastName}"
                } ?: classData.teacherLogin,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun ClassTop(
    classData: SchoolClass,
    onUsersClick: () -> Unit
) {
    if (classData.members.isNotEmpty()) {
        AppCard(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Топ 3 класса",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.height(12.dp))

                classData.members.take(3).forEachIndexed { index, member ->
                    MemberItem(member = member)
                    if (index != classData.members.take(3).lastIndex) {
                        HorizontalDivider()
                    }
                }

                Spacer(Modifier.height(8.dp))

                PrimaryButton(
                    text = "Все ученики",
                    onClick = onUsersClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    } else {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            EmptyState(text = "В классе нет учеников")
        }
    }
}
