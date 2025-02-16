package com.example.musicapp.presentation.navigation


sealed class Screen(val route: String) {

    data object Downloaded : Screen(ROUTE_DOWNLOADED)
    data object Tracks : Screen(ROUTE_TRACKS)
    data object Player : Screen(ROUTE_PLAYER) {
         const val ROUTE_FOR_ARGS = "player"

        fun createRoute(trackId: Long): String {
            return "$ROUTE_FOR_ARGS/$trackId"
        }
    }

    companion object {
        const val KEY_TRACK_ID = "trackId"

        const val ROUTE_DOWNLOADED = "downloaded"
        const val ROUTE_TRACKS = "tracks"
        const val ROUTE_PLAYER = "player/{$KEY_TRACK_ID}"
    }
}

