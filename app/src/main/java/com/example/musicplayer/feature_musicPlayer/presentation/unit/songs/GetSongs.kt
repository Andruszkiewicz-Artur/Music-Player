package com.example.musicplayer.feature_musicPlayer.presentation.unit.songs

import android.os.Environment
import java.io.File

fun GetSongs(file: File = Environment.getExternalStorageDirectory()): Array<File> {
    val songs = mutableListOf<File>()
    val files = file.listFiles()

    System.out.println(files)
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