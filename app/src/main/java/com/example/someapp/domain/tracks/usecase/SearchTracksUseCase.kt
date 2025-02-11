package com.example.someapp.domain.tracks.usecase

import com.example.someapp.domain.tracks.model.Track
import com.example.someapp.domain.tracks.repository.TracksRepository
import javax.inject.Inject

class SearchTracksUseCase @Inject constructor(
    private val repo: TracksRepository
) {
    suspend operator fun invoke(query: String): List<Track> {
        return repo.searchTracks(query)
    }
}