package com.example.musicplayer.feature_musicPlayer.presentation.songsList

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.musicplayer.feature_musicPlayer.core.Permisions.isExternalStoragePermissions
import com.example.musicplayer.feature_musicPlayer.presentation.unit.songs.GetSongs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SongsListViewModel @Inject constructor() : ViewModel() {

    private val _state = mutableStateOf(SongsListState())
    val state: State<SongsListState> = _state

    init {
        _state.value = state.value.copy(
            isPermission = false,
            songsList = GetSongs()
        )
    }

    fun onEvent(event: SongsListEvent) {
        when(event) {
            is SongsListEvent.chooseSong -> {

            }
            is SongsListEvent.clickPermissions -> {
//                isExternalStoragePermissions(context)
            }
        }
    }

}