package com.tayler.playvalu.component

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {
    @Serializable
    object HomeScreen : Screen
    @Serializable
    object SplashScreen : Screen
}


