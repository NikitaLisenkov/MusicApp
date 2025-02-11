package com.example.someapp.domain.tracks.usecase

import com.example.someapp.domain.tracks.model.Track
import com.example.someapp.domain.tracks.repository.TracksRepository
import javax.inject.Inject

class GetChartTracksUseCase @Inject constructor(
    private val repo: TracksRepository
) {
    suspend operator fun invoke(): List<Track> {
        return repo.getChartTracks()
    }
}