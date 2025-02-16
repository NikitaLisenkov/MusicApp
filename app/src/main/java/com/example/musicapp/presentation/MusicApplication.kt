package com.example.musicapp.presentation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.musicapp.di.app.AppComponent
import com.example.musicapp.di.app.DaggerAppComponent

class MusicApplication : Application() {

    val component: AppComponent = DaggerAppComponent.factory().create(this)
}

@Composable
fun getApplicationComponent(): AppComponent {
    return (LocalContext.current.applicationContext as MusicApplication).component
}
