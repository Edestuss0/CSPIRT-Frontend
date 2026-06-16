package com.cpirt.app.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cpirt.app.features.authorization.view.AuthorizationScreen
import com.cpirt.app.ui.components.LoadingScreen

@Composable
fun AppNavigation(
    viewModel: NavigationViewModel = hiltViewModel()
) {

    val authState by viewModel.authState.collectAsState()
    val rootNavController = rememberNavController()

    if (authState is AuthState.Loading) {
        LoadingScreen()
        return
    }

    val startDestination = when (authState) {
        is AuthState.Authorized -> "main"
        else -> "login"
    }

    NavHost(
        navController = rootNavController,
        startDestination = startDestination
    ) {
        composable("login") {
            AuthorizationScreen()
        }

        composable("main") {
            MainScreen(rootNavController)
        }
    }
}