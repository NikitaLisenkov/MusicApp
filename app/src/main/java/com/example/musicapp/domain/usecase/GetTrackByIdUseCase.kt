package com.example.musicapp.domain.usecase

import com.example.musicapp.domain.model.Track
import com.example.musicapp.domain.repository.TracksRepository
import javax.inject.Inject

class GetTrackByIdUseCase @Inject constructor(
    private val repo: TracksRepository
) {
    suspend operator fun invoke(trackId: Long): Result<Track> {
        return repo.getTrackById(trackId)
    }
}
