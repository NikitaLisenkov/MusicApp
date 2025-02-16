package com.example.musicapp.presentation.tracks

import com.example.musicapp.domain.model.Track

sealed class TracksScreenState {
    data object Loading : TracksScreenState()
    data class Content(val tracks: List<Track>) : TracksScreenState()
    data class Error(val message: String) : TracksScreenState()
}
