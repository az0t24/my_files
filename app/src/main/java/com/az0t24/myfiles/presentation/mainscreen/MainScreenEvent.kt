package com.az0t24.myfiles.presentation.mainscreen

import java.io.File

sealed class MainScreenEvent {
    data class OnDirectoryClicked(val dir: File) : MainScreenEvent()
    data class OnFileClicked(val file: File) : MainScreenEvent()
    data class OnSort(val fieldName: String) : MainScreenEvent()
    data class OnFileLongClicked(val file: File) : MainScreenEvent()
    object ShowUpdated : MainScreenEvent()
    object OnDialogDismiss : MainScreenEvent()
    object OnDialogAccept : MainScreenEvent()
}