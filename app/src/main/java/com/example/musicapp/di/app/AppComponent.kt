package com.example.musicapp.di.app

import android.content.Context
import com.example.musicapp.di.player.PlayerModule
import com.example.musicapp.presentation.ViewModelFactory
import com.example.musicapp.presentation.player.player.PlayerManager
import dagger.BindsInstance
import dagger.Component


@ApplicationScope
@Component(
    modules = [
        NetworkModule::class,
        RepositoryModule::class,
        ViewModelModule::class,
        PlayerModule::class
    ]
)
interface AppComponent {

    val playerManager: PlayerManager
    val viewModelFactory: ViewModelFactory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}