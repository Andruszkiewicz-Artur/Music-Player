package com.example.musicplayer.feature_musicPlayer.domain.service

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.musicplayer.feature_musicPlayer.MainActivity
import com.example.musicplayer.feature_musicPlayer.core.constants.Constants.BACK_REQUEST_CODE
import com.example.musicplayer.feature_musicPlayer.core.constants.Constants.CLICK_REQUEST_CODE
import com.example.musicplayer.feature_musicPlayer.core.constants.Constants.MUSIC_PLAYER_STATE
import com.example.musicplayer.feature_musicPlayer.core.constants.Constants.NEXT_REQUEST_CODE
import com.example.musicplayer.feature_musicPlayer.core.constants.Constants.START_REQUEST_CODE
import com.example.musicplayer.feature_musicPlayer.core.constants.Constants.STOP_REQUEST_CODE
import com.example.musicplayer.feature_musicPlayer.domain.MusicPlayerState

object ServiceHelper {
    private const val flag = PendingIntent.FLAG_IMMUTABLE

    fun clickPendingIntent(context: Context): PendingIntent {
        val clickIntent = Intent(context, MainActivity::class.java).apply {
            putExtra(MUSIC_PLAYER_STATE, MusicPlayerState.Started.name)
        }
        return PendingIntent.getActivity(
            context, CLICK_REQUEST_CODE, clickIntent, flag
        )
    }

    fun startPendingIntent(context: Context): PendingIntent {
        val stopIntent = Intent(context, MusicPlayerService::class.java).apply {
            putExtra(MUSIC_PLAYER_STATE, MusicPlayerState.Started.name)
        }
        return PendingIntent.getService(
            context, START_REQUEST_CODE, stopIntent, flag
        )
    }

    fun stopPendingIntent(context: Context): PendingIntent {
        val stopIntent = Intent(context, MusicPlayerService::class.java).apply {
            putExtra(MUSIC_PLAYER_STATE, MusicPlayerState.Stopped.name)
        }
        return PendingIntent.getService(
            context, STOP_REQUEST_CODE, stopIntent, flag
        )
    }

    fun nextPendingIntent(context: Context): PendingIntent {
        val resumeIntent = Intent(context, MusicPlayerService::class.java).apply {
            putExtra(MUSIC_PLAYER_STATE, MusicPlayerState.Next.name)
        }
        return PendingIntent.getService(
            context, NEXT_REQUEST_CODE, resumeIntent, flag
        )
    }

    fun backPendingIntent(context: Context): PendingIntent {
        val cancelIntent = Intent(context, MusicPlayerService::class.java).apply {
            putExtra(MUSIC_PLAYER_STATE, MusicPlayerState.Back.name)
        }
        return PendingIntent.getService(
            context, BACK_REQUEST_CODE, cancelIntent, flag
        )
    }

    fun triggerForegroundService(context: Context, action: String) {
        Intent(context, MusicPlayerService::class.java).apply {
            this.action = action
            context.startService(this)
        }
    }
}