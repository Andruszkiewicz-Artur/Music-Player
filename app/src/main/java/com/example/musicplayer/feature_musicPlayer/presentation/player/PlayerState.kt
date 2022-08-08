package com.example.musicplayer.feature_musicPlayer.presentation.player

data class PlayerState(
    val isPlay: Boolean = false,
    val songsList: List<String> = emptyList(),
    val currentSong: String = ""
)