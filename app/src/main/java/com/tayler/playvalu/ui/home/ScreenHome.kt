package com.tayler.playvalu.ui.home

import android.util.Log
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tayler.playvalu.component.Screen
import com.tayler.playvalu.ui.AppViewModel
import com.tayler.playvalu.ui.splash.InitUiEvent
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.tayler.playvalu.R
import com.tayler.playvalu.model.MusicModel
import com.tayler.playvalu.utils.TypographyBoldDark

@Composable
fun ScreenHome(viewModel: AppViewModel, navController: NavController  = rememberNavController()){

    Box{
        Column(modifier = Modifier.padding(top = 20.dp, end = 20.dp,
            start = 20.dp)) {

             if (viewModel.uiStateListMusic.isNotEmpty()){
                 Log.d("listamuca",viewModel.uiStateListMusic.toString())
                 Column(modifier = Modifier.fillMaxSize()) {
                     Text(text = "Listado de canciones")
                    LazyColumn(modifier = Modifier.fillMaxSize().background(Color.Yellow)){
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
            Column() {
                Text(text = "Nombre", maxLines = 1,
                    style = TypographyBoldDark.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(text = model.name, maxLines = 5,
                    style = TypographyBoldDark.titleSmall)
            }
        }
    }
}

