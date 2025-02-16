package com.example.musicapp.presentation.player.service

sealed class PlayerAction(val action: String) {
    data object Play : PlayerAction("action_play")
    data object Pause : PlayerAction("action_pause")
    data object Next : PlayerAction("action_next")
    data object Previous : PlayerAction("action_previous")
    data object StartService : PlayerAction("action_start_service")

    companion object {
        fun fromString(action: String?): PlayerAction? = when (action) {
            Play.action -> Play
            Pause.action -> Pause
            Next.action -> Next
            Previous.action -> Previous
            StartService.action -> StartService
            else -> null
        }
    }
}
