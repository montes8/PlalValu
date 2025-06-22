package com.tayler.playvalu.model

import androidx.compose.ui.text.style.TextAlign
import com.tayler.playvalu.R

class UiTayToolBarModel (
    var uTHeight : Int = 56,
    var uTBgColor : Int = R.color.ui_tay_black,
    var uTTextColor: Int = R.color.ui_tay_white,
    var uTIconStart : Int = R.drawable.uic_tay_ic_back,
    var uTIconEnd : Int = R.drawable.uic_tay_ic_menu,
    var uTTextSize : Int = 16,
    var uTTextMarginHorizontal : Int = 0,
    var uTIconMarginStar : Int = 0,
    var uTIconMarginEnd : Int = 0,
    var uTTextFont : Int = R.font.flex_bold_italic,
    var uTTextPosition : TextAlign = TextAlign.Center,
    var uTTypeStart : Boolean = false,
    var uTTypeEnd : Boolean = false
)