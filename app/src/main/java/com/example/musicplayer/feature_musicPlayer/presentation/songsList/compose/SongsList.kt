package com.example.musicplayer.feature_musicPlayer.presentation.songsList.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.musicplayer.feature_musicPlayer.core.Permisions.isExternalStoragePermissions
import com.example.musicplayer.feature_musicPlayer.presentation.player.PlayerViewModel
import com.example.musicplayer.feature_musicPlayer.presentation.songsList.SongsListEvent
import com.example.musicplayer.feature_musicPlayer.presentation.songsList.SongsListViewModel

@Composable
fun SongsList(
    navController: NavHostController,
    viewModel: SongsListViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val state = viewModel.state.value

    Column(
        verticalArrangement = if(state.isPermission) Arrangement.Top else Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (state.isPermission) {
            Text(
                text = "Music Playlist",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            )
            if(state.songsList.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                ) {
                    items(state.songsList) {
                        Text(
                            text = it.name
                        )
                        if (it != state.songsList.last()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                            )
                        }
                    }
                }
            } else {
                Text(
                    text = "You don`t have songs on you phone!" ,
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            Text(
                text = "You need allow permissions!",
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                textAlign = TextAlign.Center
            )
            Button(
                onClick = {
                    isExternalStoragePermissions(context)
                },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "Allow permissions",
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}