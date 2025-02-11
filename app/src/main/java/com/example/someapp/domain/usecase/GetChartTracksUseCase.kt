package com.example.someapp.domain.usecase

import com.example.someapp.domain.model.Track
import com.example.someapp.domain.repository.TracksRepository

class GetChartTracksUseCase(private val repo: TracksRepository) {
    suspend operator fun invoke(): List<Track> {
        return repo.getChartTracks()
    }
}