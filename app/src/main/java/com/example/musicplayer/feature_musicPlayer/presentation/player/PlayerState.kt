package com.example.musicplayer.feature_musicPlayer.presentation.player

import java.io.File

data class PlayerState(
    val isPlay: Boolean = false,
    val currentSong: File? = null
)