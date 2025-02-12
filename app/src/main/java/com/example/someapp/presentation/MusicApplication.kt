package com.example.someapp.presentation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.someapp.di.app.AppComponent
import com.example.someapp.di.app.DaggerAppComponent

class MusicApplication : Application() {

    val component: AppComponent = DaggerAppComponent.factory().create(this)
}

@Composable
fun getApplicationComponent(): AppComponent {
    return (LocalContext.current.applicationContext as MusicApplication).component
}
