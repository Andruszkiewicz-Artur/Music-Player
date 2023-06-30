package com.example.musicplayer.feature_musicPlayer.presentation.unit.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.musicplayer.feature_musicPlayer.domain.service.MusicPlayerService
import com.example.musicplayer.feature_musicPlayer.presentation.player.compose.Player
import com.example.musicplayer.feature_musicPlayer.presentation.songsList.compose.SongsList


@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    service: MusicPlayerService
) {
    NavHost(
        navController = navController,
        startDestination = Screen.songsListScreen.route
    ) {
        composable(
            route = Screen.songsListScreen.route
        ) {
            SongsList(
                navController = navController,
                service = service
            )
        }

        composable(
            route = Screen.playerScreen.route
        ) {
            Player(
                service = service
            )
        }
    }
}