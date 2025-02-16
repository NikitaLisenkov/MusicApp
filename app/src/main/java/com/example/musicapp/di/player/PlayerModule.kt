package com.example.musicapp.di.player

import android.content.Context
import com.example.musicapp.di.app.ApplicationScope
import com.example.musicapp.presentation.player.player.PlayerManager
import dagger.Module
import dagger.Provides

@Module
class PlayerModule {

    @Provides
    @ApplicationScope
    fun providePlayerManager(context: Context): PlayerManager {
        return PlayerManager(context)
    }
}
