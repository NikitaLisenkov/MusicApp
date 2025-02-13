package com.example.someapp.presentation.downloaded_tracks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.someapp.R

@Composable
fun DownloadedTracksScreen(
    onTrackClick: (Long) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(listOf(1L, 2L, 3L)) { trackId ->
            Text(
                text = stringResource(id = R.string.downloaded_track, trackId),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onTrackClick(trackId) }
                    .padding(16.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}


