package com.example.musicplayer.feature_musicPlayer.presentation.songsList

import android.content.Context

sealed class SongsListEvent() {
    class chooseSong(val songUri: String): SongsListEvent()
    object clickPermissions: SongsListEvent()
}
