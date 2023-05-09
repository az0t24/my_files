package com.az0t24.myfiles.app

import android.content.Context
import android.content.Intent
import android.webkit.MimeTypeMap
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File

class FileHelper(
    private val context: Context,
) {
    fun openFile(file: File) {
        val uri = FileProvider.getUriForFile(context, "com.az0t24.app.provider", file)
        val mime = getMimeType(uri.toString())

        val intent = Intent(Intent.ACTION_VIEW).apply {
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
            setDataAndType(uri, mime)
        }

        ContextCompat.startActivity(context, intent, null)
    }

    fun shareFile(file: File) {
        val uri = FileProvider.getUriForFile(context, "com.az0t24.app.provider", file)
        val mime = getMimeType(uri.toString())

        val intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_STREAM, uri)
            type = mime
        }

        val activity = Intent.createChooser(intent, null).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        ContextCompat.startActivity(context, activity, null)
    }

    private fun getMimeType(url: String): String {
        val ext = MimeTypeMap.getFileExtensionFromUrl(url)
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext) ?: "text/plain"
    }
}