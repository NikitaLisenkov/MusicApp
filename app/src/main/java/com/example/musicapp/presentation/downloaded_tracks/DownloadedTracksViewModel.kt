package com.example.musicapp.presentation.downloaded_tracks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.model.Track
import com.example.musicapp.domain.usecase.GetChartTracksUseCase
import com.example.musicapp.utils.runSuspendCatching
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class DownloadedTracksViewModel @Inject constructor(
    private val getChartTracksUseCase: GetChartTracksUseCase,
) : ViewModel() {

    private val _state: MutableStateFlow<DownloadedTracksState> =
        MutableStateFlow(DownloadedTracksState.Loading)
    val state: StateFlow<DownloadedTracksState> = _state.asStateFlow()

    private var job: Job? = null

    private var currentTracks: List<Track> = emptyList()

    private val searchQuery = MutableSharedFlow<String>(
        extraBufferCapacity = 20,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    init {
        fetchTracks()

        searchQuery
            .distinctUntilChanged()
            .debounce(100)
            .onEach(::filterTracks)
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

    fun fetchTracks(refresh: Boolean = false) {
        job = viewModelScope.launch(Dispatchers.IO) {
            val currentState = _state.value
            if (refresh && currentState is DownloadedTracksState.Content) {
                _state.update {
                    currentState.copy(refreshing = true)
                }
            }
            loadTracks(refresh)
        }
    }

    fun searchTracks(query: String) {
        viewModelScope.launch {
            searchQuery.emit(query)
        }
    }

    private suspend fun loadTracks(refresh: Boolean) {
        runSuspendCatching { getChartTracksUseCase.invoke(fromCache = refresh) }
            .onSuccess { tracks ->
                _state.update {
                    currentTracks = tracks
                        .getOrThrow()
                        .filter { it.type == Track.Type.LOCAL }

                    DownloadedTracksState.Content(
                        tracks = currentTracks,
                        refreshing = false
                    )
                }
            }
            .onFailure { error ->
                _state.update {
                    DownloadedTracksState.Error(
                        error.message ?: "Ошибка загрузки"
                    )
                }
            }
    }

    private fun filterTracks(query: String) {
        val currentState = _state.value as? DownloadedTracksState.Content ?: return
        val tracks = if (query.isBlank()) {
            currentTracks
        } else {
            currentTracks.filter {
                it.title.contains(
                    other = query,
                    ignoreCase = true
                )
            }
        }
        _state.value = currentState.copy(
            tracks = tracks
        )
    }
}