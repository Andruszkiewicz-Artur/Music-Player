package com.example.musicplayer.feature_musicPlayer.presentation.songsList

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.musicplayer.feature_musicPlayer.presentation.unit.Permisions.isExternalStoragePermissions
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SongsListViewModel @Inject constructor(
    private val context: Context
): ViewModel() {

    var isPermission: Boolean = false

    init {
        isPermission = isExternalStoragePermissions(context)
        System.out.println("Work")
    }

}