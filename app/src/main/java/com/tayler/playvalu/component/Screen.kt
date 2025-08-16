package com.tayler.playvalu.component

const val ROOT_GRAPH_ROUTE = "root"

sealed class Screen (open val route: String) {
    object HomeScreen : Screen("home_screen")
    object SplashScreen : Screen("splash_screen")
}


