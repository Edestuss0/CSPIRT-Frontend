package com.cpirt.app.features.parallels.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cpirt.app.domain.classes.entity.Parallel
import com.cpirt.app.domain.classes.entity.SchoolClass
import com.cpirt.app.domain.user.entity.UserPersonalInfo
import com.cpirt.app.domain.user.entity.UserRole
import com.cpirt.app.ui.theme.AppBadge
import com.cpirt.app.ui.theme.AppCard
import com.cpirt.app.ui.theme.AppScaffold
import com.cpirt.app.ui.theme.EmptyState
import com.cpirt.app.ui.theme.PrimaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParallelScreen(
    parallel: Parallel?,
    onClassesClick: (id: Int) -> Unit = {},
    onBackClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    AppScaffold(
        modifier = Modifier.fillMaxSize(),
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
    ) { innerPadding ->

        if (parallel == null) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                EmptyState(text = "Не удалось получить информацию о параллели")
            }
            return@AppScaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(Modifier.height(16.dp))
            ParallelHeader(parallel = parallel)
            Spacer(Modifier.height(16.dp))
            parallel.bestClass?.let { bestClass ->
                ParallelBestClass(bestClass = bestClass)
                Spacer(Modifier.height(16.dp))
            }
            PrimaryButton(
                text = "Все классы параллели",
                onClick = { onClassesClick(parallel.id) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            )
            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun ParallelHeader(parallel: Parallel) {
    AppCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = parallel.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
            AppBadge(text = "${parallel.classes?.size ?: 0} Классов")
        }
    }
}

@Composable
private fun ParallelBestClass(bestClass: SchoolClass) {
    AppCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Лучший класс",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "${bestClass.grade}${bestClass.letter} Класс",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = "Классный руководитель",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = if (bestClass.teacher != null) {
                    bestClass.teacher.fullname
                } else {
                    bestClass.teacherLogin
                },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(Modifier.height(12.dp))
            AppBadge(text = "${bestClass.members.size} Учеников")
            Spacer(Modifier.height(12.dp))
            Text(
                text = "Общий рейтинг класса",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = (bestClass.classTotalRating + bestClass.userTotalRating).toString(),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ParallelScreenPreview() {
    val members = listOf(
        UserPersonalInfo(
            id = 1, name = "Анна", lastName = "Иванова", avatar = null,
            login = "anna", rating = 3200, role = UserRole.User,
            className = "9А", classId = 1
        ),
        UserPersonalInfo(
            id = 2, name = "Борис", lastName = "Петров", avatar = null,
            login = "boris", rating = 2800, role = UserRole.User,
            className = "9А", classId = 1
        )
    )
    val bestClass = SchoolClass(
        id = 1, name = "9А", grade = 9, letter = "А",
        firstQuarterComplete = 3, secondQuarterComplete = 2,
        thirdQuarterComplete = 1, quarterComplete = 6,
        teacherLogin = "teacher_login", teacher = null,
        members = members, userTotalRating = 1500, classTotalRating = 4500
    )
    val parallel = Parallel(
        id = 1, name = "9",
        bestClass = bestClass,
        classes = listOf(1, 2, 3)
    )
    AppScaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(16.dp))
            ParallelHeader(parallel = parallel)
            Spacer(Modifier.height(16.dp))
            ParallelBestClass(bestClass = bestClass)
            Spacer(Modifier.height(16.dp))
            PrimaryButton(
                text = "Все классы параллели",
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )
            Spacer(Modifier.height(32.dp))
        }
    }
}
