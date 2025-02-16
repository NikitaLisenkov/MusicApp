package com.example.musicapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController


class NavigationState(val navHostController: NavHostController) {

    fun navigateTo(route: String) {
        navHostController.navigate(route) {
            popUpTo(navHostController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToPlayer(trackId: Long) {
        navHostController.navigate(Screen.Player.createRoute(trackId))
    }
}


@Composable
fun rememberNavState(
    navHostController: NavHostController = rememberNavController()
): NavigationState {
    return remember(navHostController) { NavigationState(navHostController) }
}



