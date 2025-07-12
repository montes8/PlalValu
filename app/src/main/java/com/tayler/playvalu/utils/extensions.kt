package com.tayler.playvalu.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import java.util.concurrent.TimeUnit

fun Context.getActivityOrNull(): Activity? {
    var context = this
    if (context is Activity) return context
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }

    return null
}


@SuppressLint("DefaultLocale")
fun formatTimePlayer(time :Int): String{
    val timeFinal = java.lang.String.format("%02d:%02d ", TimeUnit.MILLISECONDS.toMinutes(time.toLong()),
        TimeUnit.MILLISECONDS.toSeconds(time.toLong()) - TimeUnit.MINUTES.toSeconds(
            TimeUnit.MILLISECONDS.toMinutes(
                time.toLong()))
    )
    return timeFinal

}