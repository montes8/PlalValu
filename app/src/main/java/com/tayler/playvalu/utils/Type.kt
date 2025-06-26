package com.tayler.playvalu.utils

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.tayler.playvalu.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)


val flexBoldFont = FontFamily(
    Font(R.font.flex_bold_italic)
)

val gabbiFont = FontFamily(
    Font(R.font.gabi_regular)
)


val TypographyTitleBold = Typography(
    titleLarge = TextStyle(
        fontFamily = gabbiFont,
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.5.sp,
        color = Color.White
    ),
    titleMedium = TextStyle(
        fontFamily = gabbiFont,
        fontWeight = FontWeight.Bold,
        fontSize = 35.sp,
        lineHeight = 35.sp,
        letterSpacing = 0.5.sp,
        color = Color.White
    ),
    titleSmall = TextStyle(
        fontFamily = gabbiFont,
        fontWeight = FontWeight.Bold,
        fontSize = 25.sp,
        lineHeight = 25.sp,
        letterSpacing = 0.5.sp,
        color = Color.White
    ),
    labelLarge = TextStyle(
        fontFamily = gabbiFont,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = Color.White
    ),
    labelMedium = TextStyle(
        fontFamily = gabbiFont,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = Color.White
    ),
    labelSmall = TextStyle(
        fontFamily = gabbiFont,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        color = Color.White
    )
)

val TypographySubTitleGabbi = Typography(
    titleLarge = TextStyle(
        fontFamily = gabbiFont,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        color = Color.White
    ),
    titleMedium = TextStyle(
        fontFamily = gabbiFont,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 22.sp,
        color = Color.White
    ),
    titleSmall = TextStyle(
        fontFamily = gabbiFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        color = Color.White
    ),
    labelLarge = TextStyle(
        fontFamily = gabbiFont,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        color = Color.White
    ),
    labelMedium = TextStyle(
        fontFamily = gabbiFont,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 22.sp,
        color = Color.White
    ),
    labelSmall = TextStyle(
        fontFamily = gabbiFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        color = Color.White
    )
)