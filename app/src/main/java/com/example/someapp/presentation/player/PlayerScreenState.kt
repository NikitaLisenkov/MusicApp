package com.example.someapp.presentation.player

import com.example.someapp.domain.tracks.model.Track

sealed class PlayerScreenState {
    data object Loading : PlayerScreenState()
    data class Content(val track: Track) : PlayerScreenState()
    data class Error(val message: String) : PlayerScreenState()
}
