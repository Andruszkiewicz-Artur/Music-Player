package com.example.musicplayer.feature_musicPlayer.presentation.player.compose

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun TimePresent(
    time: Int
) {
    Text(
        text = "${time/60}:${(time%60).toString().padStart(2, '0')}"
    )
}