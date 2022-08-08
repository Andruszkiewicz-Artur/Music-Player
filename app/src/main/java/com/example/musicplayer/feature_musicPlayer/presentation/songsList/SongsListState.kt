package com.example.musicplayer.feature_musicPlayer.presentation.songsList

import java.io.File

data class SongsListState(
    val isPermission: Boolean = false,
    val songsList: Array<File> = emptyArray()
)