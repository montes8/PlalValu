package com.tayler.playvalu.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Scaffold
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.gb.vale.uivalulibrary.manager.permission.UiTayPermissionManager
import com.gb.vale.uivalulibrary.utils.uiTayShowToast
import com.tayler.playvalu.R
import com.tayler.playvalu.component.MediaPlayerSingleton
import com.tayler.playvalu.component.Navigation
import com.tayler.playvalu.component.UiTayCToolBar
import com.tayler.playvalu.model.UiTayToolBarModel
import com.tayler.playvalu.ui.service.MusicService
import com.tayler.playvalu.utils.PlayValuTheme
import com.tayler.playvalu.utils.permission.PermissionManager
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var onResume = false
    private val viewModel: AppViewModel by viewModels()
    private val permissionVale : UiTayPermissionManager = UiTayPermissionManager(this, onDeny = {
        uiTayShowToast("Necesitas el permiso para acceder a tu musica")
    })

    @SuppressLint("ImplicitSamInstance")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stopService(Intent(this, MusicService::class.java))
        configInit()
    }

    private fun configInit(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                onResume = true
                val uri = String.format(
                    Locale.ENGLISH,
                    "package:%s",
                    this.packageName
                ).toUri()
                this.startActivity(
                    Intent(
                        Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                        uri
                    )
                )
            } else {
                permissionSuccess()

            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
            ) {
                onResume = true
                permissionVale.requestPermissions(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                ){
                    permissionSuccess()
                }

            } else {
                permissionSuccess()
            }
        }
    }


    override fun onResume() {
        super.onResume()
        if(onResume){
            configInit()
            onResume = false
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    private fun permissionSuccess(){
        setContent {
            PlayValuTheme {
                Scaffold(topBar = {
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
                }, content = { paddingValues ->
                    Navigation(viewModel,paddingValues)
                })

            }
        }
    }

}




