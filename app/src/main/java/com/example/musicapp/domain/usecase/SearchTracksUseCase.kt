package com.example.musicapp.domain.usecase

import com.example.musicapp.domain.model.Track
import com.example.musicapp.domain.repository.TracksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchTracksUseCase @Inject constructor(
    private val repo: TracksRepository,
    private val linkWithLocalTracksUseCase: LinkWithLocalTracksUseCase
) {
    suspend operator fun invoke(query: String): Result<List<Track>> = withContext(Dispatchers.IO) {
        repo.searchTracks(query).map { remoteTracks ->
            linkWithLocalTracksUseCase.invoke(remoteTracks)
        }
    }
}