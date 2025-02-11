package com.example.someapp.domain.repository


import com.example.someapp.domain.model.Track

interface TracksRepository {
    suspend fun getChartTracks(): List<Track>
    suspend fun searchTracks(query: String): List<Track>
}