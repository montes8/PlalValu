package com.tayler.playvalu.component

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tayler.playvalu.ui.AppViewModel
import com.tayler.playvalu.ui.home.ScreenHome
import com.tayler.playvalu.ui.search.ScreenSearch
import com.tayler.playvalu.ui.splash.ScreenSplash

@Composable
fun Navigation(
    navController: NavHostController,
    viewModel: AppViewModel,
    paddingValues: PaddingValues
) {

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
            ScreenHome(viewModel, paddingValues)
        }

        composable<Screen.SearchScreen> {
            ScreenSearch(viewModel, paddingValues)
        }

    }

}
