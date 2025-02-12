package com.example.someapp.presentation.player

sealed class PlayerState {
    data object Playing : PlayerState()
    data object Paused : PlayerState()
    data object Buffering : PlayerState()
}