package com.example.musicplayer.feature_musicPlayer

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.musicplayer.feature_musicPlayer.presentation.unit.navigation.NavGraph
import com.example.musicplayer.ui.theme.MusicPlayerTheme
import dagger.hilt.android.AndroidEntryPoint

var mediaPlayer: MediaPlayer = MediaPlayer()

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicPlayerTheme {
                NavGraph()
            }
        }
    }
}