package com.example.musicplayer.feature_musicPlayer

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import com.example.musicplayer.R
import com.example.musicplayer.feature_musicPlayer.domain.service.MusicPlayerService
import com.example.musicplayer.feature_musicPlayer.core.comp.PermissionPresentation
import com.example.musicplayer.feature_musicPlayer.presentation.unit.navigation.NavGraph
import com.example.musicplayer.ui.theme.MusicPlayerTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalAnimationApi::class, ExperimentalPermissionsApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "Check MainActivity"
    }

    private var isBound by mutableStateOf(false)
    private lateinit var musicPlayerService: MusicPlayerService
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as MusicPlayerService.MusicPlayerBinder
            musicPlayerService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            isBound = false
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, MusicPlayerService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var alarmingText by remember { mutableStateOf("") }
            var listOfPermissions = listOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                listOfPermissions.plus(Manifest.permission.POST_NOTIFICATIONS)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                listOfPermissions.plus(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
            }

            val permissions = rememberMultiplePermissionsState(permissions = listOfPermissions)

            LaunchedEffect(key1 = Unit) {
                if (!permissions.allPermissionsGranted) {
                    permissions.permissions.forEach { permission ->
                        alarmingText = requestPermission(permission.permission)
                    }
                    permissions.launchMultiplePermissionRequest()
                }
            }

            MusicPlayerTheme {
                if(isBound && permissions.allPermissionsGranted) {
                    NavGraph(
                        service = musicPlayerService
                    )
                } else {
                    PermissionPresentation(
                        alarmingText = alarmingText,
                        onClick = {
                            permissions.launchMultiplePermissionRequest()
                            permissions.permissions.forEach { permission ->
                                alarmingText = requestPermission(permission.permission)
                            }
                        }
                    )
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        isBound = false
    }

    private fun requestPermission(permission: String): String {
        return if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            this.getString(R.string.YouNeedToWorkApp)
        } else {
            this.getString(R.string.DescriptionToAllow)
        }
    }
}