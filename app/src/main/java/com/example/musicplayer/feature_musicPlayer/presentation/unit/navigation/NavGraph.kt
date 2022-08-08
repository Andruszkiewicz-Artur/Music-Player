package com.example.musicplayer.feature_musicPlayer.presentation.unit.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.musicplayer.feature_musicPlayer.presentation.player.compose.Player
import com.example.musicplayer.feature_musicPlayer.presentation.songsList.compose.SongsList


@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.songsListScreen.route
    ) {
        composable(
            route = Screen.songsListScreen.route
        ) {
            SongsList(
                navController = navController
            )
        }

        composable(
            route = Screen.playerScreen.route
        ) {
            Player(
                navController = navController
            )
        }
    }
}

sealed class Screen(
    val route: String
) {
    object songsListScreen: Screen(
        route = "SongsListScreen"
    )

    object playerScreen: Screen(
        route = "PlayerScreen"
    )
}