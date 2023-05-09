package com.az0t24.myfiles.app

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity

class PermissionsHelper(
    private val context: Context,
    val requiredPermissions: Array<String>,
) {
    fun allPermissionsGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager()
        }
        var allPermissionsGranted = true
        requiredPermissions.forEach { permission ->
            if (ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                allPermissionsGranted = false
            }
        }
        return allPermissionsGranted
    }

    fun openAppSettings() {
        val intent = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        } else {
            Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(context, intent, null)
    }
}
