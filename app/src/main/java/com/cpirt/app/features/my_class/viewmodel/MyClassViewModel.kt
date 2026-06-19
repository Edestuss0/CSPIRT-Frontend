package com.cpirt.app.features.my_class.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cpirt.app.core.domain.my_class.repository.IMyClassRepository
import com.cpirt.app.core.domain.profile.dto.ProfileResult
import com.cpirt.app.core.domain.profile.repository.IProfileRepository
import com.cpirt.app.ui.components.AppSnackbarVisuals
import com.cpirt.app.ui.components.SnackbarMessageType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyClassViewModel @Inject constructor(
    private val myClassRepository: IMyClassRepository,
    private val profileRepository: IProfileRepository
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
                val classData = async { myClassRepository.getMyClass() }
                _state.update { it.copy(schoolClassInfo = classData.await()) }
                profileRepository.getMe(force).collect { result ->
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