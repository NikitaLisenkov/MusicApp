package com.example.someapp.presentation.main

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.someapp.presentation.navigation.AppNavGraph
import com.example.someapp.presentation.navigation.BottomNavigationBar
import com.example.someapp.presentation.navigation.NavigationState
import com.example.someapp.presentation.navigation.Screen

@Composable
fun MusicMainContainer() {
    val navController = rememberNavController()
    val navState = remember { NavigationState(navController) }

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute != Screen.Player.route) {
                BottomNavigationBar(navState)
            }
        }
    ) { paddingValues ->
        AppNavGraph(
            navState = navState,
            paddingValues = paddingValues
        )
    }
}

