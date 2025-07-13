package com.tayler.playvalu.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.tayler.playvalu.R
import com.tayler.playvalu.model.UiTayToolBarModel

@Composable
fun UiTayCToolBar(uiTayText : String = "",
                  uiTayModifier : UiTayToolBarModel = UiTayToolBarModel(),
                  uiTayClick: (Boolean) -> Unit
) {
    Box (modifier = Modifier.height(uiTayModifier.uTHeight.dp).background(colorResource(uiTayModifier.uTBgColor))
        ,contentAlignment = Alignment.CenterStart
    ){
        if (uiTayModifier.uTTypeStart){
            IconButton(onClick = {
                uiTayClick(true)
            },modifier = Modifier
                .align(Alignment.CenterStart).padding(start = uiTayModifier.uTIconMarginStar.dp)) {
                Icon(painter = painterResource(id = uiTayModifier.uTIconStart),
                    tint = Color.Unspecified,contentDescription = "uiTayBackIcon"
                )
            }
        }
        Text(text = uiTayText,modifier = Modifier.fillMaxWidth().padding(
            start = uiTayModifier.uTTextMarginHorizontal.dp,
            end = uiTayModifier.uTTextMarginHorizontal.dp), color = colorResource(uiTayModifier.uTTextColor),
            textAlign = uiTayModifier.uTTextPosition,
            fontSize = uiTayModifier.uTTextSize.sp,
            fontFamily = FontFamily(Font(uiTayModifier.uTTextFont))
        )
        if (uiTayModifier.uTTypeEnd){
            IconButton(onClick = {
                uiTayClick(false)
            }, modifier = Modifier
                .align(Alignment.CenterEnd).padding(start = uiTayModifier.uTIconMarginEnd.dp)) {
                Icon( painter = painterResource(id = uiTayModifier.uTIconEnd),
                    tint = Color.Unspecified,
                    contentDescription =
                    "uiTayMenuIcon")
            }
        }

    }
}

@Composable
fun AnimatedLotti(modifier: Modifier = Modifier) {
    val preloaderLottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            R.raw.music
        )
    )

    val preloaderProgress by animateLottieCompositionAsState(
        preloaderLottieComposition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true
    )


    LottieAnimation(
        composition = preloaderLottieComposition,
        progress = preloaderProgress,
        modifier = modifier
    )
}