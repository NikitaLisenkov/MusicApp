package com.example.someapp.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.someapp.presentation.downloaded_tracks.DownloadedTracksScreen
import com.example.someapp.presentation.player.PlayerScreen
import com.example.someapp.presentation.tracks.TracksScreen

@Composable
fun AppNavGraph(
    navState: NavigationState,
    paddingValues: PaddingValues
) {
    NavHost(
        navController = navState.navController,
        startDestination = Screen.Tracks.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(Screen.Downloaded.route) {
            DownloadedTracksScreen()
        }
        composable(Screen.Tracks.route) {
            TracksScreen(navState)
        }
        composable(
            route = Screen.Player.route,
            arguments = listOf(navArgument("trackId") { type = NavType.StringType })
        ) { backStackEntry ->
            val trackId = backStackEntry.arguments?.getString("trackId")?.toLongOrNull() ?: 0L
            PlayerScreen(trackId, navState)
        }

    }
}





