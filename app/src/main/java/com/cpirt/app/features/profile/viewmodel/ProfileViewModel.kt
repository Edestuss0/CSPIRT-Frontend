package com.cpirt.app.features.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cpirt.app.core.domain.user.dto.UserResult
import com.cpirt.app.core.domain.user.repository.IUserRepository
import com.cpirt.app.ui.components.AppSnackbarVisuals
import com.cpirt.app.ui.components.SnackbarMessageType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    userRepository: IUserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, isError = false) }
            try {
                userRepository.getMe(false).collect { result ->
                    when (result) {
                        is UserResult.Loading -> {
                            _state.update { it.copy(
                                isLoading = true
                            ) }
                        }
                        is UserResult.Success -> {
                            _state.update { it.copy(
                                isLoading = false,
                                userInfo = result.data,
                            ) }
                        }
                        is UserResult.Error -> {
                            _state.update { it.copy(
                                isLoading = false,
                                userInfo = result.oldData ?: it.userInfo,
                                snackbarMessage = AppSnackbarVisuals(
                                    type = SnackbarMessageType.ERROR,
                                    message = result.message
                                )
                            )}
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(
                    snackbarMessage = AppSnackbarVisuals(
                        type = SnackbarMessageType.ERROR,
                        message = e.message ?: "Прозошла непредвиденная ошибка"
                    ),
                    isError = true,
                    isLoading = false
                )}
            }
        }
    }

    fun onMessageShown() {
        _state.update { it.copy(snackbarMessage = null) }
    }
}