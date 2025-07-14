package com.tayler.playvalu.ui.splash

import android.os.Handler
import android.os.Looper
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tayler.playvalu.R
import com.tayler.playvalu.component.AnimatedLotti
import com.tayler.playvalu.component.Screen
import com.tayler.playvalu.ui.AppViewModel
import com.tayler.playvalu.utils.TypographyTitleBold
import com.tayler.playvalu.utils.permission.PermissionManager
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ScreenSplash(viewModel: AppViewModel, navController: NavController = rememberNavController()) {

    val context = LocalContext.current

    var animLotti by remember { mutableStateOf(false) }
    var animText by remember { mutableStateOf(false) }
    val offset by animateDpAsState(
        targetValue = if (animLotti) 0.dp else (-500).dp,
        animationSpec = tween(
            durationMillis = 2500,
            easing = LinearEasing
        ),
        label = "Animation top"
    )

    val offsetBottom by animateDpAsState(
        targetValue = if (animText) (-30).dp else (500).dp,
        animationSpec = tween(
            durationMillis = 1500,
            easing = LinearEasing
        ),
        label = "Animation bottom"
    )

    Handler(Looper.getMainLooper()).postDelayed({
        animText = true
        animLotti = true },100)

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            if (event is InitUiEvent.NavigateToNext) {
                navController.navigate(Screen.HomeScreen.route)
            }
        }
    }

    PermissionManager.CheckFilePermission(context) { isGranted ->
        if (isGranted) {
            viewModel.loadValidateLogin()
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.background_splash), contentScale
                = ContentScale.FillBounds
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painterResource(R.drawable.ic_music_bg),
            contentDescription = "",
            contentScale = ContentScale.Crop
        )

        Box( modifier = Modifier
            .offset(y = offset)
            .graphicsLayer()) {
            AnimatedLotti(modifier = Modifier.width(250.dp).height(200.dp).align(Alignment.Center))
        }

        Column(modifier = Modifier
            .offset(y = offsetBottom)
            .graphicsLayer(),horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.text_title_splash),
                textAlign = TextAlign.Center,
                style = TypographyTitleBold.titleLarge,
                color = colorResource(R.color.primary_Accent)
            )
            Text(
                modifier = Modifier.padding(top = 10.dp),
                color = colorResource(R.color.primary_Accent),
                text = stringResource(R.string.text_sub_title_splash),
                textAlign = TextAlign.Center,
                style = TypographyTitleBold.titleLarge,

            )
        }

        Image(
            painterResource(R.drawable.ic_music_bg),
            contentDescription = "",
            contentScale = ContentScale.Crop
        )
    }
}

