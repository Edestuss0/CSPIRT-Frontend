package com.cpirt.app.features.user.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cpirt.app.features.user.viewmodel.UserViewModel

@Composable
fun UserHost(
    viewModel: UserViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val navController = rememberNavController()

    when {
//        state.isLoading -> {
//            LoadingScreen()
//        }
//        state.userInfo == null || state.profileInfo == null -> {
//            EmptyState("Не удалось получить информацию о пользователе")
//        }
        else -> {
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
                        onBackClick = {navController.popBackStack()},
                        onDelete = {id -> viewModel.deleteNote(id)},
                        role = state.profileInfo!!.user.role
                    )
                }

                composable("complaints") {
                    UserComplaintsScreen(
                        complaints = state.userInfo?.complaints ?: emptyList(),
                        onBackClick = {navController.popBackStack()},
                        onDelete = {id -> viewModel.deleteComplaint(id)},
                        role = state.profileInfo!!.user.role
                    )
                }
            }
        }
    }

}