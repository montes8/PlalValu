package com.tayler.playvalu.component

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tayler.playvalu.ui.AppViewModel
import com.tayler.playvalu.ui.home.ScreenHome
import com.tayler.playvalu.ui.splash.ScreenSplash

@Composable
fun Navigation(viewModel: AppViewModel,paddingValues: PaddingValues) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.SplashScreen,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        composable<Screen.SplashScreen> {
            ScreenSplash(viewModel) {
                navController.navigate(Screen.HomeScreen) {
                    popUpTo(Screen.SplashScreen) { inclusive = true }
                }
            }
        }

        composable<Screen.HomeScreen> {
            ScreenHome(viewModel,paddingValues)
        }

    }

}