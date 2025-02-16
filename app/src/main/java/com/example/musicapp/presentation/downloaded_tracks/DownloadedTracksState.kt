package com.example.musicapp.presentation.downloaded_tracks

import com.example.musicapp.domain.model.Track

sealed class DownloadedTracksState {
    data object Loading : DownloadedTracksState()
    data class Content(
        val tracks: List<Track>,
        val refreshing: Boolean
    ) : DownloadedTracksState()

    data class Error(val message: String) : DownloadedTracksState()
}