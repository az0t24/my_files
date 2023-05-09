package com.az0t24.myfiles.presentation.mainscreen

import androidx.annotation.StringRes
import java.io.File

data class MainScreenState(
    val files: List<File>,
    val currentDirectory: File,
    @StringRes val sortedBy: Int,
    val sortOrder: Boolean,
    val updatedFiles: List<File>,
    val isUpdatedShow: Boolean,
    val isPermissionDialogShow: Boolean,
)
