package com.example.someapp.domain.usecase

import com.example.someapp.domain.model.Track
import com.example.someapp.domain.repository.TracksRepository

class SearchTracksUseCase(private val repo: TracksRepository) {
    suspend operator fun invoke(query: String): List<Track> {
        return repo.searchTracks(query)
    }
}