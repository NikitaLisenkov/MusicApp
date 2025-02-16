package com.example.musicapp.presentation.tracks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.model.Track
import com.example.musicapp.domain.repository.TracksRepository
import com.example.musicapp.domain.usecase.GetChartTracksUseCase
import com.example.musicapp.domain.usecase.SearchTracksUseCase
import com.example.musicapp.utils.runSuspendCatching
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class TracksViewModel @Inject constructor(
    private val getChartTracksUseCase: GetChartTracksUseCase,
    private val searchTracksUseCase: SearchTracksUseCase,
    private val repo: TracksRepository
) : ViewModel() {

    private val _state: MutableStateFlow<TracksScreenState> = MutableStateFlow(TracksScreenState.Loading)
    val state: StateFlow<TracksScreenState> = _state.asStateFlow()

    private var currentQuery: String? = null

    init {
        fetchTracks()
    }

    fun fetchTracks() {
        viewModelScope.launch {
            _state.value = TracksScreenState.Loading

            runSuspendCatching(getChartTracksUseCase::invoke)
                .onSuccess { tracks ->
                    _state.update { TracksScreenState.Content(tracks.getOrThrow()) }
                    currentQuery = null
                }
                .onFailure { error ->
                    _state.update { TracksScreenState.Error(error.message ?: "Ошибка загрузки") }
                }
        }
    }

    fun searchTracks(query: String) {
        if (query.isBlank()) {
            fetchTracks()
            return
        }
        if (query == currentQuery) return

        currentQuery = query
        viewModelScope.launch {
            _state.value = TracksScreenState.Loading

            runSuspendCatching { searchTracksUseCase.invoke(query) }
                .onSuccess { tracks ->
                    _state.value = TracksScreenState.Content(tracks.getOrThrow())
                }
                .onFailure { error ->
                    _state.value = TracksScreenState.Error(error.message ?: "Ошибка поиска")
                }
        }
    }

    fun onActionClick(track: Track) {
        when (track.type) {
            Track.Type.REMOTE -> {
                viewModelScope.launch(Dispatchers.IO) {
                    updateTrack(track, Track.Type.DOWNLOADING)

                    val newType = repo.loadToLocalStorage(track)
                        ?.let { Track.Type.LOCAL }
                        ?: Track.Type.REMOTE

                    updateTrack(track, newType)
                }
            }

            Track.Type.DOWNLOADING, Track.Type.LOCAL -> {}
        }
    }

    private fun updateTrack(track: Track, newType: Track.Type) {
        val content = _state.value as? TracksScreenState.Content ?: return
        _state.update {
            content.copy(
                tracks = content.tracks.map {
                    if (it.id == track.id) {
                        track.copy(type = newType)
                    } else {
                        it
                    }
                }
            )
        }
    }
}
