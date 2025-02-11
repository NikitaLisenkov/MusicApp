package com.example.someapp.domain.tracks.repository


import com.example.someapp.domain.tracks.model.Track

interface TracksRepository {
    suspend fun getChartTracks(): List<Track>
    suspend fun searchTracks(query: String): List<Track>
}