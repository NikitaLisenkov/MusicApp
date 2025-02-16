package com.example.musicapp.presentation.main

import com.example.musicapp.presentation.player.player.PlayerScreen
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.musicapp.presentation.downloaded_tracks.DownloadedTracksScreen
import com.example.musicapp.presentation.navigation.AppNavGraph
import com.example.musicapp.presentation.navigation.BottomNavigationBar
import com.example.musicapp.presentation.navigation.Screen
import com.example.musicapp.presentation.navigation.rememberNavState
import com.example.musicapp.presentation.tracks.TracksScreen

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
                    modifier = Modifier.padding(paddingValues),
                    onTrackClick = navState::navigateToPlayer
                )
            },
            tracksScreenContent = {
                TracksScreen(
                    modifier = Modifier.padding(paddingValues),
                    onTrackClick = navState::navigateToPlayer
                )
            },
            playerScreenContent = { trackId ->
                PlayerScreen(
                    modifier = Modifier,
                    trackId = trackId,
                    onBackPressed = navController::popBackStack
                )
            }
        )
    }
}

