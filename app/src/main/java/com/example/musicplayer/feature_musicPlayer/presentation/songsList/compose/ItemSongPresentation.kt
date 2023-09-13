package com.example.musicplayer.feature_musicPlayer.presentation.songsList.compose

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.musicplayer.R
import com.example.musicplayer.feature_musicPlayer.core.extensions.deleteExtensionFile
import java.io.File

@Composable
fun ItemSongPresentation(
    song: File,
    isLast: Boolean,
    isPlaying: Boolean,
    onClickRecord: () -> Unit,
    onClickPlay: () -> Unit,
    onClickStop: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable {
                onClickRecord()
            }
    ) {
        Text(
            text = song.name.deleteExtensionFile(),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth(),
            maxLines = 2
        )
        AnimatedContent(
            targetState = isPlaying
        ) {
            if (it) {
                Icon(
                    imageVector = Icons.Filled.Stop,
                    contentDescription = stringResource(id = R.string.Play),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .weight(1f)
                        .size(40.dp)
                        .padding(start = 8.dp)
                        .clickable {
                            onClickStop()
                        }
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = stringResource(id = R.string.Play),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .weight(1f)
                        .size(40.dp)
                        .padding(start = 8.dp)
                        .clickable {
                            onClickPlay()
                        }
                )
            }
        }
    }
    if (!isLast) {
        Divider(
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .height(1.dp)
        )
    } else {
        Spacer(modifier = Modifier.height(8.dp))
    }
}