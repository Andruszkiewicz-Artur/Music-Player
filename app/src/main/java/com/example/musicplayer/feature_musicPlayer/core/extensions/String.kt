package com.example.musicplayer.feature_musicPlayer.core.extensions

fun String.deleteExtensionFile(): String {
    return this.substringBeforeLast('.')
}