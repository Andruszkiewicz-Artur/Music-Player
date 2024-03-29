package com.example.musicplayer.feature_musicPlayer.presentation.songsList.compose

import android.Manifest
import android.os.Environment
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.musicplayer.R
import com.example.musicplayer.feature_musicPlayer.core.Permisions.isExternalStoragePermissions
import com.example.musicplayer.feature_musicPlayer.core.constants.Constants
import com.example.musicplayer.feature_musicPlayer.core.extensions.deleteExtensionFile
import com.example.musicplayer.feature_musicPlayer.domain.service.MusicPlayerService
import com.example.musicplayer.feature_musicPlayer.domain.service.ServiceHelper
import com.example.musicplayer.feature_musicPlayer.presentation.songsList.SongsListEvent
import com.example.musicplayer.feature_musicPlayer.presentation.songsList.SongsListViewModel
import com.example.musicplayer.feature_musicPlayer.presentation.unit.navigation.Screen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SongsList(
    navController: NavHostController,
    viewModel: SongsListViewModel = hiltViewModel(),
    service: MusicPlayerService
) {
    val state = service.state.value

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.background
            )
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = stringResource(id = R.string.MusicPlaylist),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            )
            if(state.musicList.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                ) {
                    items(state.musicList) {song ->
                        ItemSongPresentation(
                            song = song,
                            isLast = state.musicList.last() == song,
                            isPlaying = if (song == state.currentSong) state.musicPlayer?.isPlaying ?: false else false,
                            onClickPlay = {
                                viewModel.onEvent(SongsListEvent.StartPlay(song.absolutePath))
                            },
                            onClickStop = {
                                viewModel.onEvent(SongsListEvent.StopPlay)
                            },
                            onClickRecord = {
                                navController.navigate(Screen.playerScreen.sendPath(song.absolutePath.toString()))
                            }
                        )
                    }

                    item {
                        if(state.currentSong != null && state.musicPlayer != null) {
                            Spacer(modifier = Modifier.height(100.dp))
                        }
                    }
                }
            } else {
                Text(
                    text = stringResource(id = R.string.YouDontHaveSongsOnYouPhone) ,
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        BottomSongPresentation(
            state = state,
            onClickPlay = {
                viewModel.onEvent(SongsListEvent.StartPlay(state.currentSong?.absolutePath ?: ""))
            },
            onClickStop = {
                viewModel.onEvent(SongsListEvent.StopPlay)
            },
            onClickRecord = {
                navController.navigate(Screen.playerScreen.sendPath(state.currentSong?.absolutePath.toString()))
            }
        )
    }
}