package com.tayler.playvalu.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.material3.Scaffold
import androidx.compose.ui.res.stringResource
import com.gb.vale.uivalulibrary.manager.permission.UiTayPermissionManager
import com.gb.vale.uivalulibrary.utils.uiTayShowToast
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.tayler.playvalu.R
import com.tayler.playvalu.component.MediaPlayerSingleton
import com.tayler.playvalu.component.Navigation
import com.tayler.playvalu.component.UiTayCToolBar
import com.tayler.playvalu.model.UiTayToolBarModel
import com.tayler.playvalu.ui.service.MusicService
import com.tayler.playvalu.utils.PlayValuTheme
import com.tayler.playvalu.utils.permission.PermissionManager
import com.tayler.playvalu.utils.permission.PermissionManager.checkFilePermissionActivity
import com.tayler.playvalu.utils.validateApiAndroidR
import com.tayler.playvalu.utils.validateApiAndroidRP
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val updateOptions = AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
    private val viewModel: AppViewModel by viewModels()
    val permissionVale : UiTayPermissionManager = UiTayPermissionManager(this, onDeny = {
        uiTayShowToast("Necesitas el permiso para acceder a tu musica")
    })
    companion object {
        private const val UPDATE_CODE = 10001
    }

    @SuppressLint("ImplicitSamInstance")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stopService(Intent(this, MusicService::class.java))
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun configInit(){
        checkFilePermissionActivity(this){
            if (it){
                permissionSuccess()
            }else {
                validatePermission()
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun validatePermission(){
        val permissions = if(validateApiAndroidRP())
            Manifest.permission.READ_MEDIA_AUDIO
         else
            Manifest.permission.READ_EXTERNAL_STORAGE

        permissionVale.requestPermission(permissions
        ){
            permissionSuccess()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onResume() {
        super.onResume()
        validateVersionUpdate()
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    private fun permissionSuccess(){
        setContent {
            PlayValuTheme {
                Scaffold(topBar = {
                    if(viewModel.visibleToolbar){
                        UiTayCToolBar(uiTayText = stringResource(R.string.tb_title_home), uiTayModifier = UiTayToolBarModel(
                            uTTypeEnd = true
                        )) {
                            MediaPlayerSingleton.positionMusic =  viewModel.uiStatePosition
                            MediaPlayerSingleton.positionDurationMusic = MediaPlayerSingleton.playCurrentPosition()
                            PermissionManager.checkOverlayPermission(this) {
                                startService(Intent(this, MusicService::class.java))
                                MediaPlayerSingleton.playStop()
                                finish()
                            }
                        }
                    }
                }, content = { paddingValues ->
                        Navigation(viewModel,paddingValues)
                })

            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun validateVersionUpdate() {
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
                configInit()
            }
        }
        appUpdateInfoTask.addOnFailureListener {
            configInit()
        }
    }

}




