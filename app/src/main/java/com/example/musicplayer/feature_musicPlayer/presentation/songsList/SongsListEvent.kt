package com.example.musicplayer.feature_musicPlayer.presentation.songsList

import androidx.navigation.NavHostController

sealed class SongsListEvent() {
    data class chooseSong(
        val path: String,
        val navController: NavHostController
    ): SongsListEvent()

    data class isPermission(
        val isPermission: Boolean
    ): SongsListEvent()
}
