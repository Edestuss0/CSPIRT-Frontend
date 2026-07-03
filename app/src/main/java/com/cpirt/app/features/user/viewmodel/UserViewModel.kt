package com.cpirt.app.features.user.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cpirt.app.core.entity.AppResult
import com.cpirt.app.domain.user.entity.AddComplaintForm
import com.cpirt.app.domain.user.entity.AddNoteForm
import com.cpirt.app.domain.user.entity.ChangeRatingForm
import com.cpirt.app.core.utils.getCurrentIsoTime
import com.cpirt.app.domain.user.usecases.AddComplaintUseCase
import com.cpirt.app.domain.user.usecases.AddNoteUseCase
import com.cpirt.app.domain.user.usecases.ChangeRatingUseCase
import com.cpirt.app.domain.user.usecases.GetMeUseCase
import com.cpirt.app.domain.user.usecases.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getUser: GetUserUseCase,
    private val getMe: GetMeUseCase,
    private val changeRating: ChangeRatingUseCase,
    private val addNote: AddNoteUseCase,
    private val addComplaint: AddComplaintUseCase
) : ViewModel() {
    val userId = savedStateHandle.get<Int>("id") ?: 1
    private val _state = MutableStateFlow(UserState())
    val state = _state.asStateFlow()
    private val _events = Channel<UserUIEventState>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        loadData(false)
    }

    fun loadData(force: Boolean) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, isError = false) }
            try {
                getUser(userId, force).collect { result ->
                    when (result) {
                        is AppResult.Loading -> {
                            _state.update { it.copy(
                                isLoading = true
                            ) }
                        }
                        is AppResult.Success -> {
                            _state.update { it.copy(
                                isLoading = false,
                                userInfo = result.data,
                            ) }
                        }
                        is AppResult.Error -> {
                            _state.update { it.copy(
                                isLoading = false,
                                userInfo = result.data ?: it.userInfo,
                            )}
                            _events.send(UserUIEventState.ShowError(result.message))
                        }
                    }
                }
                getMe(force).collect { result ->
                    when (result) {
                        is AppResult.Loading -> {
                            _state.update { it.copy(
                                isLoading = true
                            ) }
                        }
                        is AppResult.Success -> {
                            _state.update { it.copy(
                                isLoading = false,
                                profileInfo = result.data,
                            ) }
                        }
                        is AppResult.Error -> {
                            _state.update { it.copy(
                                isLoading = false,
                                profileInfo = result.data ?: it.profileInfo,
                            )}
                            _events.send(UserUIEventState.ShowError(result.message))
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(
                    isLoading = false,
                    isError = true,
                )}
                _events.send(UserUIEventState.ShowError(e.message ?: "Произошла непредвиденная ошибка"))
            }
        }
    }

    fun changeRating() {
        val changeRatingState = state.value.changeRatingState
        if (state.value.userInfo == null || changeRatingState.rating.isEmpty() || changeRatingState.reason.isEmpty()) {
            return
        }
        _state.update { it.copy(isLoading = true, isError = false) }
        val form = ChangeRatingForm(
            targetLogin = _state.value.userInfo!!.user.login,
            reason = changeRatingState.reason,
            rating = changeRatingState.rating.toIntOrNull() ?: 0
        )
        viewModelScope.launch {
            try {
                changeRating(form, state.value.profileInfo?.user)
                loadData(true)
            } catch (e: Exception) {
                _state.update { it.copy(
                    changeRatingState = changeRatingState.copy(show = false, reason = "", rating = ""),
                    isLoading = false,
                    isError = true,
                )}
                _events.send(UserUIEventState.ShowError(e.message ?: "Произошла непредвиденная ошибка"))
            }
        }
    }

    fun addNote() {
        val addNoteState = state.value.addNoteState
        if (state.value.userInfo == null || addNoteState.content.isEmpty() || state.value.profileInfo == null) {
            return
        }
        _state.update { it.copy(isLoading = true, isError = false) }
        val form = AddNoteForm(
            authorId = state.value.profileInfo!!.user.id,
            targetId = state.value.userInfo!!.user.id,
            authorName = "${state.value.profileInfo!!.user.name} ${state.value.profileInfo!!.user.lastName}",
            targetName = "${state.value.userInfo!!.user.name} ${state.value.userInfo!!.user.lastName}",
            content = addNoteState.content,
            createdAt = getCurrentIsoTime()
        )
        viewModelScope.launch {
            try {
                val response = addNote(form, state.value.profileInfo?.user)
                _state.update { it.copy(
                    addNoteState = addNoteState.copy(show = false, content = ""),
                    isLoading = false,
                    isError = false,
                )}
                _events.send(UserUIEventState.ShowSuccess(response))
                loadData(true)
            } catch (e: Exception) {
                _state.update { it.copy(
                    addNoteState = addNoteState.copy(show = false, content = ""),
                    isLoading = false,
                    isError = true,
                )}
                _events.send(UserUIEventState.ShowError(e.message ?: "Произошла непредвиденная ошибка"))
            }
        }
    }

    fun addComplaint() {
        val addComplaintState = state.value.addComplaintState
        if (state.value.userInfo == null || addComplaintState.content.isEmpty() || state.value.profileInfo == null) {
            return
        }
        _state.update { it.copy(isLoading = true, isError = false) }
        val form = AddComplaintForm(
            authorId = state.value.profileInfo!!.user.id,
            targetId = state.value.userInfo!!.user.id,
            authorName = "${state.value.profileInfo!!.user.name} ${state.value.profileInfo!!.user.lastName}",
            targetName = "${state.value.userInfo!!.user.name} ${state.value.userInfo!!.user.lastName}",
            content = addComplaintState.content,
            createdAt = getCurrentIsoTime()
        )
        viewModelScope.launch {
            try {
                val response = addComplaint(form, state.value.profileInfo?.user)
                _state.update { it.copy(
                    addComplaintState = addComplaintState.copy(show = false, content = ""),
                    isLoading = false,
                    isError = false,
                )}
                _events.send(UserUIEventState.ShowSuccess(response))
                loadData(true)
            } catch (e: Exception) {
                _state.update { it.copy(
                    addComplaintState = addComplaintState.copy(show = false, content = ""),
                    isLoading = false,
                    isError = true,
                )}
                _events.send(UserUIEventState.ShowError(e.message ?: "Произошла непредвиденная ошибка"))
            }
        }
    }

    fun onChangeAddComplaintModalVisibility() {
        val addComplaintState = state.value.addComplaintState
        _state.update { it.copy(
            addComplaintState = addComplaintState.copy(show = !addComplaintState.show)
        ) }
    }

    fun onAddComplaintContentInput(input: String) {
        val addComplaintState = state.value.addComplaintState
        _state.update { it.copy(
            addComplaintState = addComplaintState.copy(content = input)
        ) }
    }

    fun onChangeAddNoteModalVisibility() {
        val addNoteState = state.value.addNoteState
        _state.update { it.copy(
            addNoteState = addNoteState.copy(show = !addNoteState.show)
        ) }
    }

    fun onAddNoteContentInput(input: String) {
        val addNoteState = state.value.addNoteState
        _state.update { it.copy(
            addNoteState = addNoteState.copy(content = input)
        ) }
    }

    fun onChangeRatingChangeModalVisibility() {
        val changeRatingState = state.value.changeRatingState
        _state.update { it.copy(
            changeRatingState = changeRatingState.copy(show = !changeRatingState.show)
        ) }
    }

    fun onChangeRatingReasonInput(input: String) {
        val changeRatingState = state.value.changeRatingState
        _state.update { it.copy(
            changeRatingState = changeRatingState.copy(reason = input)
        ) }
    }

    fun onChangeRatingRatingInput(input: String) {
        val changeRatingState = state.value.changeRatingState
        _state.update { it.copy(
            changeRatingState = changeRatingState.copy(rating = input)
        ) }
    }
}