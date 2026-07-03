package com.cpirt.app.features.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cpirt.app.core.entity.AppResult
import com.cpirt.app.domain.classes.usecases.GetMyClassUseCase
import com.cpirt.app.domain.events.entity.toEventsView
import com.cpirt.app.domain.events.usecases.GetEventsUseCase
import com.cpirt.app.ui.components.snackbar.AppSnackbarVisuals
import com.cpirt.app.ui.components.snackbar.SnackbarMessageType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMyClass: GetMyClassUseCase,
    private val getEvents: GetEventsUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()
    private val _events = Channel<AppSnackbarVisuals>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

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
                            ) }
                            _events.send(AppSnackbarVisuals(
                                type = SnackbarMessageType.ERROR,
                                message = result.message
                            ))
                        }
                    }
                }
                getEvents(force).collect { result ->
                    when (result) {
                        is AppResult.Loading -> {
                            _state.update { it.copy(
                                isLoading = true
                            ) }
                        }
                        is AppResult.Success -> {
                            _state.update { it.copy(
                                isLoading = false,
                                events = result.data.toEventsView(),
                            ) }
                        }
                        is AppResult.Error -> {
                            _state.update { it.copy(
                                isLoading = false,
                                events = result.data?.toEventsView(),
                            ) }
                            _events.send(AppSnackbarVisuals(
                                type = SnackbarMessageType.ERROR,
                                message = result.message
                            ))
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(
                    isLoading = false,
                    isError = true
                )}
                _events.send(AppSnackbarVisuals(
                    type = SnackbarMessageType.ERROR,
                    message = e.message ?: "Произошла непредвиденная ошибка"
                ))
            }
        }
    }
}