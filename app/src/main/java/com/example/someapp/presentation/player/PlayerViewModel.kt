package com.example.someapp.presentation.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.someapp.domain.tracks.model.Track
import com.example.someapp.domain.tracks.usecase.GetTrackByIdUseCase
import com.example.someapp.utils.runSuspendCatching
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class PlayerViewModel @Inject constructor(
    private val playerManager: PlayerManager,
    private val getTrackByIdUseCase: GetTrackByIdUseCase
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

            runSuspendCatching { getTrackByIdUseCase.invoke(trackId) }
                .onSuccess { result ->
                    val track = result.getOrThrow()
                    _screenState.value = PlayerScreenState.Content(track)
                    // Для переключения между треками желательно передавать полноценный плейлист.
                    playerManager.setPlaylist(listOf(track), startIndex = 0)
                }
                .onFailure { error ->
                    _screenState.value = PlayerScreenState.Error(error.message ?: "Ошибка загрузки")
                }
        }
    }

    fun setPlaylist(tracks: List<Track>, startIndex: Int = 0) {
        playerManager.setPlaylist(tracks, startIndex)
        _screenState.value = PlayerScreenState.Content(tracks[startIndex])
    }

    fun nextTrack() {
        playerManager.nextTrack()
    }

    fun previousTrack() {
        playerManager.previousTrack()
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
}






