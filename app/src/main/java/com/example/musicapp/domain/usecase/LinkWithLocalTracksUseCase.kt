package com.example.musicapp.domain.usecase

import com.example.musicapp.domain.model.Track
import com.example.musicapp.domain.repository.TracksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LinkWithLocalTracksUseCase @Inject constructor(
    private val repo: TracksRepository,
) {

    suspend operator fun invoke(remoteTracks: List<Track>): List<Track> =
        withContext(Dispatchers.IO) {
            val localTracks = repo.getAllDownloadedTracks()
            val localTracksNames = localTracks
                .map { it.title }
                .toSet()

            remoteTracks.map { remoteTrack ->
                if (remoteTrack.title in localTracksNames) {
                    val localTrack = localTracks.first { it.title == remoteTrack.title }

                    remoteTrack.copy(
                        type = Track.Type.LOCAL,
                        uri = localTrack.uri
                    )
                } else {
                    remoteTrack
                }
            }
        }
}