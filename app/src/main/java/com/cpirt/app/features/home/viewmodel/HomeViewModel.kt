package com.cpirt.app.features.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cpirt.app.core.entity.AppResult
import com.cpirt.app.core.exception.ServerException
import com.cpirt.app.domain.classes.entity.SchoolClass
import com.cpirt.app.domain.classes.usecases.GetAllClassesUseCase
import com.cpirt.app.domain.classes.usecases.GetMyClassUseCase
import com.cpirt.app.domain.events.entity.AddEventForm
import com.cpirt.app.domain.events.entity.EventsView
import com.cpirt.app.domain.events.entity.toEventsView
import com.cpirt.app.domain.events.usecases.AddEventUseCase
import com.cpirt.app.domain.events.usecases.GetEventsUseCase
import com.cpirt.app.domain.user.entity.UserInfo
import com.cpirt.app.domain.user.usecases.GetMeUseCase
import com.cpirt.app.ui.components.snackbar.AppSnackbarVisuals
import com.cpirt.app.ui.components.snackbar.SnackbarMessageType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okio.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMyClass: GetMyClassUseCase,
    private val getEvents: GetEventsUseCase,
    private val getClasses: GetAllClassesUseCase,
    private val getMe: GetMeUseCase,
    private val addEvent: AddEventUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()
    private val _events = Channel<AppSnackbarVisuals>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        loadData()
    }

    fun loadData(force: Boolean = false) {
        viewModelScope.launch {
            combine(
                getMyClass(force),
                getEvents(force),
                getClasses(force),
                getMe(force)
            ) { classResult, eventsResult, classesResult, meResult ->
                val isLoading = classResult is AppResult.Loading || eventsResult is AppResult.Loading || classesResult is AppResult.Loading

                val classInfo = when (classResult) {
                    is AppResult.Success -> classResult.data.schoolClass
                    is AppResult.Error -> classResult.data?.schoolClass
                    else -> null
                }
                val userInfo = when (meResult) {
                    is AppResult.Success -> {
                        Log.d("ME", meResult.data.user.role.toString())
                        meResult.data
                    }
                    is AppResult.Error -> meResult.data
                    else -> null
                }
                val events = when (eventsResult) {
                    is AppResult.Success -> eventsResult.data.toEventsView()
                    is AppResult.Error -> eventsResult.data?.toEventsView()
                    else -> null
                }
                val classes = when (classesResult) {
                    is AppResult.Success -> classesResult.data
                    is AppResult.Error -> classesResult.data
                    else -> null
                }

                val errors = userInfo?.user?.classId?.let {
                    if (it > 0) {
                        listOf(classResult, eventsResult, classesResult, meResult).filterIsInstance<AppResult.Error<*>>()
                    } else {
                        listOf(eventsResult, classesResult, meResult).filterIsInstance<AppResult.Error<*>>()
                    }
                }

                Log.d("ROLE", userInfo?.user.toString())
                CombinedResult(isLoading, classInfo, userInfo, events, classes, errors)
            }.collect { combined ->
                _state.update { current ->
                    current.copy(
                        isLoading = combined.isLoading,
                        schoolClassInfo = combined.schoolClassInfo ?: current.schoolClassInfo,
                        userInfo = combined.userInfo ?: current.userInfo,
                        events = combined.events ?: current.events,
                        classes = combined.classes ?: current.classes
                    )
                }

                combined.errors?.forEach { error ->
                    _events.send(
                        AppSnackbarVisuals(
                            type = SnackbarMessageType.ERROR,
                            message = error.message
                        )
                    )
                }
            }
        }
    }

    fun addEvent() {
        val addEventState = state.value.addEventState
        _state.update { it.copy(isLoading = true, isError = false) }
        val form = AddEventForm(
            title = addEventState.title,
            description = addEventState.description,
            startedAt = addEventState.startedAt,
            classes = addEventState.classes,
            ratingReward = addEventState.ratingReward.toIntOrNull() ?: 0
        )
        viewModelScope.launch {
            try {
                addEvent(form)
                _state.update { it.copy(
                    addEventState = AddEventState(),
                    isLoading = false,
                    isError = false
                ) }
                _events.send(
                    AppSnackbarVisuals(
                        type = SnackbarMessageType.SUCCESS,
                        message = "Мероприятие добавлено"
                    )
                )
                loadData(true)
            } catch (e: Exception) {
                when {
                    e is ServerException -> {
                        _events.send(
                            AppSnackbarVisuals(
                                type = SnackbarMessageType.ERROR,
                                message = "Ошибка при попытке добавления мероприятия"
                            )
                        )
                    }
                    e is IOException -> {
                        _events.send(
                            AppSnackbarVisuals(
                                type = SnackbarMessageType.ERROR,
                                message = "Нет подключения к интернету"
                            )
                        )
                    }
                    e is IllegalArgumentException -> {
                        _events.send(
                            AppSnackbarVisuals(
                                type = SnackbarMessageType.ERROR,
                                message = e.message ?: "Произошла непредвиденная ошибка"
                            )
                        )
                    }
                    else -> {
                        _events.send(
                            AppSnackbarVisuals(
                                type = SnackbarMessageType.ERROR,
                                message = "Произошла непредвиденная ошибка"
                            )
                        )
                    }
                }
                _state.update { it.copy(
                    isLoading = false,
                    isError = true
                ) }
            }
        }
    }

    fun onChangeAddEventModalVisibility() {
        val addEventState = state.value.addEventState
        _state.update { it.copy(
            addEventState = addEventState.copy(show = !addEventState.show)
        ) }
    }

    fun onAddEventTitleInput(input: String) {
        val addEventState = state.value.addEventState
        _state.update { it.copy(
            addEventState = addEventState.copy(title = input)
        ) }
    }

    fun onAddEventDescriptionInput(input: String) {
        val addEventState = state.value.addEventState
        _state.update { it.copy(
            addEventState = addEventState.copy(description = input)
        ) }
    }

    fun onAddEventStartedAtInput(input: String) {
        val addEventState = state.value.addEventState
        _state.update { it.copy(
            addEventState = addEventState.copy(startedAt = input)
        ) }
    }

    fun onAddEventRatingRewardInput(input: String) {
        val addEventState = state.value.addEventState
        _state.update { it.copy(
            addEventState = addEventState.copy(ratingReward = input)
        ) }
    }

    fun onAddEventClassToggle(classId: Int) {
        val addEventState = state.value.addEventState
        val selectedClasses = if (classId in addEventState.classes) {
            addEventState.classes - classId
        } else {
            addEventState.classes + classId
        }
        _state.update { it.copy(
            addEventState = addEventState.copy(classes = selectedClasses)
        ) }
    }
}

private data class CombinedResult(
    val isLoading: Boolean,
    val schoolClassInfo: SchoolClass?,
    val userInfo: UserInfo?,
    val events: EventsView?,
    val classes: List<SchoolClass>?,
    val errors: List<AppResult.Error<*>>?
)
