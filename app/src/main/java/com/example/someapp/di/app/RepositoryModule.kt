package com.example.someapp.di.app

import com.example.someapp.data.repository.TracksRepositoryImpl
import com.example.someapp.domain.tracks.repository.TracksRepository
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {

    @Binds
    @ApplicationScope
    fun bindTracksRepository(impl: TracksRepositoryImpl): TracksRepository
}