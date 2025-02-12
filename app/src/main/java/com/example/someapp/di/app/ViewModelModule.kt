package com.example.someapp.di.app

import androidx.lifecycle.ViewModel
import com.example.someapp.presentation.player.PlayerViewModel
import com.example.someapp.presentation.tracks.TracksViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(TracksViewModel::class)
    fun bindTracksViewModel(viewModel: TracksViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PlayerViewModel::class)
    fun bindPlayerViewModel(viewModel: PlayerViewModel): ViewModel
}