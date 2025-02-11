package com.example.someapp.data.repository

import com.example.someapp.data.network.model.AlbumResponse
import com.example.someapp.data.network.model.ArtistResponse
import com.example.someapp.data.network.model.TrackResponse
import com.example.someapp.domain.model.Album
import com.example.someapp.domain.model.Artist
import com.example.someapp.domain.model.Track

object Mapper {

    fun TrackResponse.toDomain(): Track {
        return Track(
            id = this.id,
            title = this.title,
            previewUrl = this.previewUrl,
            artist = this.artist.toDomain(),
            album = this.album.toDomain()
        )
    }

    fun ArtistResponse.toDomain(): Artist {
        return Artist(
            id = this.id,
            name = this.name
        )
    }

    fun AlbumResponse.toDomain(): Album {
        return Album(
            id = this.id,
            coverUrl = this.coverUrl
        )
    }

    fun List<TrackResponse>.toDomainList(): List<Track> {
        return this.map { response ->
            response.toDomain()
        }
    }
}