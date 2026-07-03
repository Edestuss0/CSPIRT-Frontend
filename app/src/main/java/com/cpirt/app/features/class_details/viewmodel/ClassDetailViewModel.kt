package com.cpirt.app.features.class_details.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cpirt.app.core.entity.AppResult
import com.cpirt.app.domain.classes.usecases.GetClassUseCase
import com.cpirt.app.domain.schedule.entity.toScheduleView
import com.cpirt.app.domain.schedule.usecases.GetScheduleByClassUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClassDetailViewModel @Inject constructor(
    private val getClass: GetClassUseCase,
    private val getSchedule: GetScheduleByClassUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ClassDetailState())
    val state = _state.asStateFlow()

    fun loadData(id: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getClass(id, false).collect { result ->
                when (result) {
                    is AppResult.Success -> {
                        _state.update { it.copy(
                            isLoading = false,
                            classData = result.data
                        ) }
                    }
                    is AppResult.Error -> {
                        _state.update { it.copy(
                            isLoading = false,
                            classData = result.data
                        ) }
                    }
                    is AppResult.Loading -> {
                        _state.update { it.copy(
                            isLoading = true,
                        ) }
                    }
                    else -> {_state.update { it.copy(isLoading = false) }}
                }
            }
        }
    }

    fun loadSchedule(id: Int, force: Boolean) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getSchedule(id, force).collect { result ->
                when (result) {
                    is AppResult.Success -> {
                        _state.update { it.copy(
                            isLoading = false,
                            scheduleData = result.data.toScheduleView()
                        ) }
                    }
                    is AppResult.Error -> {
                        _state.update { it.copy(
                            isLoading = false,
                            scheduleData = result.data?.toScheduleView()
                        ) }
                    }
                    is AppResult.Loading -> {
                        _state.update { it.copy(
                            isLoading = true,
                        ) }
                    }
                    else -> {_state.update { it.copy(isLoading = false) }}
                }
            }
        }
    }
}