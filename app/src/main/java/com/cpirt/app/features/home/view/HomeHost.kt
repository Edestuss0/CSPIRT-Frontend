package com.cpirt.app.features.home.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cpirt.app.features.home.viewmodel.HomeViewModel

@Composable
fun HomeHost(
    viewModel: HomeViewModel = hiltViewModel(),
    onUserClick: (id: Int) -> Unit
) {
    val navController = rememberNavController()
    val state by viewModel.state.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            HomeScreen(
                viewModel = viewModel,
                state = state,
                onUserClick = onUserClick,
                onEventsCLick = {navController.navigate("events")}
            )
        }

        composable("events") {
            EventsScreen(
                events = state.events?.completed.orEmpty() + state.events?.scheduled.orEmpty(),
                onBackClick = {navController.popBackStack()}
            )
        }
    }
}