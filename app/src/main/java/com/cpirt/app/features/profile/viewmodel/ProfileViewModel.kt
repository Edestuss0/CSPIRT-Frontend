package com.cpirt.app.features.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cpirt.app.core.domain.auth.repository.AuthorizationRepositoryImpl
import com.cpirt.app.core.domain.auth.repository.IAuthRepository
import com.cpirt.app.core.domain.profile.dto.ProfileResult
import com.cpirt.app.core.domain.profile.repository.IProfileRepository
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
    profileRepository: IProfileRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, isError = false) }
            try {
                profileRepository.getMe(false).collect { result ->
                    when (result) {
                        is ProfileResult.Loading -> {
                            _state.update { it.copy(
                                isLoading = true
                            ) }
                        }
                        is ProfileResult.Success -> {
                            _state.update { it.copy(
                                isLoading = false,
                                userInfo = result.data,
                            ) }
                            if (result.fromCache) {
                                _state.update { it.copy(
                                    snackbarMessage = AppSnackbarVisuals(
                                        type = SnackbarMessageType.INFO,
                                        message = "Данные могут быть устаревшими"
                                    )
                                ) }
                            }
                        }
                        is ProfileResult.Error -> {
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