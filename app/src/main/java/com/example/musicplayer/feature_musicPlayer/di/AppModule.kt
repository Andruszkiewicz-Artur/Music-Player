package com.example.musicplayer.feature_musicPlayer.di

import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import com.example.musicplayer.feature_musicPlayer.presentation.unit.songs.GetSongs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideContext(application: Application): Context = application.applicationContext

    @Singleton
    @Provides
    fun provideMediaPlayer(): MediaPlayer = MediaPlayer()

    @Singleton
    @Provides
    fun provideSongsList(): Array<File> = GetSongs()
}