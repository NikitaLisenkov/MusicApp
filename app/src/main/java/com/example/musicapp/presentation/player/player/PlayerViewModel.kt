package com.example.musicapp.presentation.player.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.usecase.GetChartTracksUseCase
import com.example.musicapp.utils.runSuspendCatching
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlayerViewModel @Inject constructor(
    private val playerManager: PlayerManager,
    private val getChartTracksUseCase: GetChartTracksUseCase,
) : ViewModel() {

    val playerState: StateFlow<PlayerState> get() = playerManager.playerState
    val currentPosition: StateFlow<Long> get() = playerManager.currentPosition
    val duration: StateFlow<Long> get() = playerManager.duration

    private val _screenState = MutableStateFlow<PlayerScreenState>(PlayerScreenState.Loading)
    val screenState: StateFlow<PlayerScreenState> = _screenState.asStateFlow()

    fun loadTrackById(trackId: Long) {
        viewModelScope.launch {
            val currentTrack = playerManager.currentTrack.value
            if (currentTrack != null && currentTrack.id == trackId) {
                _screenState.value = PlayerScreenState.Content(currentTrack)
                return@launch
            }

            _screenState.value = PlayerScreenState.Loading

            runSuspendCatching { getChartTracksUseCase.invoke(fromCache = true) }
                .onSuccess { result ->
                    val tracks = result.getOrThrow()
                    val trackIndex = tracks.indexOfFirst { it.id == trackId }

                    if (trackIndex == -1) {
                        _screenState.value = PlayerScreenState.Error("Трек с id=$trackId не найден")
                        return@onSuccess
                    }

                    val targetTrack = tracks[trackIndex]
                    _screenState.value = PlayerScreenState.Content(targetTrack)
                    playerManager.setPlaylist(tracks, startIndex = trackIndex)
                }
                .onFailure { error ->
                    _screenState.value = PlayerScreenState.Error(
                        error.message ?: "Ошибка загрузки"
                    )
                }
        }
    }

    fun nextTrack() {
        playerManager.nextTrack()
        updateCurrentTrack()
    }

    fun previousTrack() {
        playerManager.previousTrack()
        updateCurrentTrack()
    }

    fun play() {
        playerManager.play()
    }

    fun pause() {
        playerManager.pause()
    }

    fun restart() {
        playerManager.restart()
    }

    fun seekTo(positionMs: Long) {
        playerManager.seekTo(positionMs)
    }

    private fun updateCurrentTrack() {
        val state = _screenState.value as? PlayerScreenState.Content ?: return
        val currentTrack = playerManager.currentTrack.value ?: return
        _screenState.value = state.copy(track = currentTrack)
    }
}







