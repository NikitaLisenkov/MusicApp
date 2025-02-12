package com.example.someapp.presentation.player

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.someapp.presentation.getApplicationComponent
import com.example.someapp.utils.formatTime


@Composable
fun PlayerScreen() {
    val component = getApplicationComponent()
    val viewModel: PlayerViewModel = viewModel(factory = component.getViewModelFactory())

    AudioExoPlayer(viewModel = viewModel)
}


@Composable
fun AudioExoPlayer(viewModel: PlayerViewModel) {
    val playerState by viewModel.playerState.collectAsStateWithLifecycle()
    val currentPosition by viewModel.currentPosition.collectAsStateWithLifecycle()
    val duration by viewModel.duration.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.startUpdatingProgress()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Audio Player", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        when (playerState) {
            is PlayerState.Playing -> Text("Playing")
            is PlayerState.Paused -> Text("Paused")
            is PlayerState.Buffering -> Text("Buffering...")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.Center) {
            IconButton(onClick = {
                if (playerState is PlayerState.Playing) {
                    viewModel.pause()
                } else {
                    viewModel.play()
                }
            }) {
                Icon(
                    imageVector = if (playerState is PlayerState.Playing) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (playerState is PlayerState.Playing) "Pause" else "Play"
                )
            }

            IconButton(onClick = { viewModel.restart() }) {
                Icon(Icons.Default.Replay, contentDescription = "Restart")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val safeDuration = duration.coerceAtLeast(0)
        val progress = if (safeDuration > 0) (currentPosition.toFloat() / safeDuration).coerceIn(0f, 1f) else 0f

        val animatedProgress by animateFloatAsState(
            targetValue = progress,
            animationSpec = tween(durationMillis = 500),
            label = "Audio Progress Animation"
        )

        LinearProgressIndicator(progress = animatedProgress)

        val currentPositionFormatted = formatTime(currentPosition)
        val durationFormatted = formatTime(safeDuration)
        Text("Progress: $currentPositionFormatted / $durationFormatted")
    }
}



