package com.cpirt.app.features.parallels.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cpirt.app.core.domain.classes.dto.ParallelsResult
import com.cpirt.app.core.domain.classes.repository.IClassRepository
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
    private val classRepository: IClassRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ParallelsState())
    val state = _state.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            classRepository.getParallels().collect { result ->
                when (result) {
                    is ParallelsResult.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                    }
                    is ParallelsResult.Success -> {
                        _state.update { it.copy(
                            isLoading = false,
                            parallels = result.data
                        ) }
                    }
                    is ParallelsResult.Error -> {
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
            Log.d("PARALLEL", state.value.parallels.toString())
        }
    }
}
