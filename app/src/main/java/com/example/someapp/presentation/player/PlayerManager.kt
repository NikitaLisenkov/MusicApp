package com.example.someapp.presentation.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.someapp.domain.tracks.model.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlayerManager @Inject constructor(context: Context) {

    private val _playerState = MutableStateFlow<PlayerState>(PlayerState.Paused)
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> = _currentPosition.asStateFlow()

    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration.asStateFlow()

    private val _currentTrackIndex = MutableStateFlow(0)
    val currentTrackIndex: StateFlow<Int> = _currentTrackIndex.asStateFlow()

    private val _currentTrack = MutableStateFlow<Track?>(null)
    val currentTrack: StateFlow<Track?> = _currentTrack.asStateFlow()

    private val player = ExoPlayer.Builder(context).build()
    private var trackList: List<Track> = emptyList()

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var updateJob: Job? = null

    init {
        player.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _playerState.value = if (isPlaying) PlayerState.Playing else PlayerState.Paused
                if (isPlaying) startUpdatingProgress() else stopUpdatingProgress()
            }

            override fun onPlaybackStateChanged(state: Int) {
                when (state) {
                    ExoPlayer.STATE_BUFFERING -> _playerState.value = PlayerState.Buffering
                    ExoPlayer.STATE_ENDED -> nextTrack()
                }
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                _duration.value = player.duration
            }

            override fun onPositionDiscontinuity(
                oldPosition: Player.PositionInfo,
                newPosition: Player.PositionInfo,
                reason: Int
            ) {
                _currentPosition.value = newPosition.positionMs
                _duration.value = player.duration
            }
        })
    }

    fun setPlaylist(tracks: List<Track>, startIndex: Int = 0) {
        trackList = tracks
        _currentTrackIndex.value = startIndex
        playCurrentTrack()
    }

    private fun playCurrentTrack() {
        val track = trackList.getOrNull(_currentTrackIndex.value)
        if (track != null) {
            _currentTrack.value = track
            val mediaItem = MediaItem.fromUri(track.previewUrl)
            player.setMediaItem(mediaItem)
            player.prepare()
            player.playWhenReady = true
        } else {
            _playerState.value = PlayerState.Paused
        }
    }

    fun play() {
        player.play()
        startUpdatingProgress()
    }

    fun pause() {
        player.pause()
        stopUpdatingProgress()
    }

    fun restart() {
        player.seekTo(0)
        player.playWhenReady = true
    }

    fun seekTo(positionMs: Long) {
        player.seekTo(positionMs)
        updateProgress()
    }

    fun nextTrack() {
        if (_currentTrackIndex.value < trackList.size - 1) {
            _currentTrackIndex.value += 1
            playCurrentTrack()
        }
    }

    fun previousTrack() {
        if (player.currentPosition < RESTART_THRESHOLD_MS && _currentTrackIndex.value > 0) {
            _currentTrackIndex.value -= 1
            playCurrentTrack()
        } else {
            restart()
        }
    }

    private fun updateProgress() {
        _currentPosition.value = player.currentPosition
        _duration.value = player.duration
    }

    private fun startUpdatingProgress() {
        updateJob?.cancel()
        updateJob = scope.launch {
            while (isActive) {
                updateProgress()
                delay(500)
            }
        }
    }

    private fun stopUpdatingProgress() {
        updateJob?.cancel()
    }

    fun release() {
        stopUpdatingProgress()
        player.release()
        scope.cancel()
    }

    companion object {
        private const val RESTART_THRESHOLD_MS = 3000L
    }
}








