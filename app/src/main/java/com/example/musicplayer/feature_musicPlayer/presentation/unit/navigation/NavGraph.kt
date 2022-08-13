package com.example.musicplayer.feature_musicPlayer.presentation.unit.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
            route = Screen.playerScreen.route + "?path={path}",
            arguments = listOf(
                navArgument("path") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
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
    ) fun sendPath(
        path: String
    ): String {
        return this.route + "?uri=$path"
    }
}