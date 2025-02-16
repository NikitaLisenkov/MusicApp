package com.example.musicapp.presentation.player.player

import com.example.musicapp.domain.model.Track

sealed class PlayerScreenState {
    data object Loading : PlayerScreenState()
    data class Content(val track: Track) : PlayerScreenState()
    data class Error(val message: String) : PlayerScreenState()
}
