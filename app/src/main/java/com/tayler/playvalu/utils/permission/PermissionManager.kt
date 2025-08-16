package com.tayler.playvalu.utils.permission

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.tayler.playvalu.utils.validateApiAndroidR


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

    fun checkFilePermissionActivity(context: ComponentActivity,onClick: (Boolean) -> Unit){
        if (validateApiAndroidR()) {
            if (ContextCompat.checkSelfPermission(
                    context, Manifest.permission.READ_MEDIA_AUDIO
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                onClick.invoke(true)
            } else {
                onClick.invoke(false)
            }
        } else {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
            ) {
                onClick.invoke(false)

            } else {
                onClick.invoke(true)
            }
        }
    }
}