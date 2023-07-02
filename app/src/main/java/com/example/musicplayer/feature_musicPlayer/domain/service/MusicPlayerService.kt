package com.example.musicplayer.feature_musicPlayer.domain.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.Environment
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.example.musicplayer.R
import com.example.musicplayer.feature_musicPlayer.core.constants.Constants.ACTION_SERVICE_NEXT
import com.example.musicplayer.feature_musicPlayer.core.constants.Constants.ACTION_SERVICE_PREVIOUS
import com.example.musicplayer.feature_musicPlayer.core.constants.Constants.ACTION_SERVICE_START
import com.example.musicplayer.feature_musicPlayer.core.constants.Constants.ACTION_SERVICE_STOP
import com.example.musicplayer.feature_musicPlayer.core.constants.Constants.MUSIC_PLAYER_STATE
import com.example.musicplayer.feature_musicPlayer.core.constants.Constants.NOTIFICATION_CHANNEL_ID
import com.example.musicplayer.feature_musicPlayer.core.constants.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.musicplayer.feature_musicPlayer.core.constants.Constants.NOTIFICATION_ID
import com.example.musicplayer.feature_musicPlayer.core.constants.Constants.SONG_URI
import com.example.musicplayer.feature_musicPlayer.domain.model.MusicPlayerState
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.DurationUnit

@ExperimentalAnimationApi
@AndroidEntryPoint
class MusicPlayerService: Service() {
    @Inject
    lateinit var notificationManager: NotificationManager
    @Inject
    lateinit var notificationBuilder: NotificationCompat.Builder
    @Inject
    lateinit var context: Context

    private val binder = MusicPlayerBinder()

    private val _state = mutableStateOf(com.example.musicplayer.feature_musicPlayer.domain.service.MusicPlayerState())
    val state = _state

    override fun onBind(p0: Intent?) = binder

    override fun onCreate() {
        super.onCreate()
        _state.value = state.value.copy(
            musicList = GetSongs().toMutableList()
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("Check", "${intent?.extras}")
        val songPath = intent?.getStringExtra(SONG_URI)
        if(_state.value.musicList.isNotEmpty()) {
            when (intent?.getStringExtra(MUSIC_PLAYER_STATE)) {
                MusicPlayerState.Started.name -> {
                    startPlayMusic()
                    startForegroundService()
                    setStopButton()
                }
                MusicPlayerState.Stopped.name -> {
                    stopPlayMusic()
                    setPlayButton()
                }
                MusicPlayerState.Next.name -> {
                    playNextMusic()
                    setStopButton()
                }
                MusicPlayerState.Previous.name -> {
                    playPreviousMusic()
                    setStopButton()
                }
            }
            intent?.action.let {
                when (it) {
                    ACTION_SERVICE_START -> {
                        startPlayMusic(songPath)
                        startForegroundService()
                        setStopButton()
                    }
                    ACTION_SERVICE_STOP -> {
                        stopPlayMusic()
                        setPlayButton()
                    }
                    ACTION_SERVICE_NEXT -> {
                        playNextMusic()
                        setStopButton()
                    }
                    ACTION_SERVICE_PREVIOUS -> {
                        playPreviousMusic()
                        setStopButton()
                    }
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startPlayMusic(songUri: String? = null) {
        if (_state.value.currentSong?.absolutePath != songUri && songUri != null) {
            var currentSong: File? = null

            _state.value.musicList.forEach {
                if (it.absolutePath == songUri) {
                    currentSong = it
                }
            }
            playAudio(context, currentSong ?: _state.value.musicList.first())
        } else {
            if (_state.value.musicPlayer != null) {
                _state.value.musicPlayer!!.start()
            }
        }
        if (_state.value.musicPlayer != null) {
            _state.value = state.value.copy(
                currentState = MusicPlayerState.Started
            )
        }
    }
    private fun stopPlayMusic() {
        if (_state.value.musicPlayer != null) {
            if(_state.value.musicPlayer!!.isPlaying) {
                _state.value.musicPlayer!!.pause()
                _state.value = state.value.copy(
                    currentState = MusicPlayerState.Stopped
                )
            }
        }
    }

    private fun playNextMusic() {
        if(_state.value.currentSong != null) {
            val song = findSong(
                _state.value.currentSong!!,
                isNext = true
            )
            playAudio(context, song)
            _state.value = state.value.copy(
                currentState = MusicPlayerState.Started
            )
        }
    }

    private fun playPreviousMusic() {
        if(_state.value.currentSong != null) {
            val song = findSong(
                _state.value.currentSong!!,
                isNext = false
            )
            playAudio(context, song)
            _state.value = state.value.copy(
                currentState = MusicPlayerState.Started
            )
        }
    }

    private fun startForegroundService() {
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

    private fun updateNotification() {
        notificationManager.notify(
            NOTIFICATION_ID,
            notificationBuilder.setContentText(
                _state.value.currentSong?.name?.replace(".mp3", "")?.replace(".wav", "") ?: ""
            ).build()
        )
    }

    @SuppressLint("RestrictedApi")
    private fun setPlayButton() {
        notificationBuilder.mActions.removeAt(1)
        notificationBuilder.mActions.add(
            1,
            NotificationCompat.Action(
                R.drawable.baseline_play_arrow_24,
                "Play",
                ServiceHelper.startPendingIntent(this)
            )
        )
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    @SuppressLint("RestrictedApi")
    private fun setStopButton() {
        notificationBuilder.mActions.removeAt(1)
        notificationBuilder.mActions.add(
            1,
            NotificationCompat.Action(
                R.drawable.baseline_pause_24,
                "Stop",
                ServiceHelper.stopPendingIntent(this)
            )
        )
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun playAudio(context: Context, song: File) {
        if (_state.value.musicPlayer != null && song != _state.value.currentSong) {
            _state.value.musicPlayer?.stop()
            _state.value.musicPlayer?.release()

            _state.value = state.value.copy(
                musicPlayer = null
            )

            _state.value = state.value.copy(
                currentSong = song
            )
        }

        if(_state.value.musicPlayer == null) {
            _state.value = state.value.copy(
                musicPlayer = MediaPlayer()
            )
            _state.value.musicPlayer?.setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
        }

        val musicPlayer = _state.value.musicPlayer

        if (musicPlayer != null) {
            try {
                musicPlayer.setDataSource(context, Uri.parse(song.absolutePath))
                musicPlayer.prepare()
                musicPlayer.start()
                updateNotification()
            } catch (e: Exception) {
                Log.d("Check Error", e.message ?: "Unknown Error")
            }
        }
    }

    private fun findSong(currentSong: File, isNext: Boolean): File {
        var song = currentSong

        _state.value.musicList.forEachIndexed { index, file ->
            if (file == currentSong) {
                Log.d("Check isNext", "${isNext}")
                song = if(isNext) {
                    Log.d("Check musicList size", "${_state.value.musicList.size}")
                    Log.d("Check question", "${index >= _state.value.musicList.size + 1}")
                    if(index != _state.value.musicList.size + 1) {
                        _state.value.musicList[index + 1]
                    } else {
                        currentSong
                    }
                } else {
                    if(index == 0) {
                        currentSong
                    } else {
                        _state.value.musicList[index - 1]
                    }
                }
            }
        }

        return song
    }

    private fun GetSongs(file: File = Environment.getExternalStorageDirectory()): Array<File> {
        val songs = mutableListOf<File>()
        val files = file.listFiles()

        if(files != null) {
            for ( singleFile in files) {
                if(singleFile.isDirectory && !singleFile.isHidden) {
                    songs.addAll(GetSongs(singleFile))
                } else {
                    if(singleFile.name.endsWith(".mp3") || singleFile.name.endsWith(".wav")) {
                        songs.add(singleFile)
                    }
                }
            }
        }

        return songs.toTypedArray()
    }
    inner class MusicPlayerBinder : Binder() {
        fun getService(): MusicPlayerService = this@MusicPlayerService
    }
}