package com.cpirt.app.features.authorization.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cpirt.app.core.data.auth.AuthorizationRepository
import com.cpirt.app.core.data.auth.LoginDto
import com.cpirt.app.ui.components.AppSnackbarVisuals
import com.cpirt.app.ui.components.SnackbarMessageType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewmodel @Inject constructor(
    private val authorizationRepository: AuthorizationRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AuthorizationState())
    val state = _state.asStateFlow()

    fun login() {
        _state.update { it.copy(isLoading = true, isError = false) }
        if (state.value.passwordInput.length < 6) {
            _state.update { it.copy(
                isError = true,
                snackbarMessage = AppSnackbarVisuals(
                    type = SnackbarMessageType.ERROR,
                    message = "Мининмальная длина пароля - 6 символов"
                ),
                isLoading = false
            )}
            return
        }
        val form = LoginDto(state.value.loginInput, state.value.passwordInput)
        viewModelScope.launch {
            try {
                val loginResponse = authorizationRepository.login(form)
                _state.update { it.copy(
                    isError = false,
                    passwordInput = "",
                    loginInput = "",
                    snackbarMessage = AppSnackbarVisuals(
                        type = SnackbarMessageType.SUCCESS,
                        message = loginResponse
                    ),
                    isLoading = false
                )}
            } catch (e: Exception) {
                _state.update { it.copy(
                    isError = true,
                    passwordInput = "",
                    loginInput = "",
                    snackbarMessage = AppSnackbarVisuals(
                        type = SnackbarMessageType.ERROR,
                        message = e.message ?: "Непредвиденная ошибка"
                    ),
                    isLoading = false
                )}
            }
        }
    }

    fun onLoginInput(input: String) {
        _state.update { it.copy(loginInput = input) }
    }

    fun onPasswordInput(input: String) {
        _state.update { it.copy(passwordInput = input) }
    }

    fun onMessageShown() {
        _state.update { it.copy(snackbarMessage = null) }
    }
}