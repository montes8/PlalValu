package com.tayler.playvalu.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.tayler.playvalu.component.Navigation
import com.tayler.playvalu.utils.PlayValuTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlayValuTheme {
                    Navigation()
            }
        }
    }
}