package com.example.someapp.di.player

import android.content.Context
import com.example.someapp.di.app.ApplicationScope
import com.example.someapp.presentation.player.PlayerManager
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