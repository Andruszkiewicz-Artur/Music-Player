package com.example.musicplayer.feature_musicPlayer.presentation.player

import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
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

    private var songUri: String? = null

    init {
        songUri = savedStateHandle.get<String>("songUri")
        Log.d("Check song", "$songUri")
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