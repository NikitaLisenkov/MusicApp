package com.example.musicapp.presentation.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navState: NavigationState) {
    val items = listOf(
        NavigationItem.Tracks,
        NavigationItem.Downloaded
    )
    NavigationBar {
        val currentRoute =
            navState.navHostController.currentBackStackEntryAsState().value?.destination?.route

        items.forEach { item ->
            val selected = currentRoute == item.screen.route

            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = null) },
                label = { Text(text = stringResource(id = item.titleRes)) },
                selected = selected,
                onClick = { navState.navigateTo(item.screen.route) }
            )
        }
    }
}



