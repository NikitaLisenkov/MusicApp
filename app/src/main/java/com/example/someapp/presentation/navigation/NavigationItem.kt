package com.example.someapp.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DownloadDone
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.someapp.R

sealed class NavigationItem(
    val screen: Screen,
    @StringRes val titleResId: Int,
    val icon: ImageVector
) {
    data object Downloaded : NavigationItem(
        screen = Screen.Downloaded,
        titleResId = R.string.navigation_item_downloaded,
        icon = Icons.Outlined.DownloadDone
    )

    data object Tracks : NavigationItem(
        screen = Screen.Tracks,
        titleResId = R.string.navigation_item_tracks,
        icon = Icons.Outlined.LibraryMusic
    )
}
