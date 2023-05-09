package com.az0t24.myfiles.presentation.mainscreen

import android.os.Environment.getExternalStorageDirectory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.az0t24.myfiles.R
import com.az0t24.myfiles.app.FileHelper
import com.az0t24.myfiles.app.permissionsHelper
import com.az0t24.myfiles.data.FileHashEntity
import com.az0t24.myfiles.data.FileHashRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val repository: FileHashRepository,
    private val fileHelper: FileHelper,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainScreenState(
        files = emptyList(),
        currentDirectory = getExternalStorageDirectory(),
        sortedBy = R.string.name,
        sortOrder = false,
        updatedFiles = emptyList(),
        isUpdatedShow = false,
        isPermissionDialogShow = false,
    ))
    val uiState = _uiState.asStateFlow()

    fun onTriggerEvent(eventType: MainScreenEvent) {
        when(eventType) {
            is MainScreenEvent.OnDirectoryClicked -> openDirectory(eventType.dir)
            is MainScreenEvent.OnFileClicked -> openFile(eventType.file)
            is MainScreenEvent.OnSort -> onSort(eventType.fieldName)
            is MainScreenEvent.ShowUpdated -> changeListToShow()
            is MainScreenEvent.OnFileLongClicked -> shareFile(eventType.file)
            is MainScreenEvent.OnDialogAccept -> openSettings()
            is MainScreenEvent.OnDialogDismiss -> closeDialog()
        }
    }

    init {
        if (!permissionsHelper.allPermissionsGranted()) {
            _uiState.update {
                it.copy(isPermissionDialogShow = true)
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            fillData()
            updateData()
        }
    }

    private fun openSettings() {
        permissionsHelper.openAppSettings()
        _uiState.update {
            it.copy(isPermissionDialogShow = false)
        }
    }

    private fun closeDialog() {
        _uiState.update {
            it.copy(isPermissionDialogShow = false)
        }
    }

    private fun fillData() {
        val dir = uiState.value.currentDirectory
        val list = getListFiles(dir)
        _uiState.update {
            it.copy(files = list.sorted())
        }
    }

    private suspend fun updateData() {
        inspectDirectory(getExternalStorageDirectory())
    }

    private suspend fun inspectDirectory(directory: File) {
        val files = getListFiles(directory)
        val storedFiles = repository.getHashes(directory.absolutePath)
        val updatedFiles = files.filter { file ->
            FileHashEntity(
                file.absolutePath,
                file.parentFile?.absolutePath ?: "",
                file.hashCode()
            ) !in storedFiles
        } + _uiState.value.updatedFiles

        _uiState.update { currentState ->
            currentState.copy(updatedFiles = updatedFiles)
        }

        uploadData(directory, files)

        for (file in files) {
            if (file.isDirectory) {
                inspectDirectory(file)
            }
        }
    }

    private suspend fun uploadData(directory: File, files: List<File>) {
        repository.clearHashes(directory.absolutePath)
        for (file in files) {
            repository.insertHash(
                FileHashEntity(
                    file.absolutePath,
                    file.parentFile?.absolutePath ?: "",
                    file.hashCode()
                )
            )
        }
    }

    private fun getListFiles(dir: File) =
        dir.listFiles()?.toList() ?: listOf()

    private fun openDirectory(dir: File) {
        _uiState.update { currentState ->
            currentState.copy(
                files = getListFiles(dir).sorted(),
                currentDirectory = dir,
                sortedBy = R.string.name,
                sortOrder = false,
                isUpdatedShow = false,
            )
        }
    }

    private fun openFile(file: File) {
        fileHelper.openFile(file)
    }

    private fun shareFile(file: File) {
        fileHelper.shareFile(file)
    }

    private fun onSort(fieldName: String) {
        when(fieldName) {
            "Name" -> sortByName()
            "Size" -> sortBySize()
            "Last modified" -> sortByLastModified()
            "File extension" -> sortByFileExtension()
        }
    }

    private fun sortByName() {
        _uiState.update { currentState ->
            val sortOrder =  if (currentState.sortedBy == R.string.name) {
                currentState.sortOrder.not()
            } else {
                false
            }
            currentState.copy(
                files = if (sortOrder) {
                    currentState.files.sortedBy { it.name }
                } else {
                    currentState.files.sortedBy { it.name }.reversed()
                },
                updatedFiles = if (sortOrder) {
                    currentState.updatedFiles.sortedBy { it.name }
                } else {
                    currentState.updatedFiles.sortedBy { it.name }.reversed()
                },
                sortedBy = R.string.name,
                sortOrder = sortOrder,
            )
        }
    }

    private fun sortBySize() {
        _uiState.update { currentState ->
            val sortOrder =  if (currentState.sortedBy == R.string.size) {
                currentState.sortOrder.not()
            } else {
                false
            }
            currentState.copy(
                files = if (sortOrder) {
                    currentState.files.sortedBy { it.length() }
                } else {
                    currentState.files.sortedBy { it.length() }.reversed()
                },
                updatedFiles = if (sortOrder) {
                    currentState.updatedFiles.sortedBy { it.length() }
                } else {
                    currentState.updatedFiles.sortedBy { it.length() }.reversed()
                },
                sortedBy = R.string.size,
                sortOrder = sortOrder,
            )
        }
    }

    private fun sortByLastModified() {
        _uiState.update { currentState ->
            val sortOrder =  if (currentState.sortedBy == R.string.last_modified) {
                currentState.sortOrder.not()
            } else {
                false
            }
            currentState.copy(
                files = if (sortOrder) {
                    currentState.files.sortedBy { it.lastModified() }
                } else {
                    currentState.files.sortedBy { it.lastModified() }.reversed()
                },
                updatedFiles = if (sortOrder) {
                    currentState.updatedFiles.sortedBy { it.lastModified() }
                } else {
                    currentState.updatedFiles.sortedBy { it.lastModified() }.reversed()
                },
                sortedBy = R.string.last_modified,
                sortOrder = sortOrder,
            )
        }
    }

    private fun sortByFileExtension() {
        _uiState.update { currentState ->
            val sortOrder =  if (currentState.sortedBy == R.string.file_extension) {
                currentState.sortOrder.not()
            } else {
                false
            }
            currentState.copy(
                files = if (sortOrder) {
                    currentState.files.sortedBy { it.extension }
                } else {
                    currentState.files.sortedBy { it.extension }.reversed()
                },
                updatedFiles = if (sortOrder) {
                    currentState.updatedFiles.sortedBy { it.extension }
                } else {
                    currentState.updatedFiles.sortedBy { it.extension }.reversed()
                },
                sortedBy = R.string.file_extension,
                sortOrder = sortOrder,
            )
        }
    }

    private fun changeListToShow() {
        _uiState.update {currentState ->
            currentState.copy(isUpdatedShow = currentState.isUpdatedShow.not())
        }
    }
}