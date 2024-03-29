package com.example.musicplayer.feature_musicPlayer.presentation.unit.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.musicplayer.feature_musicPlayer.core.constants.Constants
import com.example.musicplayer.feature_musicPlayer.domain.service.MusicPlayerService
import com.example.musicplayer.feature_musicPlayer.presentation.player.compose.Player
import com.example.musicplayer.feature_musicPlayer.presentation.songsList.compose.SongsList


@OptIn(ExperimentalAnimationApi::class)
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
            route = Screen.playerScreen.route + "?${Constants.SONG_URI}={${Constants.SONG_URI}}",
            arguments = listOf(
                navArgument(Constants.SONG_URI) {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            Player(
                service = service
            )
        }
    }
}