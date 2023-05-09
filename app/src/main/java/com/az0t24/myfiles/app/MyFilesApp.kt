package com.az0t24.myfiles.app

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

@SuppressLint("StaticFieldLeak")
lateinit var permissionsHelper: PermissionsHelper

@HiltAndroidApp
class MyFilesApp : Application() {
    override fun onCreate() {
        super.onCreate()

        val requiredPermissions = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        } else {
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE,
            )
        }

        permissionsHelper = PermissionsHelper(applicationContext, requiredPermissions)
    }
}