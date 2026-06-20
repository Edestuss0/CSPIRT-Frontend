package com.cpirt.app.features.my_class.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cpirt.app.core.domain.classes.dto.ClassResult
import com.cpirt.app.core.domain.classes.repository.IClassRepository
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
class MyClassViewModel @Inject constructor(
    private val сlassRepository: IClassRepository,
    private val userRepository: IUserRepository
) : ViewModel() {
    private val _state = MutableStateFlow(MyClassState())
    val state = _state.asStateFlow()

    init {
        loadData()
    }

    fun loadData(force: Boolean = false) {
        _state.update { it.copy(isLoading = true, isError = false) }
        viewModelScope.launch {
            try {
                сlassRepository.getMyClass(force).collect { result ->
                    when (result) {
                        is ClassResult.Loading -> {
                            _state.update { it.copy(
                                isLoading = true
                            ) }
                        }
                        is ClassResult.Success -> {
                            _state.update { it.copy(
                                isLoading = false,
                                schoolClassInfo = result.data
                            ) }
                        }
                        is ClassResult.Error -> {
                            _state.update { it.copy(
                                isLoading = false,
                                schoolClassInfo = result.data ?: it.schoolClassInfo,
                                snackbarMessage = AppSnackbarVisuals(
                                    type = SnackbarMessageType.ERROR,
                                    message = result.message
                                )
                            ) }
                        }
                    }
                }
                userRepository.getMe(force).collect { result ->
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
                        message = e.message ?: "Произошла непредвиденная ошибка"
                    ),
                    isLoading = false,
                    isError = true
                )}
            }
        }
    }

    fun onMessageShown() {
        _state.update { it.copy(snackbarMessage = null) }
    }
}