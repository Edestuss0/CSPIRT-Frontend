package com.cpirt.app.features.user.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cpirt.app.core.domain.profile.repository.IProfileRepository
import com.cpirt.app.core.domain.user.dto.AddNoteDto
import com.cpirt.app.core.domain.user.dto.ChangeRatingDto
import com.cpirt.app.core.domain.user.repository.IUserRepository
import com.cpirt.app.core.utils.getCurrentIsoTime
import com.cpirt.app.ui.components.AppSnackbarVisuals
import com.cpirt.app.ui.components.SnackbarMessageType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: IUserRepository,
    private val profileRepository: IProfileRepository
) : ViewModel() {
    val userId = savedStateHandle.get<Int>("id") ?: 1
    private val _state = MutableStateFlow(UserState())
    val state = _state.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, isError = false) }
            try {
                val profileDeferred = async { profileRepository.getMe() }
                val userDeferred = async { userRepository.getUserById(userId) }
                _state.update { it.copy(isLoading = false, userInfo = userDeferred.await(), profileInfo = profileDeferred.await()) }
            } catch (e: Exception) {
                _state.update { it.copy(
                    isLoading = false,
                    isError = true,
                    snackbarMessage = AppSnackbarVisuals(
                        type = SnackbarMessageType.ERROR,
                        message = e.message ?: "Произошла непредвиденная ошибка"
                    )
                )}
            }
        }
    }

    fun changeRating() {
        val changeRatingState = state.value.changeRatingState
        if (state.value.userInfo == null || changeRatingState.rating.isEmpty() || changeRatingState.reason.isEmpty()) {
            return
        }
        _state.update { it.copy(isLoading = true, isError = false) }
        val form = ChangeRatingDto(
            targetLogin = _state.value.userInfo!!.user.login,
            reason = changeRatingState.reason,
            rating = changeRatingState.rating.toIntOrNull() ?: 0
        )
        viewModelScope.launch {
            try {
                val response = userRepository.changeUserRating(form)
                _state.update { it.copy(
                    changeRatingState = changeRatingState.copy(show = false, reason = "", rating = ""),
                    isLoading = false,
                    isError = false,
                    snackbarMessage = AppSnackbarVisuals(
                        type = SnackbarMessageType.SUCCESS,
                        message = response
                    )
                )}
                loadData()
            } catch (e: Exception) {
                _state.update { it.copy(
                    changeRatingState = changeRatingState.copy(show = false, reason = "", rating = ""),
                    isLoading = false,
                    isError = true,
                    snackbarMessage = AppSnackbarVisuals(
                        type = SnackbarMessageType.ERROR,
                        message = e.message ?: "Произошла непредвиденная ошибка"
                    )
                )}
            }
        }
    }

    fun addNote() {
        val addNoteState = state.value.addNoteState
        if (state.value.userInfo == null || addNoteState.content.isEmpty() || state.value.profileInfo == null) {
            return
        }
        _state.update { it.copy(isLoading = true, isError = false) }
        val form = AddNoteDto(
            authorId = state.value.profileInfo!!.user.id,
            targetId = state.value.userInfo!!.user.id,
            authorName = "${state.value.profileInfo!!.user.name} ${state.value.profileInfo!!.user.lastName}",
            targetName = "${state.value.userInfo!!.user.name} ${state.value.userInfo!!.user.lastName}",
            content = addNoteState.content,
            createdAt = getCurrentIsoTime()
        )
        viewModelScope.launch {
            try {
                val response = userRepository.addNote(form)
                _state.update { it.copy(
                    addNoteState = addNoteState.copy(show = false, content = ""),
                    isLoading = false,
                    isError = false,
                    snackbarMessage = AppSnackbarVisuals(
                        type = SnackbarMessageType.SUCCESS,
                        message = response
                    )
                )}
                loadData()
            } catch (e: Exception) {
                _state.update { it.copy(
                    addNoteState = addNoteState.copy(show = false, content = ""),
                    isLoading = false,
                    isError = true,
                    snackbarMessage = AppSnackbarVisuals(
                        type = SnackbarMessageType.ERROR,
                        message = e.message ?: "Произошла непредвиденная ошибка"
                    )
                )}
            }
        }
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

    fun onMessageShown() {
        _state.update { it.copy(
            snackbarMessage = null
        ) }
    }
}