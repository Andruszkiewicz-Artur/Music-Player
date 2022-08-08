package com.example.musicplayer.feature_musicPlayer.core.Permisions

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import javax.inject.Inject

private fun hasReadExternalStorage(context: Context) =
    ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

fun isExternalStoragePermissions(context: Context): Boolean {

    var permissionsToRequest = mutableListOf<String>()

    if(!hasReadExternalStorage(context)) {
        permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    if(permissionsToRequest.isNotEmpty()) {
        ActivityCompat.requestPermissions(context as Activity, permissionsToRequest.toTypedArray(), 0)
    }

    return permissionsToRequest.isEmpty()
}