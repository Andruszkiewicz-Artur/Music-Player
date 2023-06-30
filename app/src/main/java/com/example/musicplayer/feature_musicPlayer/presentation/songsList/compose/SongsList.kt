package com.example.musicplayer.feature_musicPlayer.presentation.songsList.compose

import android.os.Environment
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.musicplayer.R
import com.example.musicplayer.feature_musicPlayer.core.Permisions.isExternalStoragePermissions
import com.example.musicplayer.feature_musicPlayer.domain.service.MusicPlayerService
import com.example.musicplayer.feature_musicPlayer.presentation.songsList.SongsListEvent
import com.example.musicplayer.feature_musicPlayer.presentation.songsList.SongsListViewModel
import com.example.musicplayer.feature_musicPlayer.presentation.unit.navigation.Screen

@Composable
fun SongsList(
    navController: NavHostController,
    viewModel: SongsListViewModel = hiltViewModel(),
    service: MusicPlayerService
) {
    val context = LocalContext.current
    val isPermission = viewModel.isPermission.value
    val state = service.state.value

    LaunchedEffect(key1 = isPermission) {
        if(!isExternalStoragePermissions(context)) {
            viewModel.onEvent(SongsListEvent.isPermission(false))
        } else {
            viewModel.onEvent(SongsListEvent.isPermission(true))
        }
    }

    Column(
        verticalArrangement = if(isPermission) Arrangement.Top else Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (isPermission) {
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
                            isLast = state.musicList.last() == song
                        ) {
                            viewModel.onEvent(SongsListEvent.Play)
                            navController.navigate(Screen.playerScreen.route)
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
        } else {
            PermissionPresentaiton {
                viewModel.onEvent(SongsListEvent.isPermission(isExternalStoragePermissions(context)))
            }
        }
    }
}