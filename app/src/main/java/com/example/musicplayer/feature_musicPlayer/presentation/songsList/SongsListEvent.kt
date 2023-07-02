package com.example.musicplayer.feature_musicPlayer.presentation.songsList

import androidx.navigation.NavHostController

sealed class SongsListEvent() {
    data class StartPlay(val songUri: String): SongsListEvent()
    object StopPlay: SongsListEvent()

    data class isPermission(
        val isPermission: Boolean
    ): SongsListEvent()
}
