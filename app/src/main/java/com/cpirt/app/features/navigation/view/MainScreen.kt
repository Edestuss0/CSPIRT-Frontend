package com.cpirt.app.features.navigation.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cpirt.app.features.home.view.HomeHost
import com.cpirt.app.features.parallels.view.ParallelsHost
import com.cpirt.app.features.profile.view.ProfileScreen
import com.cpirt.app.ui.theme.AppScaffold

enum class Screens(val route: String, val title: String, val icon: ImageVector) {
    Home("home", "Главная", Icons.Default.Home),
    Rating("rating", "Рейтинг", Icons.Default.Leaderboard),
    Profile("profile", "Профиль", Icons.Default.Person),
}

@Composable
fun MainScreen(
    rootNavController: NavController,
) {

    val tabNavController = rememberNavController()
    val backStackEntry by tabNavController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    AppScaffold(
        bottomBar = {
            NavigationBar {
                Screens.entries.forEach { screen ->
                    NavigationBarItem(
                        selected = currentRoute == screen.route,
                        icon = {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = screen.title
                            )
                        },
                        label = { Text(screen.title) },
                        onClick = {
                            tabNavController.navigate(screen.route) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(tabNavController.graph.startDestinationId) { saveState = true }
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            modifier = Modifier.fillMaxSize().padding(bottom = paddingValues.calculateBottomPadding()),
            navController = tabNavController,
            startDestination = Screens.Profile.route
        ) {
            composable(Screens.Profile.route) {
                ProfileScreen()
            }
            composable(Screens.Home.route) {
                HomeHost(
                    onUserClick = {id -> rootNavController.navigate("user/$id")}
                )
            }
            composable(Screens.Rating.route) {
                ParallelsHost(onClassesClick = {id -> rootNavController.navigate("classes/$id")})
            }
        }
    }
}
