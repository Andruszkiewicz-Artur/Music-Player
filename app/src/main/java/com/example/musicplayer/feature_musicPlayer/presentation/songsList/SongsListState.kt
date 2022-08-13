package com.example.musicplayer.feature_musicPlayer.presentation.songsList

import java.io.File

data class SongsListState(
    val isPermission: Boolean = true,
    val songsList: Array<File> = emptyArray()
)