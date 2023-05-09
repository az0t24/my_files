package com.az0t24.myfiles.data

import javax.inject.Inject

class FileHashRepository
@Inject constructor(
    private val fileHashDao: FileHashDao,
) {
    fun getHashes(absoluteDirectory: String): List<FileHashEntity> = fileHashDao.getHashes(absoluteDirectory)
    suspend fun insertHash(hash: FileHashEntity) = fileHashDao.insert(hash)
    suspend fun clearHashes(absoluteDirectory: String) = fileHashDao.clearHashes(absoluteDirectory)
}