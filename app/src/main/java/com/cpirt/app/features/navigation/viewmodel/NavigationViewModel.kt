package com.cpirt.app.features.navigation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cpirt.app.domain.user.usecases.GetAuthStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed class AuthState {
    object Loading : AuthState()
    object Authorized : AuthState()
    object NotAuthorized : AuthState()
}

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val getAuthStatus: GetAuthStatusUseCase
) : ViewModel() {
    val authState: StateFlow<AuthState> = getAuthStatus().map { status ->
        if (status == true) AuthState.Authorized else AuthState.NotAuthorized
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AuthState.Loading
    )
}