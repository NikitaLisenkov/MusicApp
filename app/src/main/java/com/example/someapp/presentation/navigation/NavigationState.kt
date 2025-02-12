package com.example.someapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

class NavigationState(val navController: NavHostController) {

    fun navigateTo(route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToPlayer(trackId: String) {
        navController.navigate(Screen.Player.createRoute(trackId))
    }
}

@Composable
fun rememberNavState(
    navController: NavHostController = rememberNavController()
): NavigationState {
    return remember(navController) { NavigationState(navController) }
}


