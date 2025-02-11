package com.example.someapp.presentation.tracks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.someapp.domain.tracks.usecase.GetChartTracksUseCase
import com.example.someapp.domain.tracks.usecase.SearchTracksUseCase
import com.example.someapp.utils.runSuspendCatching
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class TracksViewModel @Inject constructor(
    private val getChartTracksUseCase: GetChartTracksUseCase,
    private val searchTracksUseCase: SearchTracksUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<TracksScreenState> =
        MutableStateFlow(TracksScreenState.Loading)
    val state: StateFlow<TracksScreenState> = _state.asStateFlow()

    private var currentQuery: String? = null

    init {
        fetchTracks()
    }

    fun fetchTracks() {
        viewModelScope.launch {
            _state.value = TracksScreenState.Loading

            runSuspendCatching(
                block = { getChartTracksUseCase.invoke() },
                onSuccess = { tracks ->
                    _state.update { TracksScreenState.Content(tracks) }
                    currentQuery = null
                },
                onError = { error ->
                    _state.update { TracksScreenState.Error(error.message ?: "Ошибка загрузки") }
                }
            )
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
            runSuspendCatching(
                block = { searchTracksUseCase.invoke(query) },
                onSuccess = { tracks ->
                    _state.value = TracksScreenState.Content(tracks)
                },
                onError = { error ->
                    _state.value = TracksScreenState.Error(error.message ?: "Ошибка поиска")
                }
            )
        }
    }
}
