package com.example.musicapp.di.app

import com.example.musicapp.data.repository.TracksRepositoryImpl
import com.example.musicapp.domain.repository.TracksRepository
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {

    @Binds
    @ApplicationScope
    fun bindTracksRepository(impl: TracksRepositoryImpl): TracksRepository
}