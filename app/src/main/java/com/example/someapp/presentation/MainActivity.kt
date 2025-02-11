package com.example.someapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.someapp.presentation.theme.SomeAppTheme
import com.example.someapp.presentation.tracks.TracksScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SomeAppTheme {
                TracksScreen()
            }
        }
    }
}