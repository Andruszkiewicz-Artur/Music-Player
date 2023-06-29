package com.example.musicplayer.feature_musicPlayer.di

import android.app.NotificationManager
import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.app.NotificationCompat
import com.example.musicplayer.R
import com.example.musicplayer.feature_musicPlayer.core.constants.Constants.NOTIFICATION_CHANNEL_ID
import com.example.musicplayer.feature_musicPlayer.domain.service.ServiceHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@ExperimentalAnimationApi
@Module
@InstallIn(ServiceComponent::class)
class NotificationModule {
    @ServiceScoped
    @Provides
    fun provideNotificationBuilder(
        @ApplicationContext context: Context
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Music player")
            .setContentText("Music title")
            .setSmallIcon(R.drawable.baseline_music_note_24)
            .setOngoing(true)
            .addAction(R.drawable.baseline_skip_previous_24, "", ServiceHelper.previousPendingIntent(context))
            .addAction(R.drawable.baseline_pause_24, "", ServiceHelper.stopPendingIntent(context))
            .addAction(R.drawable.baseline_skip_next_24, "", ServiceHelper.nextPendingIntent(context))
            .setContentIntent(ServiceHelper.clickPendingIntent(context))
    }

    @ServiceScoped
    @Provides
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
}