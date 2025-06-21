package com.tayler.playvalu.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tayler.playvalu.R
import com.tayler.playvalu.model.MusicModel
import com.tayler.playvalu.ui.AppViewModel
import com.tayler.playvalu.utils.TypographyBoldDark
import androidx.compose.runtime.*
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import com.tayler.playvalu.ui.getMusic
import java.util.Locale

@Composable
fun ScreenHome(viewModel: AppViewModel, navController: NavController  = rememberNavController()){
    val context = LocalContext.current

    var permission by remember { mutableStateOf(true) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = {isGranted ->
            if (
                isGranted[Manifest.permission.READ_EXTERNAL_STORAGE] == true

            )  {
                permission = true
            } else {
                Log.e("MainActivity", "ERROR PERMISSION NOT GRANTED")
                Log.e("MainActivity", "READ_EXTERNAL_STORAGE = ${isGranted.get(Manifest.permission.READ_EXTERNAL_STORAGE)}")
                Log.e("MainActivity", "WRITE_EXTERNAL_STORAGE = ${isGranted.get(Manifest.permission.WRITE_EXTERNAL_STORAGE)}")
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
        }else{
            Log.d("persimovalu","otorgado por el usuario mayo R")
            permission = true

        }
    } else {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) { // ch
            // eck if we already have permission
            launcher.launch( arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE
            ))

        }else{
            Log.d("persimovalu","otorgado por el usuario")
        }
    }



    if(permission){
        configLisMusic(viewModel)
    }else{
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) -> {
                configLisMusic(viewModel)
            }
            else -> {
                SideEffect {
                    launcher.launch( arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ))
                }

            }
        }
    }

}

@Composable
fun configLisMusic(viewModel: AppViewModel){
    viewModel.loadMusic()
    Box{
        Column(modifier = Modifier.padding(top = 20.dp, end = 20.dp,
            start = 20.dp)) {
            if (viewModel.uiStateListMusic.isNotEmpty()){
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(text = "Listado de canciones")
                    LazyColumn(modifier = Modifier.fillMaxSize()){
                        items(viewModel.uiStateListMusic){music ->
                            MusicItem(music)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MusicItem(model: MusicModel){
    Card (shape = RoundedCornerShape(20.dp), elevation = CardDefaults.cardElevation(
        defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.White
        ), modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .clickable {
                //accion del item click
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
                Text(text = "Nombre de cancion", maxLines = 1,
                    style = TypographyBoldDark.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(text = model.name, maxLines = 5,
                    style = TypographyBoldDark.titleSmall)
            }
        }
    }
}

