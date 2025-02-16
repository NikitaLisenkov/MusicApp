package com.example.musicapp.presentation.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.musicapp.presentation.theme.MusicAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionIfNeeded(Manifest.permission.POST_NOTIFICATIONS, 0)
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            requestPermissionIfNeeded(Manifest.permission.WRITE_EXTERNAL_STORAGE, 1)
        }

        setContent {
            MusicAppTheme {
                MusicMainContainer()
            }
        }
    }

    private fun requestPermissionIfNeeded(permission: String, requestCode: Int) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(permission), requestCode)
        }
    }
}

