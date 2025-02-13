package com.example.someapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument


@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    tracksScreenContent: @Composable () -> Unit,
    downloadedTracksScreenContent: @Composable () -> Unit,
    playerScreenContent: @Composable (Long) -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Tracks.route
    ) {

        composable(Screen.Tracks.route) {
            tracksScreenContent()
        }

        composable(Screen.Downloaded.route) {
            downloadedTracksScreenContent()
        }

        composable(
            route = Screen.Player.route,
            arguments = listOf(navArgument(Screen.KEY_TRACK_ID) { type = NavType.LongType })
        ) { backStackEntry ->
            val trackId = backStackEntry.arguments?.getLong(Screen.KEY_TRACK_ID) ?: 0L
            playerScreenContent(trackId)
        }
    }
}







