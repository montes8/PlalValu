package com.tayler.playvalu.component

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tayler.playvalu.ui.home.ScreenHome
import com.tayler.playvalu.ui.splash.ScreenSplash

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.SplashScreen.route,
        route = ROOT_GRAPH_ROUTE) {

        composable(route = Screen.SplashScreen.route) {
            BackHandler(true) {
                // Or do nothing
            }
            ScreenSplash(hiltViewModel(), navController = navController)
        }

        composable(route = Screen.HomeScreen.route) {
            BackHandler(true) {
                // Or do nothing
            }
            ScreenHome(hiltViewModel())
        }
    }

}