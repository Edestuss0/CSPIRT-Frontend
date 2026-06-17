package com.cpirt.app.core.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cpirt.app.core.domain.user.repository.UserRepository
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
    private val userRepository: UserRepository
) : ViewModel() {
    val authState: StateFlow<AuthState> = userRepository.getAuthorizedStatus().map { status ->
        if (status == true) AuthState.Authorized else AuthState.NotAuthorized
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AuthState.Loading
    )
}