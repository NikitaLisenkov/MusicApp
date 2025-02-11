package com.example.someapp.data.repository

import com.example.someapp.data.network.DeezerApi
import com.example.someapp.data.repository.Mapper.toDomainList
import com.example.someapp.domain.model.Track
import com.example.someapp.domain.repository.TracksRepository
import com.example.someapp.utils.runSuspendCatching

class TracksRepositoryImpl(private val api: DeezerApi) : TracksRepository {

    override suspend fun getChartTracks(): List<Track> {
        return runSuspendCatching(
            block = { api.getChartTracks().tracks.tracks.toDomainList() },
            onError = { e -> println("Ошибка при загрузке чартов: ${e.message}") }
        ) ?: emptyList()
    }

    override suspend fun searchTracks(query: String): List<Track> {
        return runSuspendCatching(
            block = { api.searchTracks(query).tracks.toDomainList() },
            onError = { e -> println("Ошибка при поиске треков: ${e.message}") }
        ) ?: emptyList()
    }
}

