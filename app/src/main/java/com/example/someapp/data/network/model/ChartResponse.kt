package com.example.someapp.data.network.model

import com.google.gson.annotations.SerializedName

data class ChartResponse(
    @SerializedName("tracks") val tracks: TrackListResponse
)

data class TrackListResponse(
    @SerializedName("data") val data: List<TrackResponse>
)

data class TrackResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("preview") val previewUrl: String,
    @SerializedName("artist") val artist: ArtistResponse,
    @SerializedName("album") val album: AlbumResponse
)

data class ArtistResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String
)

data class AlbumResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("cover") val coverUrl: String
)
