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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.tayler.playvalu.utils.formatTimePlayer
import com.tayler.playvalu.utils.getActivityOrNull
import com.tayler.playvalu.utils.permission.PermissionManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenHome(viewModel: AppViewModel) {
    val context = LocalContext.current
    val activity = context.getActivityOrNull()

    val sleep = Thread({
        while (viewModel.sliderPosition < viewModel.musicDuration) {
            Thread.sleep((1000).toLong())
                try {
                    viewModel.sliderPosition = (MediaPlayerSingleton.playCurrentPosition()).toFloat()
                    viewModel.textProgress = formatTimePlayer(MediaPlayerSingleton.playCurrentPosition())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
        }
    })
    viewModel.loadMusic()
    if(viewModel.visibleMusic){
        sleep.start()
    }else{
        sleep.interrupt()
    }


    Column {
        UiTayCToolBar(uiTayText = stringResource(R.string.tb_title_home), uiTayModifier = UiTayToolBarModel(
            uTTypeEnd = true
        )) {
            MediaPlayerSingleton.positionMusic =  viewModel.uiStatePosition
            MediaPlayerSingleton.positionDurationMusic = MediaPlayerSingleton.playCurrentPosition()
            PermissionManager.checkOverlayPermission(context) {
                    activity?.startService(Intent(context, MusicService::class.java))
                    MediaPlayerSingleton.playStop()
                    activity?.finish()
            }
    }
        if (viewModel.uiStateDataMusic.listMusic.isNotEmpty()) {
                Box {
                    ConfigLisMusic(viewModel) { index ->
                        viewModel.visibleMusic = true
                        viewModel.stateMusic = true
                        viewModel.uiStatePosition = index
                        viewModel.uiStateMusic = viewModel.uiStateDataMusic.listMusic[index]
                        MediaPlayerSingleton.playStart(viewModel.uiStateMusic.path)
                        viewModel.musicDuration = MediaPlayerSingleton.playDuration()
                    }
                    if (viewModel.visibleMusic) {
                        LadMusicDetail(viewModel)
                    }
                }
            }
        }

    if (viewModel.uiStateDataMusic.uiStateLoading){
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(
                modifier = Modifier
                    .background(Color.Transparent)
                    .wrapContentSize(), color = colorResource(R.color.primary_pink)
            )

        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LadMusicDetail(viewModel: AppViewModel){
    Box(modifier = Modifier.fillMaxHeight()) {
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
                            .padding(top = 12.dp, start = 12.dp, end = 12.dp),
                        text = viewModel.uiStateMusic.name.replace(".mp3", ""),
                        maxLines = 1,
                        color = colorResource(R.color.ui_tay_black),
                        style = TypographySubTitleGabbi.labelMedium,
                    )
                    Image(
                        painterResource(R.drawable.ic_close),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 8.dp, end = 8.dp)
                            .clickable {
                                viewModel.visibleMusic = false
                                viewModel.sliderPosition = 0f
                                viewModel.musicDuration = 0
                                MediaPlayerSingleton.playStop()
                            },
                        contentDescription = "closeMusic",
                        contentScale = ContentScale.Crop
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.CenterVertically).width(40.dp),
                        text = viewModel.textProgress,
                        maxLines = 1,
                        color = colorResource(R.color.ui_tay_black),
                        style = TypographySubTitleGabbi.labelSmall)
                    Image(
                        painterResource(R.drawable.ic_skip_previous),
                        modifier = Modifier
                            .padding(start = 12.dp, end = 24.dp)
                            .clickable {
                                previousMusic(viewModel)
                            },
                        contentDescription = "closeMusic",
                        contentScale = ContentScale.Crop
                    )
                    Image(
                        painterResource(if (viewModel.stateMusic) R.drawable.ic_pause else R.drawable.ic_play),
                        modifier = Modifier
                            .padding(start = 24.dp, end = 24.dp)
                            .clickable {
                                playStateMusic(viewModel.stateMusic)
                                viewModel.stateMusic = !viewModel.stateMusic
                            },
                        contentDescription = "closeMusic",
                        contentScale = ContentScale.Crop
                    )
                    Image(
                        painterResource(R.drawable.ic_skip_next),
                        modifier = Modifier
                            .padding(start = 24.dp, end = 12.dp)
                            .clickable {
                                nextMusic(viewModel)
                            },
                        contentDescription = "closeMusic",
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        text = formatTimePlayer(MediaPlayerSingleton.playDuration()),
                        maxLines = 1,
                        color = colorResource(R.color.ui_tay_black),
                        style = TypographySubTitleGabbi.labelSmall)
                }
                Slider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .padding(horizontal = 8.dp),
                    value = viewModel.sliderPosition,
                    enabled = false,
                    onValueChange = {
                        viewModel.sliderPosition = it
                    },
                    valueRange = 0f..viewModel.musicDuration.toFloat(),
                    thumb = {
                        Box(
                            Modifier
                                .size(20.dp)
                                .padding(4.dp)
                                .background(
                                    colorResource(R.color.primary_pink),
                                    CircleShape
                                )
                        )
                    },

                    track = { sliderState ->
                        if (sliderState.value > viewModel.musicDuration -1000){
                            nextMusic(viewModel)
                        }
                        val fraction by remember {
                            derivedStateOf {
                                (viewModel.sliderPosition - sliderState.valueRange.start) / (sliderState.valueRange.endInclusive - sliderState.valueRange.start)
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
                                    .background(
                                        colorResource(R.color.ui_tay_gray),
                                        CircleShape
                                    )
                            )
                        }
                    }

                )
            }
        }
    }

}

fun nextMusic(viewModel: AppViewModel){
    var positionCurrent = viewModel.uiStatePosition
    if (positionCurrent < viewModel.uiStateDataMusic.listMusic.size - 1) {
        viewModel.sliderPosition = 0f
        viewModel.musicDuration = 0
        viewModel.uiStatePosition = positionCurrent + 1
        viewModel.uiStateMusic =
            viewModel.uiStateDataMusic.listMusic[viewModel.uiStatePosition]
        MediaPlayerSingleton.playStart(viewModel.uiStateMusic.path)
        viewModel.musicDuration =
            MediaPlayerSingleton.playDuration()
    }
}

fun previousMusic(viewModel: AppViewModel) {
    var positionCurrent = viewModel.uiStatePosition
    if (positionCurrent > 0) {
        viewModel.sliderPosition = 0f
        viewModel.musicDuration = 0
        viewModel.uiStatePosition = positionCurrent - 1
        viewModel.uiStateMusic =
            viewModel.uiStateDataMusic.listMusic[viewModel.uiStatePosition]
        MediaPlayerSingleton.playStart(viewModel.uiStateMusic.path)
        viewModel.musicDuration =
            MediaPlayerSingleton.playDuration()
    }
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
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = model.name.replace(".mp3", ""), maxLines = 2,
                    color = colorResource(R.color.ui_tay_black),
                    style = TypographySubTitleGabbi.titleMedium
                )
            }
            Image(
                painterResource(R.drawable.ic_play),
                modifier = Modifier.weight(0.5f),
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
        }
    }
}

