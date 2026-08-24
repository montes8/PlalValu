package com.tayler.playvalu.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tayler.playvalu.R
import com.tayler.playvalu.ui.AppViewModel
import com.tayler.playvalu.utils.formatTimePlayer
import com.valu.uitaycompose.utils.textGabbi14
import com.valu.uitaycompose.utils.textGabbi16

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LadMusicDetail(viewModel: AppViewModel) {
    Box(modifier = Modifier
        .fillMaxHeight()
        .background(Color.Transparent)) {
        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .fillMaxWidth()
            ) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(top = 12.dp, start = 12.dp, end = 12.dp),

                        text = viewModel.uiStateMusic.name.replace(".mp3", ""),
                        maxLines = 2,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = textGabbi16,
                    )
                    Image(
                        painterResource(R.drawable.ic_close),
                        modifier = Modifier
                            .align(Alignment.TopEnd).padding(2.dp)
                            .clickable {
                                viewModel.stopMusic()
                            },
                        contentDescription = "closeMusic",
                        contentScale = ContentScale.Crop
                    )
                }

                CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
                    Slider(
                        value = viewModel.sliderPosition,
                        onValueChange = { viewModel.onSeek(it) },
                        valueRange = 0f..viewModel.musicDuration.toFloat(),
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thumb = {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .offset(y = 1.dp)
                                    .background(MaterialTheme.colorScheme.primary, CircleShape)
                                    .border(1.dp, Color.Black, CircleShape)
                            )
                        },
                        track = { sliderState ->
                            val fraction = if (sliderState.valueRange.endInclusive > sliderState.valueRange.start) {
                                (sliderState.value - sliderState.valueRange.start) / (sliderState.valueRange.endInclusive - sliderState.valueRange.start)
                            } else 0f

                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .height(12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    Modifier
                                        .fillMaxWidth()
                                        .height(4.dp)
                                        .background(
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                            CircleShape
                                        )
                                )
                                Box(
                                    Modifier
                                        .fillMaxWidth(fraction)
                                        .height(4.dp)
                                        .align(Alignment.CenterStart)
                                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                                )
                            }
                        }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .width(40.dp),
                        text = viewModel.textProgress,
                        maxLines = 1,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = textGabbi14
                    )
                    Image(
                        painterResource(R.drawable.ic_skip_previous),
                        modifier = Modifier
                            .padding(start = 12.dp, end = 12.dp)
                            .clickable {
                                viewModel.previousMusic()
                            },
                        contentDescription = "previous",
                        contentScale = ContentScale.Crop
                    )
                    Image(
                        painterResource(if (viewModel.stateMusic) R.drawable.ic_pause else R.drawable.ic_play),
                        modifier = Modifier
                            .padding(start = 24.dp, end = 24.dp)
                            .clickable {
                                viewModel.togglePlayPause()
                            },
                        contentDescription = "playPause",
                        contentScale = ContentScale.Crop
                    )
                    Image(
                        painterResource(R.drawable.ic_skip_next),
                        modifier = Modifier
                            .padding(start = 12.dp, end = 12.dp)
                            .clickable {
                                viewModel.nextMusic()
                            },
                        contentDescription = "next",
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        text = formatTimePlayer(viewModel.musicDuration),
                        maxLines = 1,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = textGabbi14
                    )
                }
            }
        }
    }
}
