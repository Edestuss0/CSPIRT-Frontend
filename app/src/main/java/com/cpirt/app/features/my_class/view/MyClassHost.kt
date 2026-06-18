package com.cpirt.app.features.my_class.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cpirt.app.features.my_class.viewmodel.MyClassViewModel

@Composable
fun MyClassHost(
    viewModel: MyClassViewModel = hiltViewModel(),
    onUserClick: (id: Int) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            MyClassScreen(
                viewModel = viewModel,
                state = state,
                onUsersClick = {navController.navigate("users")},
            )
        }

        composable("users") {
            MyClassUsersScreen(
                users = state.schoolClassInfo?.members ?: emptyList(),
                onBackClick = {navController.popBackStack()},
                onUserClick = {id -> onUserClick(id)}
            )
        }
    }
}