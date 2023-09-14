package com.example.musicplayer.feature_musicPlayer.presentation.unit.navigation

import com.example.musicplayer.feature_musicPlayer.core.constants.Constants

sealed class Screen(val route: String) {
    object songsListScreen: Screen(route = "SongsListScreen")

    object playerScreen: Screen(
        route = "PlayerScreen"
    ) fun sendPath(
        songUri: String
    ): String {
        return this.route + "?${Constants.SONG_URI}=$songUri"
    }

}