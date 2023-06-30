package com.example.musicplayer.feature_musicPlayer.domain.service

import android.media.MediaPlayer
import com.example.musicplayer.feature_musicPlayer.domain.model.MusicPlayerState
import java.io.File

data class MusicPlayerState(
    val musicList: MutableList<File> = mutableListOf(),
    val currentSong: File? = null,
    val currentState: MusicPlayerState = MusicPlayerState.Idle,
    val musicPlayer: MediaPlayer = MediaPlayer()
)
