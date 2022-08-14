package com.example.musicplayer.feature_musicPlayer.presentation.player.compose

import android.widget.Space
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.musicplayer.feature_musicPlayer.presentation.player.PlayerEvent
import com.example.musicplayer.feature_musicPlayer.presentation.player.PlayerViewModel

@Composable
fun Player(
    navController: NavHostController,
    viewModel: PlayerViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = state.currentSong?.name?.replace(".mp3", "")?.replace(".wav", "") ?: "",
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(40.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth(0.8f)
        ) {
            Icon(
                imageVector = Icons.TwoTone.FastRewind,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(60.dp)
                    .clickable {
                        viewModel.onEvent(PlayerEvent.PreviousSong)
                    }
            )
            Icon(
                imageVector = if(state.isPlay) Icons.TwoTone.PauseCircle else Icons.TwoTone.PlayCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier
                    .size(100.dp)
                    .clickable {
                        if(state.isPlay) {
                            viewModel.onEvent(PlayerEvent.StopSong)
                        } else {
                            viewModel.onEvent(PlayerEvent.PlaySong)
                        }
                    }
            )
            Icon(
                imageVector = Icons.TwoTone.Repeat,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(60.dp)
                    .clickable {
                        viewModel.onEvent(PlayerEvent.ResetSong)
                    }
            )
            Icon(
                imageVector = Icons.TwoTone.FastForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(60.dp)
                    .clickable {
                        viewModel.onEvent(PlayerEvent.NextSong)
                    }
            )
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}