package com.tayler.playvalu.ui.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tayler.playvalu.R
import com.tayler.playvalu.component.Screen
import com.tayler.playvalu.ui.AppViewModel
import com.tayler.playvalu.utils.TypographyTitleBold
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ScreenSplash(viewModel: AppViewModel, navController: NavController  = rememberNavController()){


    viewModel.loadValidateLogin()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is InitUiEvent.NavigateToNext -> {
                    navController.navigate(Screen.HomeScreen.route)
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
            Text(text = stringResource(R.string.text_welcome),
                textAlign = TextAlign.Center,
                style = TypographyTitleBold.titleLarge
            )
            Text(modifier = Modifier.padding(top = 10.dp),
                text = stringResource(R.string.text_title_splash),
                textAlign = TextAlign.Center,
                style = TypographyTitleBold.titleMedium)
        }
        Text(modifier = Modifier.padding(top = 10.dp),
            text = stringResource(R.string.app_name),
            textAlign = TextAlign.Center,
            style = TypographyTitleBold.titleLarge)
    }
}

