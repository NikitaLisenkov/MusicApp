package com.example.someapp.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.someapp.di.ServiceLocator
import com.example.someapp.domain.model.Track


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TracksScreen() {
    val viewModel: TracksViewModel = viewModel(
        factory = TracksViewModelFactory(
            getChartTracksUseCase = ServiceLocator.getChartTracksUseCase,
            searchTracksUseCase = ServiceLocator.searchTracksUseCase
        )
    )
    val state by viewModel.state.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Music") },
                actions = {
                    SearchBar(
                        query = searchQuery,
                        onQueryChanged = {
                            searchQuery = it
                            viewModel.searchTracks(it)
                        }
                    )
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when (state) {
                is TracksScreenState.Loading -> LoadingScreen()
                is TracksScreenState.Content -> {
                    val tracks = (state as TracksScreenState.Content).tracks
                    TrackList(tracks)
                }

                is TracksScreenState.Error -> {
                    val message = (state as TracksScreenState.Error).message
                    ErrorScreen(message = message, onRetryClick = { viewModel.fetchTracks() })
                }
            }
        }
    }
}


@Composable
fun SearchBar(query: String, onQueryChanged: (String) -> Unit) {
    var text by remember { mutableStateOf(query) }

    TextField(
        value = text,
        onValueChange = {
            text = it
            onQueryChanged(it)
        },
        placeholder = { Text("Search tracks...") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        singleLine = true
    )
}


@Composable
fun TrackList(tracks: List<Track>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(tracks, key = { it.id }) { track ->
            TrackCard(track)
        }
    }
}


@Composable
fun TrackCard(track: Track) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = track.album.coverUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = track.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = track.artist.name,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}


@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}


@Composable
fun ErrorScreen(message: String, onRetryClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRetryClick) {
            Text("Retry")
        }
    }
}