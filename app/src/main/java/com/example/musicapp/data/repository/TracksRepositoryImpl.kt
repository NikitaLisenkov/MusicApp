package com.example.musicapp.data.repository

import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import androidx.annotation.WorkerThread
import com.example.musicapp.data.network.DeezerApi
import com.example.musicapp.data.repository.Mapper.toDomain
import com.example.musicapp.data.repository.Mapper.toDomainList
import com.example.musicapp.domain.model.Track
import com.example.musicapp.domain.repository.TracksRepository
import com.example.musicapp.utils.runSuspendCatching
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class TracksRepositoryImpl @Inject constructor(
    private val api: DeezerApi,
    private val manager: DownloadManager,
    private val context: Context
) : TracksRepository {

    private var cachedTracks: List<Track> = emptyList()

    private val mutex: Mutex = Mutex()

    override suspend fun getRemoteTracks(fromCache: Boolean): Result<List<Track>> {
        return mutex.withLock {
            if (fromCache) {
                Result.success(cachedTracks)
            } else {
                runSuspendCatching { api.getChartTracks().tracks.data.toDomainList() }
                    .onSuccess { cachedTracks = it }
            }
        }
    }

    override suspend fun searchTracks(query: String): Result<List<Track>> {
        return runSuspendCatching { api.searchTracks(query).data.toDomainList() }
    }

    override suspend fun getTrackById(trackId: Long): Result<Track> {
        return runSuspendCatching { api.getTrackById(trackId).toDomain() }
    }

    @WorkerThread
    override suspend fun loadToLocalStorage(track: Track): Uri? {
        val uri = Uri.parse(track.previewUrl)
        val request = DownloadManager.Request(uri).apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            setTitle(track.title)
            setDescription("Downloading ${track.title}...")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalFilesDir(
                context,
                Environment.DIRECTORY_MUSIC,
                "${track.title}.mp3"
            )
        }
        val downloadId = manager.enqueue(request)

        while (true) {
            when (val status = getDownloadStatus(downloadId)) {
                is DownloadState.Completed -> {
                    return status.uri
                }

                is DownloadState.Downloading -> {
                    delay(200)
                }

                is DownloadState.Error -> {
                    return null
                }
            }
        }
    }

    @WorkerThread
    override fun getAllDownloadedTracks(): Set<DownloadState.Completed> {
        val downloadedTracks = mutableSetOf<DownloadState.Completed>()
        val query = DownloadManager.Query().setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL)

        manager.query(query).use { cursor ->
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    downloadedTracks.add(
                        DownloadState.Completed(
                            title = cursor.getTitle(),
                            uri = cursor.getLocalUri(),
                        )
                    )
                }
            }
        }

        return downloadedTracks
    }

    @WorkerThread
    private fun getDownloadStatus(downloadId: Long): DownloadState {
        val query = DownloadManager.Query().setFilterById(downloadId)
        return manager.query(query).use { cursor ->
            if (cursor != null && cursor.moveToFirst()) {
                val status =
                    cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
                return@use when (status) {
                    DownloadManager.STATUS_RUNNING -> {
                        DownloadState.Downloading
                    }

                    DownloadManager.STATUS_SUCCESSFUL -> {
                        DownloadState.Completed(
                            title = cursor.getTitle(),
                            uri = cursor.getLocalUri(),
                        )
                    }

                    DownloadManager.STATUS_FAILED -> {
                        DownloadState.Error
                    }

                    else -> {
                        DownloadState.Downloading
                    }
                }
            } else {
                DownloadState.Error
            }
        }
    }

    private fun Cursor.getLocalUri(): Uri {
        return Uri.parse(
            this.getString(this.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI))
        )
    }

    private fun Cursor.getTitle(): String {
        return this.getString(this.getColumnIndexOrThrow(DownloadManager.COLUMN_TITLE))
    }

    sealed class DownloadState {
        data object Downloading : DownloadState()
        data class Completed(
            val title: String,
            val uri: Uri
        ) : DownloadState()

        data object Error : DownloadState()
    }
}

