package com.example.musicapp.domain.model

import android.net.Uri

data class Track(
    val id: Long,
    val title: String,
    val previewUrl: String,
    val artist: Artist,
    val album: Album,
    val type: Type,
    val uri: Uri? = null
) {
    enum class Type {
        REMOTE,
        DOWNLOADING,
        LOCAL,
    }
}

data class Artist(
    val id: Long,
    val name: String
)

data class Album(
    val id: Long,
    val coverUrl: String
)

