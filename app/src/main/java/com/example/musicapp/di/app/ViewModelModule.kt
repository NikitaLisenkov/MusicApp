package com.example.musicapp.di.app

import androidx.lifecycle.ViewModel
import com.example.musicapp.presentation.downloaded_tracks.DownloadedTracksViewModel
import com.example.musicapp.presentation.player.player.PlayerViewModel
import com.example.musicapp.presentation.tracks.TracksViewModel
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
    @ViewModelKey(DownloadedTracksViewModel::class)
    fun bindDownloadedViewModel(viewModel: DownloadedTracksViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PlayerViewModel::class)
    fun bindPlayerViewModel(viewModel: PlayerViewModel): ViewModel
}