package com.example.someapp.domain.model

data class Track(
    val id: Long,
    val title: String,
    val previewUrl: String,
    val artist: Artist,
    val album: Album
)

data class Artist(
    val id: Long,
    val name: String
)

data class Album(
    val id: Long,
    val coverUrl: String
)

