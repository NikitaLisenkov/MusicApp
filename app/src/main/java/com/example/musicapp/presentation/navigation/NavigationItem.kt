package com.example.musicapp.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.musicapp.R

sealed class NavigationItem(
    val screen: Screen,
    @StringRes val titleRes: Int,
    val icon: ImageVector
) {
    data object Tracks : NavigationItem(
        screen = Screen.Tracks,
        titleRes = R.string.navigation_item_tracks,
        icon = Icons.Default.LibraryMusic
    )

    data object Downloaded : NavigationItem(
        screen = Screen.Downloaded,
        titleRes = R.string.navigation_item_downloaded,
        icon = Icons.Default.Download
    )
}
