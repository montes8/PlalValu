package com.tayler.playvalu.utils.permission

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.core.net.toUri


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
}