package com.example.musicplayer.feature_musicPlayer.presentation.player.compose

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.*
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
    val musicPlayer = viewModel.musicPlayer.value
    var isNext by remember {
        mutableStateOf<Boolean?>(null)
    }

    var currentTime by remember {
        mutableStateOf(0)
    }
    var wholeTime by remember {
        mutableStateOf(0)
    }

    LaunchedEffect(key1 = true) {
        viewModel.setUp(state)
    }

    LaunchedEffect(key1 = state.currentSong, key2 = isNext ) {
        Log.d("Check current song", "${state.currentSong}")
        if(state.currentSong != null && isNext != null) {
            viewModel.onEvent(PlayerEvent.ChangeSong(state.currentSong))
        }
    }

    if(currentSong == state.currentSong) {
        LaunchedEffect(key1 = state.currentState, key2 = state.musicPlayer) {
            while (state.currentState == MusicPlayerState.Started && state.musicPlayer != null) {
                if(state.musicPlayer.isPlaying) {
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
            Log.d("Player", "${state.musicPlayer?.duration}")
            if(state.musicPlayer != null) {
                wholeTime = state.musicPlayer.duration / 1000
            } else {
                wholeTime = 0
            }
        }
    } else {
        LaunchedEffect(key1 = musicPlayer) {
            Log.d("Player", "musicPlayer?.duration: ${musicPlayer?.duration}")
            Log.d("Player", "musicPlayer: $musicPlayer")
            if(musicPlayer != null) {
                Log.d("Player", "musicPlayer.duration / 1000: ${musicPlayer.duration / 1000}")
                wholeTime = musicPlayer.duration / 1000
            } else {
                wholeTime = 0
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.background
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.recording),
            contentDescription = null,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .size(300.dp)
                .clip(RoundedCornerShape(20.dp))
        )

        Text(
            text = currentSong?.name?.deleteExtensionFile() ?: "",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .fillMaxWidth(0.9f)
        )
        
        Spacer(modifier = Modifier.height(32.dp))

        SliderPresentation(
            state = state,
            currentTime = currentTime,
            wholeTime = wholeTime,
            onValueChangeSlider = {
                currentTime = it
            }
        )

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
                        isNext = false
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
                        isNext = true
                    }
            )
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}