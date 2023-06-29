package com.example.musicplayer.feature_musicPlayer.presentation.songsList.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.musicplayer.feature_musicPlayer.core.Permisions.isExternalStoragePermissions
import com.example.musicplayer.feature_musicPlayer.presentation.songsList.SongsListEvent

@Composable
fun PermissionPresentaiton(
    onClick: () -> Unit
) {
    Text(
        text = "You need allow permissions!",
        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        textAlign = TextAlign.Center
    )
    Button(
        onClick = {
            onClick()
        },
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Text(
            text = "Allow permissions",
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}