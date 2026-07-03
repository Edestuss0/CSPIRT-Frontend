package com.cpirt.app.features.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cpirt.app.core.entity.AppResult
import com.cpirt.app.domain.user.usecases.GetMeUseCase
import com.cpirt.app.domain.user.usecases.LogoutUseCase
import com.cpirt.app.ui.components.snackbar.AppSnackbarVisuals
import com.cpirt.app.ui.components.snackbar.SnackbarMessageType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getMeUseCase: GetMeUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    init {
        loadData(false)
    }

    fun loadData(force: Boolean) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, isError = false) }
            try {
                getMeUseCase(force).collect { result ->
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