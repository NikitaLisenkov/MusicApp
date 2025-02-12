package com.example.someapp.domain.tracks.repository


import com.example.someapp.domain.tracks.model.Track

interface TracksRepository {
    suspend fun getChartTracks(): Result<List<Track>>
    suspend fun searchTracks(query: String): Result<List<Track>>
    suspend fun getTrackById(trackId: Long): Result<Track>
}