package com.example.musicplayer.feature_musicPlayer.presentation.player

sealed class PlayerEvent() {
    object PlaySong: PlayerEvent()
    object StopSong: PlayerEvent()
    object PreviousSong: PlayerEvent()
    object NextSong: PlayerEvent()
}