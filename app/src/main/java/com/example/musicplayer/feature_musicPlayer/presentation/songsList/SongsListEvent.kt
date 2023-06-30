package com.example.musicplayer.feature_musicPlayer.presentation.songsList

import androidx.navigation.NavHostController

sealed class SongsListEvent() {
    object Play: SongsListEvent()

    data class isPermission(
        val isPermission: Boolean
    ): SongsListEvent()
}
