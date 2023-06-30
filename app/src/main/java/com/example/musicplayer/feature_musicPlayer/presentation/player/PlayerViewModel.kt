package com.example.musicplayer.feature_musicPlayer.presentation.player

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.musicplayer.feature_musicPlayer.core.constants.Constants
import com.example.musicplayer.feature_musicPlayer.domain.service.ServiceHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val application: Application
): ViewModel() {
    fun onEvent(event: PlayerEvent) {
        when(event) {
            is PlayerEvent.PlaySong -> {
                ServiceHelper.triggerForegroundService(
                    context = application,
                    action = Constants.ACTION_SERVICE_START
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
        }
    }
}