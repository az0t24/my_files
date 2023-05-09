package com.az0t24.myfiles.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "file_hashes")
data class FileHashEntity(
    @PrimaryKey
    @ColumnInfo(name = "name")
    val absoluteName: String,
    @ColumnInfo(name = "directory")
    val absoluteDirectory: String,
    @ColumnInfo(name = "hash_code")
    val hashCode: Int,
) : Parcelable