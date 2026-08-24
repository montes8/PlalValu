package com.tayler.playvalu.ui.splash

import android.os.Handler
import android.os.Looper
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tayler.playvalu.BuildConfig
import com.tayler.playvalu.R
import com.tayler.playvalu.ui.AppViewModel
import com.valu.uitaycompose.utils.UI_EMPTY
import com.valu.uitaycompose.utils.tay_pink_400
import com.valu.uitaycompose.utils.textM10
import com.valu.uitaycompose.utils.textPenny25
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ScreenSplash(viewModel: AppViewModel,onNavigateToMain: () -> Unit) {

    var animLotti by remember { mutableStateOf(false) }
    var animText by remember { mutableStateOf(false) }
    val offset by animateDpAsState(
        targetValue = if (animLotti) 0.dp else (-500).dp,
        animationSpec = tween(
            durationMillis = 1000,
            easing = LinearEasing
        ),
        label = "Animation top"
    )

    val offsetBottom by animateDpAsState(
        targetValue = if (animText) (-30).dp else (500).dp,
        animationSpec = tween(
            durationMillis = 1000,
            easing = LinearEasing
        ),
        label = "Animation bottom"
    )

    Handler(Looper.getMainLooper()).postDelayed({
        animText = true
        animLotti = true },100)


    viewModel.loadValidateLogin()
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            if (event is InitUiEvent.NavigateToNext) {
                onNavigateToMain.invoke()
            }
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
        Text(
            modifier = Modifier
                .fillMaxWidth().padding(end = 16.dp),
            color = Color.Black,
            text = BuildConfig.VERSION_NAME,
            textAlign = TextAlign.End,
            style = textM10,

            )
        Image(
            painterResource(R.drawable.ic_music_bg),
            contentDescription = "",
            contentScale = ContentScale.Crop
        )

        Box( modifier = Modifier
            .offset(y = offset)
            .graphicsLayer()) {
            Image(
                painter = painterResource(R.drawable.ui_icon_play),
                modifier = Modifier
                    .size(100.dp)
                    .testTag("splash_bag_image"),
                contentDescription = "Logo de la bolsa"
            )
        }

        Column(modifier = Modifier
            .offset(y = offsetBottom)
            .graphicsLayer(),horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                modifier = Modifier.padding(top = 25.dp),
                text = stringResource(R.string.text_title_splash),
                textAlign = TextAlign.Center,
                style = textPenny25,
                color = tay_pink_400
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                color =tay_pink_400,
                text = stringResource(R.string.text_sub_title_splash),
                textAlign = TextAlign.Center,
                style = textPenny25,

            )
        }

        Image(
            painterResource(R.drawable.ic_music_bg),
            contentDescription = UI_EMPTY,
            contentScale = ContentScale.Crop
        )
    }
}

