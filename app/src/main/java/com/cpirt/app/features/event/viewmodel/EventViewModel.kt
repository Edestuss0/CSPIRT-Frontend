package com.cpirt.app.features.event.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cpirt.app.core.entity.AppResult
import com.cpirt.app.core.exception.ServerException
import com.cpirt.app.domain.classes.entity.SchoolClass
import com.cpirt.app.domain.classes.usecases.GetClassesByIdsUseCase
import com.cpirt.app.domain.events.entity.Event
import com.cpirt.app.domain.events.usecases.AddEventPlayersUseCase
import com.cpirt.app.domain.events.usecases.RemoveEventPlayersUseCase
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
class EventViewModel @Inject constructor(
    private val getClasses: GetClassesByIdsUseCase,
    private val addPlayers: AddEventPlayersUseCase,
    private val removePlayers: RemoveEventPlayersUseCase,
    private val getMe: GetMeUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(EventState())
    val state = _state.asStateFlow()
    private val _events = Channel<AppSnackbarVisuals>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        loadData(false)
    }

    fun loadData(force: Boolean) {
        if (_state.value.event == null) return
        viewModelScope.launch {
            getClasses(_state.value.event!!.classes, force).combine(getMe(force)) {classesRes, meRes ->
                classesRes to meRes
            }.collect { (classesRes, meRes) ->
                when {
                    classesRes is AppResult.Loading || meRes is AppResult.Loading -> {
                        _state.update { it.copy(
                            isLoading = true
                        ) }
                    }
                    classesRes is AppResult.Success && meRes is  AppResult.Success -> {
                        _state.update { it.copy(
                            isLoading = false,
                            eventClasses = classesRes.data,
                            profileInfo = meRes.data.user
                        ) }
                    }
                    classesRes is AppResult.Error && meRes !is AppResult.Error -> {
                        _state.update { it.copy(
                            isLoading = false,
                            eventClasses = classesRes.data ?: state.value.eventClasses,
                        ) }
                        _events.send(AppSnackbarVisuals(
                            message = classesRes.message,
                            type = SnackbarMessageType.ERROR
                        ))
                    }
                    classesRes !is AppResult.Error && meRes is AppResult.Error -> {
                        _state.update { it.copy(
                            isLoading = false,
                            profileInfo = meRes.data?.user ?: state.value.profileInfo,
                        ) }
                        _events.send(AppSnackbarVisuals(
                            message = meRes.message,
                            type = SnackbarMessageType.ERROR
                        ))
                    }
                }
            }
        }
    }

    fun loadEvent(event: Event) {
        _state.update { it.copy(event = event) }
    }

    fun addEventPlayers() {
        viewModelScope.launch {
            if (state.value.event == null) {
                _events.send(AppSnackbarVisuals(
                    message = "Выбрано некорректное мероприятие",
                    type = SnackbarMessageType.ERROR
                ))
                return@launch
            }
            if (state.value.eventPlayersState.toAdd.isEmpty()) {
                return@launch
            }
            try {
                addPlayers(state.value.event!!.id, state.value.eventPlayersState.toAdd)
            } catch (e: Exception) {
                when {
                    e is ServerException -> {
                        _events.send(
                            AppSnackbarVisuals(
                                type = SnackbarMessageType.ERROR,
                                message = "Ошибка при попытке добавления участников мероприятия"
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
            }
        }
    }

    fun removeEventPlayers() {
        viewModelScope.launch {
            if (state.value.event == null) {
                _events.send(AppSnackbarVisuals(
                    message = "Выбрано некорректное мероприятие",
                    type = SnackbarMessageType.ERROR
                ))
                return@launch
            }
            if (state.value.eventPlayersState.toRemove.isEmpty()) {
                return@launch
            }
            try {
                removePlayers(state.value.event!!.id, state.value.eventPlayersState.toRemove)
            } catch (e: Exception) {
                when {
                    e is ServerException -> {
                        _events.send(
                            AppSnackbarVisuals(
                                type = SnackbarMessageType.ERROR,
                                message = "Ошибка при попытке удаления участников мероприятия"
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
            }
        }
    }

    fun submitPlayersChanges() {
        addEventPlayers()
        removeEventPlayers()
        loadData(true)
    }

    fun onPlayersViewToggle(schoolClass: SchoolClass) {
        _state.update { it.copy(
            eventPlayersState = state.value.eventPlayersState.copy(
                current = schoolClass.members.mapNotNull { member ->
                    if (state.value.event!!.players.contains(member.id)) member.id else null
                },
                toAdd = emptyList(),
                toRemove = emptyList()
            )
        ) }
    }

    fun onPlayerToggle(id: Int) {
        val playersState = state.value.eventPlayersState
        when {
            playersState.current.contains(id) && !playersState.toRemove.contains(id) -> {
                val new = playersState.toRemove.plus(id)
                _state.update { it.copy(eventPlayersState = playersState.copy(toRemove = new)) }
            }
            playersState.current.contains(id) && playersState.toRemove.contains(id) -> {
                val new = playersState.toRemove.minus(id)
                _state.update { it.copy(eventPlayersState = playersState.copy(toRemove = new)) }
            }
            !playersState.current.contains(id) && !playersState.toAdd.contains(id) -> {
                val new = playersState.toAdd.plus(id)
                _state.update { it.copy(eventPlayersState = playersState.copy(toAdd = new)) }
            }
            !playersState.current.contains(id) && playersState.toAdd.contains(id) -> {
                val new = playersState.toAdd.minus(id)
                _state.update { it.copy(eventPlayersState = playersState.copy(toAdd = new)) }
            }
        }
    }
}