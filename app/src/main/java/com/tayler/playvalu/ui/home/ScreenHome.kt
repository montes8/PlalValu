package com.tayler.playvalu.ui.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.SideEffect
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
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
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
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenHome(viewModel: AppViewModel) {
    val context = LocalContext.current
    val activity = (LocalContext.current as? Activity)
    var permission by remember { mutableStateOf(false) }
    var visibleMusic by remember { mutableStateOf(false) }
    var stateMusic by remember { mutableStateOf(true) }
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    var musicDuration by remember { mutableIntStateOf(0) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { isGranted ->
            if (
                isGranted[Manifest.permission.READ_EXTERNAL_STORAGE] == true

            ) {
                permission = true
            } else {
                Log.e("MainActivity", "ERROR PERMISSION NOT GRANTED")
                Log.e(
                    "MainActivity",
                    "READ_EXTERNAL_STORAGE = ${isGranted.get(Manifest.permission.READ_EXTERNAL_STORAGE)}"
                )
                Log.e(
                    "MainActivity",
                    "WRITE_EXTERNAL_STORAGE = ${isGranted.get(Manifest.permission.WRITE_EXTERNAL_STORAGE)}"
                )
            }
        }
    )

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

    if(visibleMusic){ mylamda.start() }else{ mylamda.interrupt()}

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { // if android 11+ request MANAGER_EXTERNAL_STORAGE
        if (!Environment.isExternalStorageManager()) { // check if we already have permission
            val uri = String.format(
                Locale.ENGLISH,
                "package:%s",
                context.packageName
            ).toUri()
            context.startActivity(
                Intent(
                    Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                    uri
                )
            )
        } else {
            Log.d("persimovalu", "otorgado por el usuario mayo R")
            permission = true

        }
    } else {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) { // ch
            // eck if we already have permission
            SideEffect {
                launcher.launch(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                )
            }

        } else {
            permission = true
            Log.d("persimovalu", "otorgado por el usuario")
        }
    }

    Column {
        UiTayCToolBar(uiTayText = "Lista de tus canciones en PlayValu", uiTayModifier = UiTayToolBarModel(
            uTTypeEnd = true
        )) {flag ->
            if (!flag){
                activity?.startService(Intent(context, MusicService::class.java))
                activity?.finish()
            }

        }
        if (permission) {
            viewModel.loadMusic()
            if (viewModel.uiStateListMusic.isNotEmpty()) {
                Box {
                    ConfigLisMusic(viewModel) { index ->
                        visibleMusic = true
                        viewModel.uiStatePosition = index
                        viewModel.uiStateMusic = viewModel.uiStateListMusic[index]
                        MediaPlayerSingleton.playStart(viewModel.uiStateMusic.path)
                        musicDuration = MediaPlayerSingleton.playDuration()/100
                        Log.d("tagprogree",musicDuration.toString())
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
                                                        viewModel.uiStateListMusic[viewModel.uiStatePosition]
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
                                                if (positionCurrent < viewModel.uiStateListMusic.size - 1) {
                                                    viewModel.uiStatePosition = positionCurrent + 1
                                                    viewModel.uiStateMusic =
                                                        viewModel.uiStateListMusic[viewModel.uiStatePosition]
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
            itemsIndexed(viewModel.uiStateListMusic) { index, music ->
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
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.padding(12.dp))
            Column {
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
        }
    }
}

