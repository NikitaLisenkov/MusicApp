package com.example.musicapp.presentation.downloaded_tracks

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.musicapp.R
import com.example.musicapp.presentation.common.ErrorScreen
import com.example.musicapp.presentation.common.LoadingScreen
import com.example.musicapp.presentation.common.SearchBar
import com.example.musicapp.presentation.common.TrackList
import com.example.musicapp.presentation.getApplicationComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadedTracksScreen(
    onTrackClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val component = getApplicationComponent()
    val viewModel: DownloadedTracksViewModel = viewModel(factory = component.viewModelFactory)
    val screenState = viewModel.state.collectAsState()
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val pullRefreshState = rememberPullToRefreshState()

    LaunchedEffect(Unit) {
        viewModel.fetchTracks(refresh = true)
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.music_title)) },
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
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when (val currentState = screenState.value) {
                is DownloadedTracksState.Loading -> {
                    LoadingScreen()
                }

                is DownloadedTracksState.Content -> {
                    PullToRefreshBox(
                        isRefreshing = currentState.refreshing,
                        onRefresh = { viewModel.fetchTracks(refresh = true) },
                        state = pullRefreshState,
                    ) {
                        TrackList(
                            tracks = currentState.tracks,
                            onTrackClick = onTrackClick,
                            onActionClick = {}
                        )
                    }
                }

                is DownloadedTracksState.Error -> {
                    ErrorScreen(
                        message = currentState.message,
                        onRetryClick = viewModel::fetchTracks
                    )
                }
            }
        }
    }
}


