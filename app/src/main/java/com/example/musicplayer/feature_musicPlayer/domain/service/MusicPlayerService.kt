package com.example.musicplayer.feature_musicPlayer.domain.service

import android.app.Service
import android.content.Intent
import android.os.Binder

class MusicPlayerService: Service() {

    private val binder = MusicPlayerBinder()

    override fun onBind(p0: Intent?) = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return super.onStartCommand(intent, flags, startId)
    }

    inner class MusicPlayerBinder : Binder() {
        fun getService(): MusicPlayerService = this@MusicPlayerService
    }
}