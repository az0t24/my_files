package com.az0t24.myfiles.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FileHashDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg hash: FileHashEntity)

    @Query("SELECT * FROM file_hashes WHERE directory = :absoluteDirectory")
    fun getHashes(absoluteDirectory: String): List<FileHashEntity>

    @Query("DELETE FROM file_hashes WHERE directory =:absoluteDirectory")
    suspend fun clearHashes(absoluteDirectory: String)
}