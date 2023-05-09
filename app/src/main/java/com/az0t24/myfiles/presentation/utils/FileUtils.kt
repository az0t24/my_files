package com.az0t24.myfiles.presentation.utils

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.az0t24.myfiles.R
import java.io.File
import java.text.SimpleDateFormat
import kotlin.math.roundToInt

@Composable
fun getIcon(file: File): Painter {
    return when (file.extension) {
        stringResource(R.string.jpg) -> painterResource(R.drawable.icons8_jpg_72___)
        stringResource(R.string.jpeg) -> painterResource(R.drawable.icons8_jpg_72___)
        stringResource(R.string.png) -> painterResource(R.drawable.icons8_png_72___)
        stringResource(R.string.pdf) -> painterResource(R.drawable.icons8_pdf_72___)
        stringResource(R.string.txt) -> painterResource(R.drawable.icons8_txt_72___)
        stringResource(R.string.zip) -> painterResource(R.drawable.icons8_zip_72___)
        stringResource(R.string.apk) -> painterResource(R.drawable.icons8_apk_72___)
        stringResource(R.string.mp3) -> painterResource(R.drawable.icons8_mp3_72___)
        stringResource(R.string.mp4) -> painterResource(R.drawable.icons8_video_file_72___)
        else -> painterResource(R.drawable.icons8_file_72___)
    }
}

fun getSize(file: File): String {
    return when(val size = file.length()) {
        in 0..1_000 -> size.toString() + "B"
        in 1_000..1_000_000 -> ((size.toDouble() / 10).roundToInt() / 100.0).toString() + "K"
        in 1_000_000..1_000_000_000 -> ((size.toDouble() / 10_000).roundToInt() / 100.0).toString() + "M"
        else -> ((size.toDouble() / 10_000_000).roundToInt() / 100.0).toString() + "G"
    }
}

@Composable
@SuppressLint("SimpleDateFormat")
fun getDataModified(file: File): String {
    val sdf = SimpleDateFormat(stringResource(R.string.data_format))
    return sdf.format(file.lastModified()).toString()
}