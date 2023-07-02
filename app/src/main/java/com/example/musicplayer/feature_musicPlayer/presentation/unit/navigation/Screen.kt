package com.example.musicplayer.feature_musicPlayer.presentation.unit.navigation

sealed class Screen(
    val route: String
) {
    object songsListScreen: Screen(
        route = "SongsListScreen"
    )

    object playerScreen: Screen(
        route = "PlayerScreen"
    ) fun sendPath(
        songUri: String
    ): String {
        return this.route + "?songUri=$songUri"
    }
}