package com.cpirt.app.features.my_class.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cpirt.app.core.entity.AppResult
import com.cpirt.app.domain.classes.usecases.GetMyClassUseCase
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
    private val getMyClass: GetMyClassUseCase
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
                getMyClass(force).collect { result ->
                    when (result) {
                        is AppResult.Loading -> {
                            _state.update { it.copy(
                                isLoading = true
                            ) }
                        }
                        is AppResult.Success -> {
                            _state.update { it.copy(
                                isLoading = false,
                                schoolClassInfo = result.data.schoolClass,
                                userInfo = result.data.me
                            ) }
                        }
                        is AppResult.Error -> {
                            _state.update { it.copy(
                                isLoading = false,
                                schoolClassInfo = result.data?.schoolClass ?: it.schoolClassInfo,
                                userInfo = result.data?.me ?: it.userInfo,
                                snackbarMessage = AppSnackbarVisuals(
                                    type = SnackbarMessageType.ERROR,
                                    message = result.message
                                )
                            ) }
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