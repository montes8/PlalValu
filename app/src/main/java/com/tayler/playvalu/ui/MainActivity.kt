package com.tayler.playvalu.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.tayler.playvalu.R
import com.tayler.playvalu.component.MediaPlayerSingleton
import com.tayler.playvalu.component.Navigation
import com.tayler.playvalu.component.Screen
import com.tayler.playvalu.ui.service.MusicService
import com.tayler.playvalu.utils.PlayValuTheme
import com.tayler.playvalu.utils.permission.PermissionManager
import com.tayler.playvalu.utils.permission.PermissionManager.checkFilePermissionActivity
import com.tayler.playvalu.utils.validateApiAndroidRP
import com.valu.uitaycompose.extra.UiTayCToolBar
import com.valu.uitaycompose.model.UiToolBarModel
import com.valu.uitaycompose.utils.extension.uiTayShowToast
import com.valu.uitaycompose.utils.permission.UiTayPermissionController
import com.valu.uitaycompose.utils.permission.rememberUiTayPermissionManager
import com.valu.uitaycompose.utils.tay_pink_200
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val updateOptions = AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
    private val viewModel: AppViewModel by viewModels()

    companion object {
        private const val UPDATE_CODE = 10001
    }

    @SuppressLint("ImplicitSamInstance")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stopService(Intent(this, MusicService::class.java))

        setContent {
            val navController = rememberNavController()
            val permissionVale = rememberUiTayPermissionManager(onDeny = {
                uiTayShowToast(R.string.text_error_permission)
            })

            var hasPermission by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                validateVersionUpdate(permissionVale) {
                    hasPermission = true
                }
            }

            PlayValuTheme {
                Scaffold(topBar = {
                    if (viewModel.visibleToolbar) {
                        UiTayCToolBar(
                            uiTayText = stringResource(R.string.text_sub_title_splash),
                            uiTayModifier = UiToolBarModel()
                                .backgroundColor(tay_pink_200)
                                .textColor(Color.White)
                                .iconEnd(R.drawable.ui_ic_minimimize)
                                .useOriginalTint(true)
                                .showStartIcon(false)
                                .showEndIcon(true)
                                .setIconSize(24.dp)
                        ) {value ->
                            if (value){
                               navController.navigate(Screen.SearchScreen)
                            }else{
                                MediaPlayerSingleton.positionMusic = viewModel.uiStatePosition
                                MediaPlayerSingleton.positionDurationMusic =
                                    MediaPlayerSingleton.playCurrentPosition()
                                PermissionManager.checkOverlayPermission(this@MainActivity) {
                                    startService(Intent(this@MainActivity, MusicService::class.java))
                                    MediaPlayerSingleton.playStop()
                                    finish()
                                }
                            }

                        }
                    }
                }, content = { paddingValues ->
                    Navigation(navController, viewModel, paddingValues)
                })
            }
        }
    }

    private fun validateVersionUpdate(
        permissionVale: UiTayPermissionController,
        onPermissionSuccess: () -> Unit
    ) {
        val appUpdateManager = AppUpdateManagerFactory.create(this)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(
                    AppUpdateType.IMMEDIATE
                )
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    this,
                    updateOptions,
                    UPDATE_CODE
                )
                finish()
            } else {
                configInit(permissionVale, onPermissionSuccess)
            }
        }
        appUpdateInfoTask.addOnSuccessListener {
             // Redundant in this context but handling success
        }
        appUpdateInfoTask.addOnFailureListener {
            configInit(permissionVale, onPermissionSuccess)
        }
    }

    private fun configInit(
        permissionVale: UiTayPermissionController,
        onPermissionSuccess: () -> Unit
    ) {
        checkFilePermissionActivity(this) { granted ->
            if (granted) {
                onPermissionSuccess()
            } else {
                validatePermission(permissionVale, onPermissionSuccess)
            }
        }
    }

    private fun validatePermission(
        permissionVale: UiTayPermissionController,
        onPermissionSuccess: () -> Unit
    ) {
        val permissions = if (validateApiAndroidRP())
            Manifest.permission.READ_MEDIA_AUDIO
        else
            Manifest.permission.READ_EXTERNAL_STORAGE

        permissionVale.requestPermission(permissions) {
            onPermissionSuccess()
        }
    }
}
