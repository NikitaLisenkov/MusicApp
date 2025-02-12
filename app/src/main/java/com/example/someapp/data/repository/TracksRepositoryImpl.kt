package com.example.someapp.data.repository

import com.example.someapp.data.network.DeezerApi
import com.example.someapp.data.repository.Mapper.toDomain
import com.example.someapp.data.repository.Mapper.toDomainList
import com.example.someapp.domain.tracks.model.Track
import com.example.someapp.domain.tracks.repository.TracksRepository
import com.example.someapp.utils.runSuspendCatching
import javax.inject.Inject

class TracksRepositoryImpl @Inject constructor(
    private val api: DeezerApi
) : TracksRepository {

    override suspend fun getChartTracks(): Result<List<Track>> {
        return runSuspendCatching { api.getChartTracks().tracks.data.toDomainList() }
    }

    override suspend fun searchTracks(query: String): Result<List<Track>> {
        return runSuspendCatching { api.searchTracks(query).data.toDomainList() }
    }

    override suspend fun getTrackById(trackId: Long): Result<Track> {
        return runSuspendCatching { api.getTrackById(trackId).toDomain() }
    }
}

