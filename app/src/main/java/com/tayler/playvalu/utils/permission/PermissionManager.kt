package com.tayler.playvalu.utils.permission

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import java.util.Locale


object PermissionManager {

    fun checkOverlayPermission(context: Context,onClick: () -> Unit) {
        if (!Settings.canDrawOverlays(context)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                ("package:" + context.packageName).toUri()
            )
            context.startActivity(intent)
        } else {
            onClick.invoke()
        }
    }


    @Composable
    fun CheckFilePermission(context: Context,onClick: (Boolean) -> Unit){
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { isGranted ->
                if (
                    isGranted[Manifest.permission.READ_EXTERNAL_STORAGE] == true

                ) {
                    onClick.invoke(true)
                }else{
                    onClick.invoke(false)
                }
            }
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { // if android 11+ request MANAGER_EXTERNAL_STORAGE
            if (!Environment.isExternalStorageManager()) { // check if we already have permission
                val uri = String.format(
                    Locale.ENGLISH,
                    "package:%s",
                    context.packageName
                ).toUri()
                context.startActivity(
                    Intent(
                        Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                        uri
                    )
                )
            } else {
                onClick.invoke(true)

            }
        } else {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
            ) {
                SideEffect {
                    launcher.launch(
                        arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                    )
                }
            } else {
                onClick.invoke(true)
            }
        }
    }
}