package com.cpirt.app.features.parallels.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cpirt.app.core.entity.AppResult
import com.cpirt.app.domain.classes.usecases.GetParallelsUseCase
import com.cpirt.app.ui.components.AppSnackbarVisuals
import com.cpirt.app.ui.components.SnackbarMessageType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParallelsViewmodel @Inject constructor(
    private val getParallels: GetParallelsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ParallelsState())
    val state = _state.asStateFlow()

    init {
        loadData()
    }

    fun loadData(force: Boolean = false) {
        viewModelScope.launch {
            getParallels(force).collect { result ->
                when (result) {
                    is AppResult.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                    }
                    is AppResult.Success -> {
                        _state.update { it.copy(
                            isLoading = false,
                            parallels = result.data
                        ) }
                    }
                    is AppResult.Error -> {
                        _state.update { it.copy(
                            isLoading = false,
                            parallels = result.data,
                            snackbarMessage = AppSnackbarVisuals(
                                type = SnackbarMessageType.ERROR,
                                message = result.message
                            )
                        ) }
                    }
                }
            }
        }
    }
}
