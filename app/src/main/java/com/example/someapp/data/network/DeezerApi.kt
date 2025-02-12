package com.example.someapp.data.network

import com.example.someapp.data.network.model.ChartResponse
import com.example.someapp.data.network.model.TrackListResponse
import com.example.someapp.data.network.model.TrackResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DeezerApi {

    @GET("chart")
    suspend fun getChartTracks(): ChartResponse

    @GET("search")
    suspend fun searchTracks(
        @Query("q") query: String
    ): TrackListResponse

    @GET("track/{id}")
    suspend fun getTrackById(
        @Path("id") trackId: Long
    ): TrackResponse
}