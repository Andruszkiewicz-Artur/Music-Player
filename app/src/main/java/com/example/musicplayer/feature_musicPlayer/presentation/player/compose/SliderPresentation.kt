package com.example.musicplayer.feature_musicPlayer.presentation.player.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.musicplayer.feature_musicPlayer.domain.service.MusicPlayerState

@Composable
fun SliderPresentation(
    state: MusicPlayerState,
    currentTime: Int,
    wholeTime: Int,
    onValueChangeSlider: (Int) -> Unit,

) {
    Slider(
        value = currentTime.toFloat(),
        valueRange = 0f..wholeTime.toFloat(),
        enabled = true,
        onValueChange = {
            onValueChangeSlider(it.toInt())
        },
        onValueChangeFinished = {
            if (state.musicPlayer != null) {
                state.musicPlayer.seekTo(currentTime * 1000)
            }
        },
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .padding(top = 16.dp)
    )

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .padding(bottom = 16.dp)
    ) {
        TimePresent(time = currentTime)
        TimePresent(time = wholeTime)
    }
}