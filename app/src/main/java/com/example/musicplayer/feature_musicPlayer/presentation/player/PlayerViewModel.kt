package com.example.musicplayer.feature_musicPlayer.presentation.player

import android.app.Application
import androidx.lifecycle.ViewModel
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
                ServiceHelper.startPendingIntent(application)
            }
            is PlayerEvent.StopSong -> {
                ServiceHelper.stopPendingIntent(application)
            }
            is PlayerEvent.ResetSong -> {

            }
            is PlayerEvent.PreviousSong -> {
                ServiceHelper.previousPendingIntent(application)
            }
            is PlayerEvent.NextSong -> {
                ServiceHelper.nextPendingIntent(application)
            }
        }
    }
}