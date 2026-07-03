package com.cpirt.app.features.parallels.view

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cpirt.app.features.parallels.viewmodel.ParallelsViewmodel
import androidx.compose.runtime.collectAsState

@Composable
fun ParallelsHost(
    viewmodel: ParallelsViewmodel = hiltViewModel(),
    onClassesClick: (id: Int) -> Unit
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            ParallelsScreen(
                viewmodel = viewmodel,
                onParallelClick = {id -> navController.navigate("parallel/$id")}
            )
        }

        composable(
            route = "parallel/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.savedStateHandle.get<Int>("id")
            val parallel = viewmodel.state.collectAsState().value.parallels?.find { it.id == id }
            ParallelScreen(
                parallel = parallel,
                onBackClick = {navController.popBackStack()},
                onClassesClick = onClassesClick
            )
        }
    }
}