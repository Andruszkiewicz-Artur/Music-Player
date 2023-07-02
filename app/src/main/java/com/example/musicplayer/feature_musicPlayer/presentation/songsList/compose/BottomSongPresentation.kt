package com.example.musicplayer.feature_musicPlayer.presentation.songsList.compose

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.musicplayer.R
import com.example.musicplayer.feature_musicPlayer.core.extensions.deleteExtensionFile
import com.example.musicplayer.feature_musicPlayer.domain.service.MusicPlayerState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BottomSongPresentation(
    state: MusicPlayerState,
    onClickPlay: () -> Unit,
    onClickStop: () -> Unit,
    onClickRecord: () -> Unit
) {
    if(state.currentSong != null && state.musicPlayer != null) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(24.dp)
                .background(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(8.dp)
                .clickable {
                    onClickRecord()
                }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_music_note_24),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 8.dp)
                )
                Text(
                    text = state.currentSong.name.deleteExtensionFile(),
                    maxLines = 1,
                    modifier = Modifier
                        .basicMarquee(
                            iterations = Int.MAX_VALUE,
                            delayMillis = 0,
                            initialDelayMillis = 0,
                            velocity = 100.dp
                        )
                )
            }
            AnimatedContent(targetState = state.musicPlayer.isPlaying) {isPlaying ->
                if (isPlaying) {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_pause_24),
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                onClickStop()
                            }
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_play_arrow_24),
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .clickable {
                                onClickPlay()
                            }
                    )
                }
            }
        }
    }
}