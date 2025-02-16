package com.example.musicapp.presentation.common

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.musicapp.domain.model.Album
import com.example.musicapp.domain.model.Artist
import com.example.musicapp.domain.model.Track
import com.example.musicapp.presentation.theme.MusicAppTheme

@Composable
fun TrackList(
    tracks: List<Track>,
    onTrackClick: (id: Long) -> Unit,
    onActionClick: (Track) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(
            items = tracks,
            key = { it.id }
        ) { track ->
            TrackCard(
                track = track,
                onTrackClick = { onTrackClick(track.id) },
                onActionClick = { onActionClick(track) }
            )
        }
    }
}

@Composable
private fun TrackCard(
    track: Track,
    onTrackClick: () -> Unit,
    onActionClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onTrackClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp)),
                model = track.album.coverUrl,
                contentDescription = null,
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.titleMedium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    text = track.title
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    text = track.artist.name,
                )
            }

            IconButton(onClick = onActionClick) {
                Crossfade(track.type) { fadeType ->
                    when (fadeType) {
                        Track.Type.REMOTE -> {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                imageVector = Icons.Default.Download,
                                contentDescription = null
                            )
                        }

                        Track.Type.LOCAL -> {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                imageVector = Icons.Default.CheckCircleOutline,
                                contentDescription = null
                            )
                        }

                        Track.Type.DOWNLOADING -> {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
private fun TracksPreview() {
    MusicAppTheme {
        TrackCard(
            track = Track(
                id = 0,
                title = "Track",
                previewUrl = "",
                artist = Artist(id = 0, name = "Ivan"),
                album = Album(id = 0, coverUrl = ""),
                type = Track.Type.LOCAL
            ),
            onTrackClick = {},
            onActionClick = {}
        )
    }
}