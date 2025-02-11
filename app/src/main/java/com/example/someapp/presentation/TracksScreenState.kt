package com.example.someapp.presentation

import com.example.someapp.domain.model.Track

sealed class TracksScreenState {
    data object Loading : TracksScreenState()
    data class Content(val tracks: List<Track>) : TracksScreenState()
    data class Error(val message: String) : TracksScreenState()
}
