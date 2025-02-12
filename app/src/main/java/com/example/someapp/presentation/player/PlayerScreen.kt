package com.example.someapp.presentation.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.someapp.domain.tracks.model.Track
import com.example.someapp.presentation.base.ErrorScreen
import com.example.someapp.presentation.base.LoadingScreen
import com.example.someapp.presentation.getApplicationComponent
import com.example.someapp.presentation.navigation.NavigationState
import com.example.someapp.presentation.navigation.Screen
import com.example.someapp.utils.formatTime


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(trackId: Long, navState: NavigationState) {

    val component = getApplicationComponent()
    val viewModel: PlayerViewModel = viewModel(factory = component.getViewModelFactory())
    val screenState = viewModel.screenState.collectAsStateWithLifecycle()
    val currentState = screenState.value

    LaunchedEffect(trackId) {
        viewModel.loadTrackById(trackId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Now Playing") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (!navState.navController.popBackStack()) {
                            navState.navController.navigate(Screen.Tracks.route)
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (currentState) {
                is PlayerScreenState.Loading -> LoadingScreen()

                is PlayerScreenState.Content -> AudioExoPlayer(viewModel, currentState.track)

                is PlayerScreenState.Error -> ErrorScreen(
                    message = currentState.message,
                    onRetryClick = { viewModel.loadTrackById(trackId) }
                )
            }
        }
    }
}

@Composable
fun AudioExoPlayer(viewModel: PlayerViewModel, track: Track) {
    val playerState by viewModel.playerState.collectAsStateWithLifecycle()
    val currentPosition by viewModel.currentPosition.collectAsStateWithLifecycle()
    val duration by viewModel.duration.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = track.album.coverUrl,
            contentDescription = "Album Cover",
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(16.dp))
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(track.title, style = MaterialTheme.typography.headlineMedium)
        Text(track.artist.name, style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(16.dp))

        when (playerState) {
            is PlayerState.Playing -> Text("Playing")
            is PlayerState.Paused -> Text("Paused")
            is PlayerState.Buffering -> Text("Buffering...")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.Center) {
            IconButton(onClick = { viewModel.previousTrack() }) {
                Icon(Icons.Default.SkipPrevious, contentDescription = null)
            }

            IconButton(onClick = {
                if (playerState is PlayerState.Playing) {
                    viewModel.pause()
                } else {
                    viewModel.play()
                }
            }) {
                Icon(
                    imageVector = if (playerState is PlayerState.Playing) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = null
                )
            }

            IconButton(onClick = { viewModel.nextTrack() }) {
                Icon(Icons.Default.SkipNext, contentDescription = null)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Slider(
            value = (currentPosition.toFloat() / duration.coerceAtLeast(1)).coerceIn(0f, 1f),
            onValueChange = { newValue ->
                viewModel.seekTo((newValue * duration).toLong())
            }
        )

        val currentPositionFormatted = formatTime(currentPosition)
        val durationFormatted = formatTime(duration)
        Text("Progress: $currentPositionFormatted / $durationFormatted")
    }
}







