package com.cpirt.app.features.user.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cpirt.app.features.user.viewmodel.UserViewModel
import com.cpirt.app.ui.components.LoadingScreen

@Composable
fun UserHost(
    viewModel: UserViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val navController = rememberNavController()

    if (state.isLoading) {
        LoadingScreen()
        return
    }

    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            UserScreen(
                viewModel = viewModel,
                state = state,
                onNotesClick = {navController.navigate("notes")},
                onComplaintsClick = {navController.navigate("complaints")},
                onBackClick = onBackClick,
            )
        }

        composable("notes") {
            UserNotesScreen(
                notes = state.userInfo?.notes ?: emptyList(),
                onBackClick = {navController.popBackStack()}
            )
        }

        composable("complaints") {
            UserComplaintsScreen(
                complaints = state.userInfo?.complaints ?: emptyList(),
                onBackClick = {navController.popBackStack()}
            )
        }
    }
}