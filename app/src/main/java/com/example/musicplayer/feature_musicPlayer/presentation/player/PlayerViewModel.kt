package com.example.musicplayer.feature_musicPlayer.presentation.player

import android.app.Application
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.feature_musicPlayer.core.constants.Constants
import com.example.musicplayer.feature_musicPlayer.domain.service.MusicPlayerState
import com.example.musicplayer.feature_musicPlayer.domain.service.ServiceHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val application: Application,
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    var file: MutableState<File?> = mutableStateOf(null)
        private set

    var musicPlayer: MutableState<MediaPlayer?> = mutableStateOf(null)
        private set

    private var songUri: String? = null

    companion object {
        private const val TAG = "PlayerViewModel"
    }

    init {
        musicPlayer.value = MediaPlayer()
        musicPlayer.value?.setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
        )

        savedStateHandle.get<String>(Constants.SONG_URI)?.let { uri ->
            if (!uri.isNullOrEmpty()) {
                songUri = uri
                val uriPath = uri.toUri()
                musicPlayer.value?.setDataSource(application, uriPath)
                musicPlayer.value?.prepare()
                Log.d(TAG, "uriPath: $uriPath")
                Log.d(TAG, "musicPlayer.value: ${musicPlayer.value?.duration}")
            }
        }
    }
    fun setUp(state: MusicPlayerState) {
        if (songUri != null) {
            state.musicList.forEach {
                if (it.absolutePath == songUri) {
                    file.value = it
                }
            }
        }
    }
    fun onEvent(event: PlayerEvent) {
        when(event) {
            is PlayerEvent.PlaySong -> {
                ServiceHelper.triggerForegroundService(
                    context = application,
                    action = Constants.ACTION_SERVICE_START,
                    songUri = file.value?.path
                )
            }
            is PlayerEvent.StopSong -> {
                ServiceHelper.triggerForegroundService(
                    context = application,
                    action = Constants.ACTION_SERVICE_STOP
                )
            }
            is PlayerEvent.PreviousSong -> {
                ServiceHelper.triggerForegroundService(
                    context = application,
                    action = Constants.ACTION_SERVICE_PREVIOUS
                )
            }
            is PlayerEvent.NextSong -> {
                ServiceHelper.triggerForegroundService(
                    context = application,
                    action = Constants.ACTION_SERVICE_NEXT
                )
            }

            is PlayerEvent.ChangeSong -> {
                file.value = event.file
            }
        }
    }
}