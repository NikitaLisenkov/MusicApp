package com.example.someapp.di

import com.example.someapp.data.network.DeezerApi
import com.example.someapp.data.repository.TracksRepositoryImpl
import com.example.someapp.domain.repository.TracksRepository
import com.example.someapp.domain.usecase.GetChartTracksUseCase
import com.example.someapp.domain.usecase.SearchTracksUseCase
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceLocator {

    private const val BASE_URL = "https://api.deezer.com/"
    private const val TIMEOUT: Long = 15

    private val authInterceptor: Interceptor = Interceptor { chain ->
        chain.proceed(
            chain.request()
                .newBuilder()
                .build()
        )
    }

    private val httpLoggingInterceptor: HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor(authInterceptor)
        .addNetworkInterceptor(httpLoggingInterceptor)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api: DeezerApi = retrofit.create(DeezerApi::class.java)

    private val repo: TracksRepository = TracksRepositoryImpl(api)

    val getChartTracksUseCase: GetChartTracksUseCase = GetChartTracksUseCase(repo)

    val searchTracksUseCase: SearchTracksUseCase = SearchTracksUseCase(repo)

}