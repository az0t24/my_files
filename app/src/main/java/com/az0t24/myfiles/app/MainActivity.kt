package com.az0t24.myfiles.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import com.az0t24.myfiles.app.theme.MyFilesTheme
import com.az0t24.myfiles.presentation.mainscreen.MainScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!permissionsHelper.allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, permissionsHelper.requiredPermissions, 1)
        }

        setContent {
            MyFilesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}