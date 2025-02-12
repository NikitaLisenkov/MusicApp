package com.example.someapp.presentation.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlayerViewModel @Inject constructor(
    private val playerManager: PlayerManager
) : ViewModel() {

    val playerState: StateFlow<PlayerState> get() = playerManager.playerState
    val currentPosition: StateFlow<Long> get() = playerManager.currentPosition
    val duration: StateFlow<Long> get() = playerManager.duration

    private var updateJob: Job? = null

    fun play() {
        playerManager.play("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3")
        startUpdatingProgress()
    }

    fun pause() {
        playerManager.pause()
        stopUpdatingProgress()
    }

    fun restart() {
        playerManager.restart()
        startUpdatingProgress()
    }

    fun startUpdatingProgress() {
        updateJob?.cancel()
        updateJob = viewModelScope.launch {
            while (true) {
                playerManager.updateProgress()
                delay(1000)
            }
        }
    }

    private fun stopUpdatingProgress() {
        updateJob?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        playerManager.release()
        updateJob?.cancel()
    }
}
