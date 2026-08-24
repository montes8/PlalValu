package com.tayler.playvalu.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.valu.uitaycompose.utils.tay_pink_400

private val LightColorScheme = lightColorScheme(
    primary = tay_pink_400,
    secondary = tay_pink_400,
    tertiary = tay_pink_400,
    surface = Color.White,
    background = Color.White,
    onSurface = Color.Black,
    onBackground = Color.Black
)

@Composable
fun PlayValuTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
      colorScheme = LightColorScheme,
      typography = Typography,
      content = content
    )
}

val Typography = androidx.compose.material3.Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)