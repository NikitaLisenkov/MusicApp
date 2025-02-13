package com.example.someapp.presentation.main

import PlayerScreen
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.someapp.presentation.downloaded_tracks.DownloadedTracksScreen
import com.example.someapp.presentation.navigation.AppNavGraph
import com.example.someapp.presentation.navigation.BottomNavigationBar
import com.example.someapp.presentation.navigation.Screen
import com.example.someapp.presentation.navigation.rememberNavState
import com.example.someapp.presentation.tracks.TracksScreen

@Composable
fun MusicMainContainer() {
    val navController = rememberNavController()
    val navState = rememberNavState(navController)

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute?.startsWith(Screen.Player.ROUTE_FOR_ARGS) != true) {
                BottomNavigationBar(navState)
            }
        }
    ) { paddingValues ->
        AppNavGraph(
            navHostController = navController,
            downloadedTracksScreenContent = {
                DownloadedTracksScreen(
                    onTrackClick = { trackId ->
                        navState.navigateToPlayer(trackId)
                    }
                )
            },
            tracksScreenContent = {
                TracksScreen(
                    paddingValues = paddingValues,
                    onTrackClick = { trackId ->
                        navState.navigateToPlayer(trackId)
                    }
                )
            },
            playerScreenContent = { trackId ->
                PlayerScreen(
                    trackId = trackId,
                    paddingValues = paddingValues,
                    onBackPressed = { navController.popBackStack() }
                )
            }
        )
    }
}

