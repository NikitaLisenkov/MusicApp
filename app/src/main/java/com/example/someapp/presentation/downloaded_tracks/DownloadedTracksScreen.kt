package com.example.someapp.presentation.downloaded_tracks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.someapp.presentation.navigation.rememberNavState

@Composable
fun DownloadedTracksScreen() {
    val navState = rememberNavState()

    LazyColumn {
        items(listOf("Track 1", "Track 2", "Track 3")) { track ->
            Text(
                text = track,
                modifier = Modifier.clickable {
                    navState.navigateToPlayer(track)
                }
            )
        }
    }
}
