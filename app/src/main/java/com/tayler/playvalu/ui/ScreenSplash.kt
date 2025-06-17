package com.tayler.playvalu.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tayler.playvalu.R
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ScreenSplash(viewModel: AppViewModel, navController: NavController  = rememberNavController()){


    viewModel.loadValidateLogin()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is InitUiEvent.NavigateToNext -> {
                    //navController.navigate(Screen.LoginScreen.route)
                }
                else ->  {} } }
    }

    Column(modifier = Modifier.fillMaxSize().
    paint(
        painterResource(id = R.drawable.background_splash),contentScale
    = ContentScale.FillBounds
    ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(  horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = stringResource(R.string.text_welcome)
                , color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(modifier = Modifier.padding(top = 10.dp), text = stringResource(R.string.text_title_splash),color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                style = MaterialTheme.typography.bodyMedium)
        }
        Text(modifier = Modifier.padding(top = 10.dp),text = stringResource(R.string.app_name),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            style = MaterialTheme.typography.bodyMedium)
    }
}