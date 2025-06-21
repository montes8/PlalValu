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


val TypographyBold = Typography(
    titleLarge = TextStyle(
        fontFamily = flexBoldFont,
        fontWeight = FontWeight.Normal,
        fontSize = 40.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        color = Color.White
    ),
    titleMedium = TextStyle(
        fontFamily = flexBoldFont,
        fontWeight = FontWeight.Normal,
        fontSize = 30.sp,
        lineHeight = 30.sp,
        letterSpacing = 0.5.sp,
        color = Color.White
    ),
    titleSmall = TextStyle(
        fontFamily = flexBoldFont,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        color = Color.White
    ),
    labelLarge = TextStyle(
        fontFamily = gabbiFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = Color.White
    ),
    labelMedium = TextStyle(
        fontFamily = gabbiFont,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = Color.White
    ),
    labelSmall = TextStyle(
        fontFamily = gabbiFont,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        color = Color.White
    )

)

val TypographyBoldDark = Typography(
    titleLarge = TextStyle(
        fontFamily = flexBoldFont,
        fontWeight = FontWeight.Normal,
        fontSize = 30.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        color = Color.Black
    ),
    titleMedium = TextStyle(
        fontFamily = flexBoldFont,
        fontWeight = FontWeight.Normal,
        fontSize = 25.sp,
        lineHeight = 30.sp,
        letterSpacing = 0.5.sp,
        color = Color.Black
    ),
    titleSmall = TextStyle(
        fontFamily = flexBoldFont,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        color = Color.Black
    ),
    labelLarge = TextStyle(
        fontFamily = gabbiFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        color = Color.Black
    ),
    labelMedium = TextStyle(
        fontFamily = gabbiFont,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        color = Color.Black
    ),
    labelSmall = TextStyle(
        fontFamily = gabbiFont,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        color = Color.Black
    )
)