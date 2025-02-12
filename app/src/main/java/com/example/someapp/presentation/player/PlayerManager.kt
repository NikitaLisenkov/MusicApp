package com.example.someapp.presentation.player

import android.annotation.SuppressLint
import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PlayerManager(context: Context) {

    private val _playerState = MutableStateFlow<PlayerState>(PlayerState.Paused)
    val playerState: StateFlow<PlayerState> get() = _playerState

    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> get() = _currentPosition

    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> get() = _duration

    private val player = ExoPlayer.Builder(context).build()

    init {
        player.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _playerState.value = if (isPlaying) PlayerState.Playing else PlayerState.Paused
            }

            override fun onPlaybackStateChanged(state: Int) {
                if (state == ExoPlayer.STATE_BUFFERING) {
                    _playerState.value = PlayerState.Buffering
                }
            }

            @SuppressLint("UnsafeOptInUsageError")
            override fun onPositionDiscontinuity(reason: Int) {
                _currentPosition.value = player.currentPosition
                _duration.value = player.duration
            }

            override fun onPositionDiscontinuity(
                oldPosition: Player.PositionInfo,
                newPosition: Player.PositionInfo,
                reason: Int
            ) {
                super.onPositionDiscontinuity(oldPosition, newPosition, reason)
            }
        })

    }

    fun play(audioUrl: String) {
        if (player.currentMediaItem == null) {
            val mediaItem = MediaItem.fromUri(audioUrl)
            player.setMediaItem(mediaItem)
            player.prepare()
        }
        player.playWhenReady = true
    }

    fun pause() {
        player.pause()
    }

    fun restart() {
        player.seekTo(0)
        player.playWhenReady = true
    }

    fun release() {
        player.release()
    }

    fun updateProgress() {
        _currentPosition.value = player.currentPosition
        _duration.value = player.duration
    }
}

