package com.example.musicapp.domain.repository


import android.net.Uri
import androidx.annotation.WorkerThread
import com.example.musicapp.data.repository.TracksRepositoryImpl
import com.example.musicapp.domain.model.Track

interface TracksRepository {
    suspend fun getRemoteTracks(fromCache: Boolean): Result<List<Track>>
    suspend fun searchTracks(query: String): Result<List<Track>>
    suspend fun getTrackById(trackId: Long): Result<Track>

    @WorkerThread
    suspend fun loadToLocalStorage(track: Track): Uri?

    @WorkerThread
    fun getAllDownloadedTracks(): Set<TracksRepositoryImpl.DownloadState.Completed>
}