package com.example.someapp.di.app

import android.content.Context
import com.example.someapp.presentation.ViewModelFactory
import dagger.BindsInstance
import dagger.Component


@ApplicationScope
@Component(
    modules = [
        NetworkModule::class,
        RepositoryModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent {

    fun getViewModelFactory(): ViewModelFactory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}