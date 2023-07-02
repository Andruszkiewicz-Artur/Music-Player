package com.example.musicplayer.feature_musicPlayer.presentation.player.compose

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.musicplayer.feature_musicPlayer.domain.model.MusicPlayerState
import com.example.musicplayer.feature_musicPlayer.domain.service.MusicPlayerService
import com.example.musicplayer.feature_musicPlayer.presentation.player.PlayerEvent
import com.example.musicplayer.feature_musicPlayer.presentation.player.PlayerViewModel
import java.util.concurrent.TimeUnit
import com.example.musicplayer.R
import com.example.musicplayer.feature_musicPlayer.core.extensions.deleteExtensionFile
import kotlinx.coroutines.delay

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Player(
    viewModel: PlayerViewModel = hiltViewModel(),
    service: MusicPlayerService
) {
    val state = service.state.value
    val currentSong = viewModel.file.value
    var isNext: Boolean? = null

    var currentTime by remember {
        mutableStateOf(0)
    }
    var wholeTime by remember {
        mutableStateOf(0)
    }

    LaunchedEffect(key1 = true) {
        viewModel.setUp(state)
    }

    LaunchedEffect(key1 = state.currentSong ) {
        Log.d("Check current song", "${state.currentSong}")
        if(state.currentSong != null && isNext != null) {
            viewModel.onEvent(PlayerEvent.ChangeSong(state.currentSong))
        }
    }

    if(currentSong == state.currentSong) {
        LaunchedEffect(key1 = state.currentState, key2 = state.musicPlayer) {
            while (true) {
                if(state.musicPlayer != null && state.musicPlayer.isPlaying) {
                    currentTime = state.musicPlayer.currentPosition / 1000
                    delay(500)
                } else {
                    if(state.musicPlayer == null) {
                        currentTime = 0
                    }
                    break
                }
            }
        }

        LaunchedEffect(key1 = state.musicPlayer) {
            if(state.musicPlayer != null) {
                wholeTime = state.musicPlayer.duration / 1000
            } else {
                wholeTime = 0
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = currentSong?.name?.deleteExtensionFile() ?: "",
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall
        )

        Image(
            painter = painterResource(id = R.drawable.baseline_music_note_24),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
        )

        if(currentSong == state.currentSong) {
            SliderPresentation(
                state = state,
                currentTime = currentTime,
                wholeTime = wholeTime,
                onValueChangeSlider = {
                    currentTime = it
                }
            )
        }

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
                    .size(30.dp)
                    .weight(1f)
                    .clickable {
                        viewModel.onEvent(PlayerEvent.PreviousSong)
                        if (currentSong != state.currentSong) {
                            isNext = false
                        }
                    }
            )

            Icon(
                imageVector = Icons.TwoTone.Replay5,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(30.dp)
                    .weight(1f)
                    .clickable {
                        if (state.musicPlayer != null) {
                            if (currentTime - 5 <= wholeTime) {
                                state.musicPlayer.seekTo(0)
                            } else {
                                state.musicPlayer.seekTo((currentTime - 5) * 1000)
                            }
                        }
                    }
            )

            AnimatedContent(
                targetState = state.musicPlayer?.isPlaying
            ) {
                if (it == true && state.currentSong == currentSong) {
                    Icon(
                        imageVector = Icons.TwoTone.PauseCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier
                            .size(50.dp)
                            .weight(2f)
                            .clickable {
                                viewModel.onEvent(PlayerEvent.StopSong)
                            }
                    )
                } else {
                    Icon(
                        imageVector = Icons.TwoTone.PlayCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier
                            .size(50.dp)
                            .weight(2f)
                            .clickable {
                                viewModel.onEvent(PlayerEvent.PlaySong)
                            }
                    )
                }
            }

            Icon(
                imageVector = Icons.TwoTone.Forward5,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(30.dp)
                    .weight(1f)
                    .clickable {
                        if (state.musicPlayer != null) {
                            if (currentTime + 5 >= wholeTime) {
                                state.musicPlayer.seekTo(wholeTime * 1000)
                            } else {
                                state.musicPlayer.seekTo((currentTime + 5) * 1000)
                            }
                        }
                    }
            )

            Icon(
                imageVector = Icons.TwoTone.FastForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(30.dp)
                    .weight(1f)
                    .clickable {
                        viewModel.onEvent(PlayerEvent.NextSong)
                        if (currentSong != state.currentSong) {
                            isNext = true
                        }
                    }
            )
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}