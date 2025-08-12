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
import androidx.compose.runtime.SideEffect
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.gb.vale.uivalulibrary.manager.permission.UiTayPermissionManager
import com.gb.vale.uivalulibrary.utils.uiTayShowToast
import com.tayler.playvalu.component.Navigation
import com.tayler.playvalu.ui.service.MusicService
import com.tayler.playvalu.utils.PlayValuTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val permissionVale : UiTayPermissionManager = UiTayPermissionManager(this, onDeny = {
        uiTayShowToast("Necesitas el permiso para acceder a tu musica")
    })

    @SuppressLint("ImplicitSamInstance")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stopService(Intent(this, MusicService::class.java))
    }

    private fun configInit(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
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
        configInit()
    }

    private fun permissionSuccess(){
        setContent {
            PlayValuTheme {
                Navigation()
            }
        }
    }

}




