package com.example.someapp.presentation.navigation


sealed class Screen(val route: String) {

    data object Downloaded : Screen(ROUTE_DOWNLOADED)
    data object Tracks : Screen(ROUTE_TRACKS)
    data object Player : Screen(ROUTE_PLAYER) {
        private const val ROUTE_FOR_ARGS = "player"

        fun createRoute(trackId: String): String {
            return "$ROUTE_FOR_ARGS/$trackId"
        }
    }

    companion object {
        private const val KEY_TRACK_ID = "trackId"

        const val ROUTE_DOWNLOADED = "downloaded"
        const val ROUTE_TRACKS = "tracks"
        const val ROUTE_PLAYER = "player/{$KEY_TRACK_ID}"
    }
}

