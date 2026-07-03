package com.cpirt.app.features.classes.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cpirt.app.core.entity.AppResult
import com.cpirt.app.domain.classes.usecases.GetParallelClassesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClassesViewModel @Inject constructor (
    private val getClasses: GetParallelClassesUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val id = savedStateHandle.get<Int?>("id")
    private val _state = MutableStateFlow(ClassesState())
    val state = _state.asStateFlow()
    private val _events = Channel<ClassesUIEventState>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        loadData(false)
    }

    fun loadData(force: Boolean) {
        viewModelScope.launch {
            if (id == null) {
                _events.send(ClassesUIEventState.ShowError("Параллель не найдена"))
            }

            _state.update { it.copy(isLoading = true) }
            getClasses(id!!, force).collect { result ->
                when (result) {
                    is AppResult.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                    }
                    is AppResult.Success -> {
                        _state.update { it.copy(
                            isLoading = false,
                            classesData = result.data
                        ) }
                    }
                    is AppResult.Error -> {
                        _state.update { it.copy(
                            isLoading = false,
                            classesData = result.data ?: it.classesData
                        ) }
                        _events.send(ClassesUIEventState.ShowError(result.message))
                    }
                }
            }
        }
    }
}