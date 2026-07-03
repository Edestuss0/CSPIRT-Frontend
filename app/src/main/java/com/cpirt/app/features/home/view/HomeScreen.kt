package com.cpirt.app.features.home.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.cpirt.app.domain.classes.entity.SchoolClass
import com.cpirt.app.domain.events.entity.Event
import com.cpirt.app.domain.events.entity.EventsView
import com.cpirt.app.domain.user.entity.UserInfo
import com.cpirt.app.domain.user.entity.UserPersonalInfo
import com.cpirt.app.domain.user.entity.UserRole
import com.cpirt.app.features.class_details.view.ClassDetailHost
import com.cpirt.app.features.event.view.EventHost
import com.cpirt.app.features.home.viewmodel.HomeState
import com.cpirt.app.features.home.viewmodel.HomeViewModel
import com.cpirt.app.features.user.viewmodel.UserUIEventState
import com.cpirt.app.ui.components.cards.EventCard
import com.cpirt.app.ui.components.snackbar.AppSnackbarHost
import com.cpirt.app.ui.components.screens.LoadingScreen
import com.cpirt.app.ui.components.snackbar.AppSnackbarVisuals
import com.cpirt.app.ui.components.snackbar.SnackbarMessageType
import com.cpirt.app.ui.theme.AppBadge
import com.cpirt.app.ui.theme.AppCard
import com.cpirt.app.ui.theme.AppScaffold
import com.cpirt.app.ui.theme.EmptyState
import com.cpirt.app.ui.theme.PrimaryButton

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    state: HomeState,
    onUserClick: (id: Int) -> Unit,
    onEventsCLick: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedClass by remember { mutableStateOf<Int?>(null) }
    var selectedEvent by remember { mutableStateOf<Event?>(null) }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            snackbarHostState.showSnackbar(event)
        }
    }

    AppScaffold(
        snackbarHost = { AppSnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize().blur(if (selectedClass != null) 20.dp else 0.dp)
    ) {innerPadding ->
        when {
            state.isLoading && state.userInfo == null && state.schoolClassInfo == null -> {
                LoadingScreen()
            }
            state.userInfo != null && state.schoolClassInfo != null -> {
                PullToRefreshBox(
                    onRefresh = {viewModel.loadData(true)},
                    isRefreshing = state.isLoading,
                    modifier = Modifier.blur(if (selectedEvent != null || selectedClass != null) 20.dp else 0.dp)
                ) {
                    HomeContent(
                        innerPadding = innerPadding,
                        schoolClassInfo = state.schoolClassInfo,
                        onDetailsClick = {selectedClass = state.schoolClassInfo.id},
                        events = state.events,
                        onEventsCLick = onEventsCLick,
                        onEventCLick = {event -> selectedEvent = event}
                    )
                }
            }
            else -> {
                EmptyState("Не удалось получить информацию")
            }
        }
        ClassDetailHost(
            id = selectedClass,
            onDismiss = {selectedClass = null},
            onUserClick = onUserClick
        )
        EventHost(
            event = selectedEvent,
            onDismiss = {selectedEvent = null}
        )
    }
}

@Composable
private fun HomeContent(
    innerPadding: PaddingValues,
    schoolClassInfo: SchoolClass,
    onDetailsClick: () -> Unit,
    events: EventsView?,
    onEventsCLick: () -> Unit,
    onEventCLick: (Event) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (events != null) {
            item {
                MyClassHeader(schoolClassInfo = schoolClassInfo)
            }
            item {
                MyClassRating(rating = schoolClassInfo.classTotalRating + schoolClassInfo.userTotalRating)
            }
            item {
                PrimaryButton(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = "Подробнее",
                    onClick = onDetailsClick
                )
            }
            if (events.active.isNotEmpty()) {
                item {
                    ActiveEvents(
                        events.active,
                        onEventCLick
                    )
                }
            }
            item {
                PrimaryButton(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = "К другим мероприятиям",
                    onClick = onEventsCLick
                )
            }
        }
    }
}

@Composable
private fun ActiveEvents(
    events: List<Event>,
    onEventCLick: (Event) -> Unit
) {
    AppCard(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
        ) {
            Text(
                text = "Активные мероприятия",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(16.dp))
            events.forEach {
                EventCard(
                    event = it,
                    onEventClick = { onEventCLick(it) }
                )
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun MyClassRating(rating: Int) {
    AppCard(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
            Text(
                text = "Общий рейтинг класса",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = rating.toString(),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun MyClassHeader(
    schoolClassInfo: SchoolClass
) {
    AppCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
            Text(
                text = "${schoolClassInfo.grade} ${schoolClassInfo.letter} Класс",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(8.dp))
            AppBadge(text = "${schoolClassInfo.members.size} Учеников")
            Spacer(Modifier.height(16.dp))

            Text(
                text = "Классный руководитель",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = if (schoolClassInfo.teacher != null) {
                    "${schoolClassInfo.teacher.name} ${schoolClassInfo.teacher.lastName}"
                } else {
                    schoolClassInfo.teacherLogin
                },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}