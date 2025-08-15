package com.tayler.playvalu.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Scaffold
import androidx.compose.ui.res.stringResource
import com.gb.vale.uivalulibrary.manager.permission.UiTayPermissionManager
import com.gb.vale.uivalulibrary.utils.uiTayHandler
import com.gb.vale.uivalulibrary.utils.uiTayShowToast
import com.tayler.playvalu.R
import com.tayler.playvalu.component.MediaPlayerSingleton
import com.tayler.playvalu.component.Navigation
import com.tayler.playvalu.component.UiTayCToolBar
import com.tayler.playvalu.model.UiTayToolBarModel
import com.tayler.playvalu.ui.service.MusicService
import com.tayler.playvalu.utils.PlayValuTheme
import com.tayler.playvalu.utils.permission.PermissionManager
import com.tayler.playvalu.utils.permission.PermissionManager.CheckFilePermissionActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val permissionVale : UiTayPermissionManager = UiTayPermissionManager(this, onDeny = {
        uiTayShowToast("Necesitas el permiso para acceder a tu musica")
    })
    private var onResume = false
    private val viewModel: AppViewModel by viewModels()

    @SuppressLint("ImplicitSamInstance")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stopService(Intent(this, MusicService::class.java))
        configInit()
    }

    private fun configInit(resume : Boolean = false){
        CheckFilePermissionActivity(this,resume){
            if (it == 0){
                permissionSuccess()
            }else {
                uiTayHandler {
                    onResume = true
                }
                if(it == 2){
                    validatePermission()
                }
            }
        }
    }
    private fun validatePermission(){
        permissionVale.requestPermissions(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ){
            permissionSuccess()
        }
    }

    override fun onResume() {
        super.onResume()
        if(onResume){
            configInit(false)
            onResume = false
        }
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

}




