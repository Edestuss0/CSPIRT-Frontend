package com.cpirt.app.features.home.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cpirt.app.domain.classes.entity.SchoolClass
import com.cpirt.app.domain.events.entity.Event
import com.cpirt.app.domain.events.entity.EventsView
import com.cpirt.app.domain.user.entity.UserInfo
import com.cpirt.app.domain.user.entity.UserRole
import com.cpirt.app.features.class_details.view.ClassDetailHost
import com.cpirt.app.features.event.view.EventHost
import com.cpirt.app.features.home.viewmodel.HomeState
import com.cpirt.app.features.home.viewmodel.HomeViewModel
import com.cpirt.app.ui.components.cards.EventCard
import com.cpirt.app.ui.components.screens.LoadingScreen
import com.cpirt.app.ui.components.snackbar.AppSnackbarHost
import com.cpirt.app.ui.theme.AppBadge
import com.cpirt.app.ui.theme.AppCard
import com.cpirt.app.ui.theme.AppScaffold
import com.cpirt.app.ui.theme.AppTextField
import com.cpirt.app.ui.theme.EmptyState
import com.cpirt.app.ui.theme.PrimaryButton
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
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
    val addEventSheetState = rememberModalBottomSheetState()
    var isCompleteEventModalOpen by remember { mutableStateOf(false) }
    var isDeleteEventModalOpen by remember { mutableStateOf(false) }
    var event by remember { mutableStateOf<Event?>(null) }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            snackbarHostState.showSnackbar(event)
        }
    }

    LaunchedEffect(state.addEventState.show) {
        if (state.addEventState.show) {
            addEventSheetState.show()
        } else {
            addEventSheetState.hide()
        }
    }

    if (isCompleteEventModalOpen && event != null) {
        AlertDialog(
            onDismissRequest = {
                isCompleteEventModalOpen = false
                event = null
                               },
            title = {Text(
                text = "Завершение мероприятия",
                style = MaterialTheme.typography.titleLarge
            )},
            text = {Text(
                text = "Вы уверены, что хотите завершить мероприятие? Это действие нельзя отменить",
                style = MaterialTheme.typography.bodyLarge
            )},
            confirmButton = {
                PrimaryButton(
                    text = "Завершить",
                    onClick = {
                        viewModel.completeEvent(event!!)
                        isCompleteEventModalOpen = false
                        event = null
                    }
                )
            },
            dismissButton = {
                PrimaryButton(
                    text = "Отмена",
                    onClick = {
                        isCompleteEventModalOpen = false
                        event = null
                    }
                )
            }
        )
    }

    if (isDeleteEventModalOpen && event != null) {
        AlertDialog(
            onDismissRequest = {
                isDeleteEventModalOpen = false
                event = null
                               },
            title = {Text(
                text = "Удаление мероприятия",
                style = MaterialTheme.typography.titleLarge
            )},
            text = {Text(
                text = "Вы уверены, что хотите удалить мероприятие? Это действие нельзя отменить",
                style = MaterialTheme.typography.bodyLarge
            )},
            confirmButton = {
                PrimaryButton(
                    text = "Удалить",
                    onClick = {
                        viewModel.deleteEvent(event!!.id)
                        isDeleteEventModalOpen = false
                        event = null
                    }
                )
            },
            dismissButton = {
                PrimaryButton(
                    text = "Отмена",
                    onClick = {
                        isDeleteEventModalOpen = false
                        event = null
                    }
                )
            }
        )
    }

    if (state.addEventState.show || addEventSheetState.isVisible) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.onChangeAddEventModalVisibility() },
            sheetState = addEventSheetState
        ) {
            AddEventSheetContent(
                state = state,
                onTitleChange = { viewModel.onAddEventTitleInput(it) },
                onDescriptionChange = { viewModel.onAddEventDescriptionInput(it) },
                onStartedAtChange = { viewModel.onAddEventStartedAtInput(it) },
                onRatingRewardChange = { viewModel.onAddEventRatingRewardInput(it) },
                onClassToggle = { viewModel.onAddEventClassToggle(it) },
                onSubmit = { viewModel.addEvent() }
            )
        }
    }

    AppScaffold(
        snackbarHost = { AppSnackbarHost(snackbarHostState) },
        modifier = Modifier
            .fillMaxSize()
            .blur(if (selectedClass != null) 20.dp else 0.dp)
    ) {innerPadding ->
        when {
            state.isLoading && state.userInfo == null && state.schoolClassInfo == null -> {
                LoadingScreen()
            }
            state.userInfo != null -> {
                PullToRefreshBox(
                    onRefresh = {viewModel.loadData(true)},
                    isRefreshing = state.isLoading,
                    modifier = Modifier.blur(if (selectedEvent != null || selectedClass != null) 20.dp else 0.dp)
                ) {
                    HomeContent(
                        innerPadding = innerPadding,
                        schoolClassInfo = state.schoolClassInfo,
                        onDetailsClick = {selectedClass = state.schoolClassInfo?.id},
                        events = state.events,
                        onEventsCLick = onEventsCLick,
                        onEventCLick = {event -> selectedEvent = event},
                        profileInfo = state.userInfo,
                        onAddEventClick = { viewModel.onChangeAddEventModalVisibility() },
                        onDelete = { item ->
                            event = item
                            isDeleteEventModalOpen = true
                        },
                        onComplete = { item ->
                            event = item
                            isCompleteEventModalOpen = true
                        }
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
    schoolClassInfo: SchoolClass?,
    profileInfo: UserInfo?,
    onDetailsClick: () -> Unit,
    events: EventsView?,
    onEventsCLick: () -> Unit,
    onEventCLick: (Event) -> Unit,
    onAddEventClick: () -> Unit,
    onComplete: (event: Event) -> Unit,
    onDelete: (event: Event) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (events != null) {
            profileInfo?.user?.classId?.let {
                if (it > 0 && schoolClassInfo != null) {
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
                }
            }
            item {
                ActiveEvents(
                    events.active,
                    onEventCLick,
                    onDelete,
                    onComplete,
                    role = profileInfo?.user?.role ?: UserRole.User
                )
            }
            item {
                PrimaryButton(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = "К другим мероприятиям",
                    onClick = onEventsCLick
                )
            }

            if (profileInfo?.user?.role == UserRole.Owner) {
                item {
                    PrimaryButton(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = "Добавить мероприятие",
                        onClick = onAddEventClick
                    )
                }
            }
        }
    }
}

@Composable
private fun AddEventSheetContent(
    state: HomeState,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onStartedAtChange: (String) -> Unit,
    onRatingRewardChange: (String) -> Unit,
    onClassToggle: (Int) -> Unit,
    onSubmit: () -> Unit
) {
    val addEventState = state.addEventState

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        AppTextField(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            label = "Название мероприятия",
            value = addEventState.title,
            onValueChange = onTitleChange
        )

        Spacer(Modifier.height(16.dp))

        AppTextField(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            label = "Описание мероприятия",
            value = addEventState.description,
            onValueChange = onDescriptionChange
        )

        Spacer(Modifier.height(16.dp))

        NativeDateTimeField(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            value = addEventState.startedAt,
            onValueChange = onStartedAtChange
        )

        Spacer(Modifier.height(16.dp))

        ClassesSelect(
            classes = state.classes.orEmpty(),
            selectedClassIds = addEventState.classes,
            onClassToggle = onClassToggle,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        )

        Spacer(Modifier.height(16.dp))

        AppTextField(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            label = "Награда рейтинга",
            value = addEventState.ratingReward,
            onValueChange = { newValue ->
                if (newValue.isEmpty() || newValue.toIntOrNull() != null) {
                    onRatingRewardChange(newValue)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(Modifier.height(16.dp))

        PrimaryButton(
            text = "Добавить мероприятие",
            onClick = onSubmit,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        )

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun NativeDateTimeField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    fun showDateTimePicker() {
        val initial = parseEventDateTime(value)
        val calendar = Calendar.getInstance().apply {
            initial?.let {
                set(Calendar.YEAR, it.year)
                set(Calendar.MONTH, it.month - 1)
                set(Calendar.DAY_OF_MONTH, it.day)
                set(Calendar.HOUR_OF_DAY, it.hour)
                set(Calendar.MINUTE, it.minute)
            }
        }

        DatePickerDialog(
            context,
            { _, year, month, day ->
                TimePickerDialog(
                    context,
                    { _, hour, minute ->
                        onValueChange(formatEventDateTime(year, month + 1, day, hour, minute))
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                ).show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Box(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            label = { Text("Дата начала") },
            placeholder = { Text("2026-06-01 17:16") },
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth()
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable { showDateTimePicker() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ClassesSelect(
    classes: List<SchoolClass>,
    selectedClassIds: List<Int>,
    onClassToggle: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val sortedClasses = classes.sortedWith(compareBy<SchoolClass> { it.grade }.thenBy { it.letter })
    val selectedText = sortedClasses
        .filter { it.id in selectedClassIds }
        .joinToString { it.toClassLabel() }
        .ifEmpty { "Классы не выбраны" }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = {},
            readOnly = true,
            label = { Text("Классы") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true)
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            if (sortedClasses.isEmpty()) {
                DropdownMenuItem(
                    text = { Text("Классы не загружены") },
                    onClick = {}
                )
            } else {
                sortedClasses.forEach { schoolClass ->
                    val checked = schoolClass.id in selectedClassIds
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = checked,
                                    onCheckedChange = { onClassToggle(schoolClass.id) }
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(schoolClass.toClassLabel())
                            }
                        },
                        onClick = { onClassToggle(schoolClass.id) }
                    )
                }
            }
        }
    }
}

private fun SchoolClass.toClassLabel(): String = "$grade $letter класс"

private data class EventDateTimeParts(
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minute: Int
)

private fun parseEventDateTime(value: String): EventDateTimeParts? {
    val match = EVENT_DATE_TIME_REGEX.matchEntire(value) ?: return null
    val values = match.groupValues.drop(1).map { it.toIntOrNull() ?: return null }
    return EventDateTimeParts(
        year = values[0],
        month = values[1],
        day = values[2],
        hour = values[3],
        minute = values[4]
    )
}

private fun formatEventDateTime(
    year: Int,
    month: Int,
    day: Int,
    hour: Int,
    minute: Int
): String {
    return String.format(Locale.ROOT, "%04d-%02d-%02d %02d:%02d", year, month, day, hour, minute)
}

private val EVENT_DATE_TIME_REGEX = Regex("""^(\d{4})-(\d{2})-(\d{2}) (\d{2}):(\d{2})$""")

@Composable
private fun ActiveEvents(
    events: List<Event>,
    onEventCLick: (Event) -> Unit,
    onDelete: (event: Event) -> Unit,
    onComplete: (event: Event) -> Unit,
    role: UserRole
) {
    AppCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(
                text = "Активные мероприятия",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(16.dp))

            if (events.isEmpty()){
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Активные мероприятия не найдены",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(items = events) { event ->
                        EventCard(
                            event = event,
                            onEventClick = { onEventCLick(event) },
                            modifier = Modifier.width(260.dp),
                            role = role,
                            onComplete = {onComplete(event)},
                            onDelete = {onDelete(event)}
                        )
                    }
                }
            }

        }
    }
}

@Composable
private fun MyClassRating(rating: Int) {
    AppCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
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
