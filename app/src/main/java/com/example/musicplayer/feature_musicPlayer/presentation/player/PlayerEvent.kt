package com.example.musicplayer.feature_musicPlayer.presentation.player

import java.io.File

sealed class PlayerEvent() {
    object PlaySong: PlayerEvent()
    object StopSong: PlayerEvent()
    object PreviousSong: PlayerEvent()
    object NextSong: PlayerEvent()

    data class ChangeSong(val file: File): PlayerEvent()
}