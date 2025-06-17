package com.tayler.playvalu.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class MusicModel (
    val id : Int= 0,
    var name : String = "",
    var path :String = "",
    var play : Boolean = false
):Parcelable