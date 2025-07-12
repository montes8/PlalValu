package com.tayler.playvalu.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.tayler.playvalu.component.Navigation
import com.tayler.playvalu.ui.service.MusicService
import com.tayler.playvalu.utils.PlayValuTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    companion object {
        fun newInstance(context: Context) = context.startActivity(Intent(context, MainActivity::class.java))
    }

    @SuppressLint("ImplicitSamInstance")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stopService(Intent(this, MusicService::class.java))
        setContent {
            PlayValuTheme {
                    Navigation()
            }
        }
    }
}




