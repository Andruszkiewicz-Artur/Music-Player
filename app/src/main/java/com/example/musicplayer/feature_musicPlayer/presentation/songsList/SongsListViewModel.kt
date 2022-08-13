package com.example.musicplayer.feature_musicPlayer.presentation.songsList

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.musicplayer.feature_musicPlayer.presentation.unit.navigation.Screen
import com.example.musicplayer.feature_musicPlayer.presentation.unit.songs.GetSongs
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SongsListViewModel @Inject constructor(
    val allSongs: Array<File>
) : ViewModel() {

    private val _state = mutableStateOf(SongsListState())
    val state: State<SongsListState> = _state

    fun onEvent(event: SongsListEvent) {
        when(event) {
            is SongsListEvent.chooseSong -> {
                event.navController.navigate(Screen.playerScreen.sendPath(event.path))
            }
            is SongsListEvent.isPermission -> {
                _state.value = state.value.copy(
                    isPermission = event.isPermission,
                    songsList = allSongs
                )
            }
        }
    }
}