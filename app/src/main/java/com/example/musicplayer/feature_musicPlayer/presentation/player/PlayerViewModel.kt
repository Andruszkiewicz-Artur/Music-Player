package com.example.musicplayer.feature_musicPlayer.presentation.player

import android.app.Application
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import androidx.compose.runtime.State
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.feature_musicPlayer.mediaPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val allSongs: Array<File>,
    private val application: Application
): ViewModel() {
    private var isFirst = true

    private val _state = mutableStateOf(PlayerState())
    val state: State<PlayerState> = _state

    init {
        mediaPlayer = MediaPlayer()
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)

        savedStateHandle.get<String>("path")?.let { path ->
            System.out.println(path)
            if(path.isNotBlank()) {
                _state.value = state.value.copy(
                    currentSong = allSongs.single {
                        it.path == path
                    }
                )
            }
        }

        System.out.println(state.value.currentSong)
    }

    fun onEvent(event: PlayerEvent) {
        when(event) {
            is PlayerEvent.PlaySong -> {
                if(mediaPlayer.isPlaying) {
                    offMusic()
                }
                try {
                    if(isFirst) {
                        mediaPlayer.setDataSource(application, _state.value.currentSong!!.toUri())
                        mediaPlayer.prepare()
                        isFirst = false
                    }
                    mediaPlayer.start()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                _state.value = state.value.copy(
                    isPlay = mediaPlayer.isPlaying
                )
            }
            is PlayerEvent.StopSong -> {
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.pause()
                }
                _state.value = state.value.copy(
                    isPlay = false
                )
            }
            is PlayerEvent.ResetSong -> {
                offMusic()
                onEvent(PlayerEvent.PlaySong)
            }
            is PlayerEvent.PreviousSong -> {
                offMusic()
                mediaPlayer = MediaPlayer()
                val index = allSongs.indexOf(_state.value.currentSong)
                if (index == 0) {
                    _state.value = state.value.copy(
                        currentSong = allSongs.last()
                    )
                } else {
                    _state.value = state.value.copy(
                        currentSong = allSongs[index - 1]
                    )
                }
                onEvent(PlayerEvent.PlaySong)
            }
            is PlayerEvent.NextSong -> {
                offMusic()
                val index = allSongs.indexOf(_state.value.currentSong)
                if (index == allSongs.size - 1) {
                    _state.value = state.value.copy(
                        currentSong = allSongs.first()
                    )
                } else {
                    _state.value = state.value.copy(
                        currentSong = allSongs[index + 1]
                    )
                }
                onEvent(PlayerEvent.PlaySong)
            }
        }
    }

    private fun offMusic() {
        isFirst = true
        if(mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.reset()
        mediaPlayer.release()

        mediaPlayer = MediaPlayer()
        _state.value = state.value.copy(
            isPlay = false
        )
    }
}