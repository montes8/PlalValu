package com.tayler.playvalu.ui.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tayler.playvalu.R
import com.tayler.playvalu.component.MediaPlayerSingleton
import com.tayler.playvalu.component.UiTayCToolBar
import com.tayler.playvalu.model.MusicModel
import com.tayler.playvalu.ui.AppViewModel
import com.tayler.playvalu.utils.TypographyBoldDark
import java.util.Locale

@Composable
fun ScreenHome(viewModel: AppViewModel, navController: NavController = rememberNavController()) {
    val context = LocalContext.current

    var permission by remember { mutableStateOf(false) }
    var visibleMusic by remember { mutableStateOf(false) }

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
        UiTayCToolBar(uiTayText = "Lista de canciones play valu") {}
        if (permission) {
            viewModel.loadMusic()
            if (viewModel.uiStateListMusic.isNotEmpty()) {
                Box {
                    ConfigLisMusic(viewModel){music ->
                        visibleMusic = true
                        MediaPlayerSingleton.getInstanceMusic()?.apply {
                            this.stop()
                        }
                        MediaPlayerSingleton.setMediaPlayerSingleton()
                        MediaPlayerSingleton.getInstanceMusic()?.setDataSource(music.path)
                        MediaPlayerSingleton.getInstanceMusic()?.prepare()
                        MediaPlayerSingleton.getInstanceMusic()?.start()

                    }
                    if (visibleMusic) {
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                            .clickable {
                                visibleMusic = false
                                MediaPlayerSingleton.getInstanceMusic()?.apply {
                                    this.stop()
                                }
                            }
                            .background(Color.Blue)
                            .align(Alignment.BottomCenter)) {
                            Text(
                                text = "Nombre de cancion", maxLines = 1,
                                style = TypographyBoldDark.titleLarge
                            )
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun ConfigLisMusic(viewModel: AppViewModel,onClick: (MusicModel) -> Unit) {
    Column(
        modifier = Modifier.padding(
            top = 12.dp, end = 8.dp,
            start = 8.dp
        )
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(viewModel.uiStateListMusic) { music ->
                MusicItem(music){
                    onClick(music)
                }
            }
        }

    }

}

@Composable
fun MusicItem(model: MusicModel,onClick: (MusicModel) -> Unit) {
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
                    onClick(model)
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
                    style = TypographyBoldDark.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = model.name, maxLines = 2,
                    style = TypographyBoldDark.labelLarge
                )
            }
        }
    }
}

