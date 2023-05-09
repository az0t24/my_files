package com.az0t24.myfiles.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        FileHashEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class MyFilesDatabase : RoomDatabase() {
    abstract fun fileHashDao(): FileHashDao
}