package com.example.musicplayer.feature_musicPlayer.presentation.player

import android.media.AudioManager
import android.media.MediaPlayer
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(): ViewModel() {

    private var mediaPlayer: MediaPlayer

    private val _state = mutableStateOf(PlayerState())
    val state: State<PlayerState> = _state

    init {
        mediaPlayer = MediaPlayer()
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
    }

}