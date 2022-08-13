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

    private var mediaPlayer: MediaPlayer

    private val _state = mutableStateOf(PlayerState())
    val state: State<PlayerState> = _state

    init {
        mediaPlayer = MediaPlayer()
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)

        savedStateHandle.get<String>("path")?.let { path ->
            if(path.isNotBlank()) {
                _state.value = state.value.copy(
                    currentSong = allSongs.single {
                        it.path == path
                    },
                    songsList = allSongs
                )
            }
        }
    }

    fun onEvent(event: PlayerEvent) {
        when(event) {
            is PlayerEvent.PlaySong -> {
                try {
                    mediaPlayer.setDataSource(application, _state.value.currentSong!!.toUri())
                    mediaPlayer.prepare()
                    mediaPlayer.start()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            is PlayerEvent.StopSong -> {
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.stop()
                    mediaPlayer.release()
                }
            }
            is PlayerEvent.ResetSong -> {
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.stop()
                    mediaPlayer.reset()
                    mediaPlayer.release()
                }
            }
            is PlayerEvent.PreviousSong -> {
                onEvent(PlayerEvent.ResetSong)
                mediaPlayer = MediaPlayer()
                allSongs.forEachIndexed { index, song ->
                    if(song == _state.value.currentSong) {
                        if(allSongs.first() == song) {
                            _state.value = state.value.copy(
                                currentSong = allSongs.last()
                            )
                        } else {
                            _state.value = state.value.copy(
                                currentSong = allSongs[index - 1]
                            )
                        }
                    }
                }
            }
            is PlayerEvent.NextSong -> {
                onEvent(PlayerEvent.ResetSong)
                mediaPlayer = MediaPlayer()
                allSongs.forEachIndexed { index, song ->
                    if(song == _state.value.currentSong) {
                        if(allSongs.last() == song) {
                            _state.value = state.value.copy(
                                currentSong = allSongs.first()
                            )
                        } else {
                            _state.value = state.value.copy(
                                currentSong = allSongs[index + 1]
                            )
                        }
                    }
                }
            }
        }
    }
}