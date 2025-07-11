package com.tayler.playvalu.ui.home

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tayler.playvalu.R
import com.tayler.playvalu.component.MediaPlayerSingleton
import com.tayler.playvalu.component.MediaPlayerSingleton.playStateMusic
import com.tayler.playvalu.component.UiTayCToolBar
import com.tayler.playvalu.model.MusicModel
import com.tayler.playvalu.model.UiTayToolBarModel
import com.tayler.playvalu.ui.AppViewModel
import com.tayler.playvalu.ui.service.MusicService
import com.tayler.playvalu.utils.TypographySubTitleGabbi
import com.tayler.playvalu.utils.TypographyTitleBold
import com.tayler.playvalu.utils.getActivityOrNull
import com.tayler.playvalu.utils.permission.PermissionManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenHome(viewModel: AppViewModel) {
    val context = LocalContext.current
    val activity = context.getActivityOrNull()
    var visibleMusic by remember { mutableStateOf(false) }
    var stateMusic by remember { mutableStateOf(true) }
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    var musicDuration by remember { mutableIntStateOf(0) }

    val mylamda = Thread({
        Log.d("tagprogree","mylamda")
        sliderPosition = 0f
        while (sliderPosition < musicDuration) {
            try {
                Thread.sleep((500).toLong())
                sliderPosition = (MediaPlayerSingleton.playCurrentPosition()/100).toFloat()
                Log.d("tagprogree",sliderPosition.toString())
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("tagprogree","error")
            }
        }
    })

    viewModel.loadMusic()

    if(visibleMusic){ mylamda.start() }else{ mylamda.interrupt()}
    Column {
        UiTayCToolBar(uiTayText = stringResource(R.string.tb_title_home), uiTayModifier = UiTayToolBarModel(
            uTTypeEnd = true
        )) {
            PermissionManager.checkOverlayPermission(context) {
                    activity?.startService(Intent(context, MusicService::class.java))
                    activity?.finish()
            }
        }

        if (viewModel.uiStateDataMusic.listMusic.isNotEmpty()) {
                Box {
                    ConfigLisMusic(viewModel) { index ->
                        visibleMusic = true
                        viewModel.uiStatePosition = index
                        viewModel.uiStateMusic = viewModel.uiStateDataMusic.listMusic[index]
                        MediaPlayerSingleton.playStart(viewModel.uiStateMusic.path)
                        musicDuration = MediaPlayerSingleton.playDuration()/100
                    }
                    if (visibleMusic) {
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 10.dp
                            ),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White,
                                contentColor = Color.White
                            ),
                            border = BorderStroke(width = 2.dp,   color = colorResource(R.color.primary_pink)),
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
                                Box(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        modifier = Modifier
                                            .align(Alignment.TopCenter)
                                            .padding(top = 12.dp, start = 16.dp, end = 16.dp),
                                        text = viewModel.uiStateMusic.name.replace(".mp3", ""),
                                        maxLines = 1,
                                        color = colorResource(R.color.primary_Accent),
                                        style = TypographySubTitleGabbi.labelLarge,
                                        )
                                    Image(
                                        painterResource(R.drawable.ic_close),
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(top = 8.dp, end = 8.dp)
                                            .clickable {
                                                visibleMusic = false
                                                sliderPosition = 0f
                                                musicDuration = 0
                                                MediaPlayerSingleton.playStop()
                                            },
                                        contentDescription = "closeMusic",
                                        contentScale = ContentScale.Crop
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Image(
                                        painterResource(R.drawable.ic_skip_previous),
                                        modifier = Modifier
                                            .padding(end = 24.dp)
                                            .clickable {
                                                stateMusic = true

                                                var positionCurrent = viewModel.uiStatePosition
                                                if (positionCurrent > 0) {
                                                    sliderPosition = 0f
                                                    musicDuration = 0
                                                    viewModel.uiStatePosition = positionCurrent - 1
                                                    viewModel.uiStateMusic =
                                                        viewModel.uiStateDataMusic.listMusic[viewModel.uiStatePosition]
                                                    MediaPlayerSingleton.playStart(viewModel.uiStateMusic.path)
                                                    musicDuration = MediaPlayerSingleton.playDuration()/100
                                                }
                                            },
                                        contentDescription = "closeMusic",
                                        contentScale = ContentScale.Crop
                                    )
                                    Image(
                                        painterResource(if (stateMusic) R.drawable.ic_pause else R.drawable.ic_play),
                                        modifier = Modifier
                                            .padding(start = 24.dp, end = 24.dp)
                                            .clickable {
                                                playStateMusic(stateMusic)
                                                stateMusic = !stateMusic
                                            },
                                        contentDescription = "closeMusic",
                                        contentScale = ContentScale.Crop
                                    )
                                    Image(
                                        painterResource(R.drawable.ic_skip_next),
                                        modifier = Modifier
                                            .padding(start = 24.dp)
                                            .clickable {
                                                stateMusic = true
                                                sliderPosition = 0f
                                                musicDuration = 0
                                                var positionCurrent = viewModel.uiStatePosition
                                                if (positionCurrent < viewModel.uiStateDataMusic.listMusic.size - 1) {
                                                    viewModel.uiStatePosition = positionCurrent + 1
                                                    viewModel.uiStateMusic =
                                                        viewModel.uiStateDataMusic.listMusic[viewModel.uiStatePosition]
                                                    MediaPlayerSingleton.playStart(viewModel.uiStateMusic.path)
                                                    musicDuration = MediaPlayerSingleton.playDuration()/100
                                                }
                                            },
                                        contentDescription = "closeMusic",
                                        contentScale = ContentScale.Crop
                                    )
                                }


                                Slider(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(20.dp)
                                        .padding(horizontal = 8.dp),
                                    value = sliderPosition,
                                    enabled = false,
                                    onValueChange = { sliderPosition = it },
                                    valueRange = 0f..musicDuration.toFloat(),
                                    thumb = {
                                        Box(
                                            Modifier
                                                .size(20.dp)
                                                .padding(4.dp)
                                                .background(
                                                    colorResource(R.color.primary_Accent),
                                                    CircleShape
                                                )
                                        )
                                    },

                                    track = { sliderState ->
                                        val fraction by remember {
                                            derivedStateOf {
                                                (sliderState.value - sliderState.valueRange.start) / (sliderState.valueRange.endInclusive - sliderState.valueRange.start)
                                            }
                                        }

                                        Box(Modifier.fillMaxWidth()) {
                                            Box(
                                                Modifier
                                                    .fillMaxWidth(fraction)
                                                    .align(Alignment.CenterStart)
                                                    .height(2.dp)
                                                    .background(
                                                        colorResource(R.color.primary_Accent),
                                                        CircleShape
                                                    )
                                            )
                                            Box(
                                                Modifier
                                                    .fillMaxWidth(1f - fraction)
                                                    .align(Alignment.CenterEnd)
                                                    .height(2.dp)
                                                    .background(colorResource(R.color.primary_pink), CircleShape)
                                            )
                                        }
                                    }

                                )
                            }
                        }

                    }
                }
            }
        }

}

@Composable
private fun ConfigReproduceView(){

}

@Composable
fun ConfigLisMusic(viewModel: AppViewModel, onClick: (Int) -> Unit) {
    Column(
        modifier = Modifier.padding(
            top = 12.dp, end = 8.dp,
            start = 8.dp
        )
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(viewModel.uiStateDataMusic.listMusic) { index, music ->
                MusicItem(music, index) {
                    onClick(it)
                }
            }
        }

    }

}

@Composable
fun MusicItem(model: MusicModel, position: Int, onClick: (Int) -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp), elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.White
        ), modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onClick(position)
                }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Image(
                painterResource(R.drawable.ic_music),
                modifier = Modifier.weight(0.6f),
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.padding(12.dp))
            Column( modifier = Modifier.weight(6f),) {
                Text(
                    text = "Nombre de cancion", maxLines = 1,
                    color = colorResource(R.color.primary_Accent),
                    style = TypographyTitleBold.labelLarge
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = model.name.replace(".mp3", ""), maxLines = 2,
                    color = colorResource(R.color.ui_tay_black),
                    style = TypographySubTitleGabbi.titleMedium
                )
            }
            Image(
                painterResource(R.drawable.ic_skip_next),
                modifier = Modifier.weight(1f),
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
        }
    }
}

