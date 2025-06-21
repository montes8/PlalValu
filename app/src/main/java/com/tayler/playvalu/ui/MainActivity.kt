package com.tayler.playvalu.ui

import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.tayler.playvalu.component.Navigation
import com.tayler.playvalu.utils.PlayValuTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val q = getMusic(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC))
        Log.d("listamuca","$q")
        setContent {
            PlayValuTheme {
                    Navigation()
            }
        }
    }
}




