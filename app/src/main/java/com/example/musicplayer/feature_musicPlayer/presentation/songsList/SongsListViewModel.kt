package com.example.musicplayer.feature_musicPlayer.presentation.songsList

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.musicplayer.feature_musicPlayer.core.constants.Constants
import com.example.musicplayer.feature_musicPlayer.core.constants.Constants.ACTION_SERVICE_START
import com.example.musicplayer.feature_musicPlayer.core.constants.Constants.ACTION_SERVICE_STOP
import com.example.musicplayer.feature_musicPlayer.domain.service.ServiceHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SongsListViewModel @Inject constructor(
    private val application: Application
) : ViewModel() {

    fun onEvent(event: SongsListEvent) {
        when(event) {
            is SongsListEvent.StartPlay -> {
                ServiceHelper.triggerForegroundService(
                    context = application,
                    action = ACTION_SERVICE_START,
                    songUri = event.songUri
                )
            }
            is SongsListEvent.StopPlay -> {
                ServiceHelper.triggerForegroundService(
                    context = application,
                    action = ACTION_SERVICE_STOP
                )
            }
        }
    }
}