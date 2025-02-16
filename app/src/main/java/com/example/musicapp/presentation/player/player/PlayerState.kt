package com.example.musicapp.presentation.player.player

sealed class PlayerState {
    data object Playing : PlayerState()
    data object Paused : PlayerState()
    data object Buffering : PlayerState()
}