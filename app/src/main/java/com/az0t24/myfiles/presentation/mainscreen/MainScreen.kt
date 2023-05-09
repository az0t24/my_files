package com.az0t24.myfiles.presentation.mainscreen

import android.os.Environment.getExternalStorageDirectory
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.az0t24.myfiles.R
import com.az0t24.myfiles.presentation.utils.getDataModified
import com.az0t24.myfiles.presentation.utils.getIcon
import com.az0t24.myfiles.presentation.utils.getSize
import java.io.File

@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState().value

    when(uiState.isPermissionDialogShow) {
        true -> PermissionDialog(viewModel)
        false -> ProviderScreen(viewModel)
    }
}

@Composable
private fun PermissionDialog(
    viewModel: MainScreenViewModel,
) {
    Dialog(
        onDismissRequest = { viewModel.onTriggerEvent(MainScreenEvent.OnDialogDismiss) },
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(size = 10.dp),
            color = MaterialTheme.colors.onPrimary
        ) {
            Column {
                Text(
                    text = stringResource(id = R.string.permissions_request),
                    modifier = Modifier
                        .padding(10.dp)
                        .size(300.dp, 100.dp),
                    color = MaterialTheme.colors.onBackground,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(30.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { viewModel.onTriggerEvent(MainScreenEvent.OnDialogDismiss) },
                        modifier = Modifier
                            .size(140.dp, 45.dp),
                        shape = RoundedCornerShape(30),
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
                    ) {
                        Text(
                            text = stringResource(id = R.string.dismiss_preferences),
                            color = MaterialTheme.colors.onPrimary,
                        )
                    }
                    Button(
                        onClick = { viewModel.onTriggerEvent(MainScreenEvent.OnDialogAccept) },
                        modifier = Modifier
                            .size(140.dp, 45.dp),
                        shape = RoundedCornerShape(30),
                    ) {
                        Text(
                            text = stringResource(id = R.string.accept_preferences),
                            color = MaterialTheme.colors.onPrimary,
                        )
                    }
                }

                Spacer(modifier = Modifier.padding(16.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ProviderScreen(
    viewModel: MainScreenViewModel,
) {
    val uiState = viewModel.uiState.collectAsState().value

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        TopAppBar(
            elevation = 4.dp,
            title = {
                Text(
                    if (uiState.currentDirectory != getExternalStorageDirectory()) {
                        uiState.currentDirectory.name
                    } else {
                        stringResource(R.string.app_name)
                    }
                )
            },
            backgroundColor = MaterialTheme.colors.primarySurface,
            navigationIcon = {
                if (uiState.currentDirectory != getExternalStorageDirectory()) {
                    IconButton(onClick = {
                        viewModel.onTriggerEvent(
                            MainScreenEvent.OnDirectoryClicked(
                                uiState.currentDirectory.parentFile
                            )
                        )
                    }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            },
            actions = {
                IconButton(onClick = { viewModel.onTriggerEvent(MainScreenEvent.ShowUpdated) }) {
                    Icon(
                        if (uiState.isUpdatedShow) {
                            Icons.Filled.VisibilityOff
                        } else {
                            Icons.Filled.Visibility
                        },
                        contentDescription = stringResource(R.string.show_updated),
                    )
                }
            })

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            val sortParam = stringResource(R.string.file_extension)
            Card(
                onClick = {
                    viewModel.onTriggerEvent(
                        MainScreenEvent.OnSort(sortParam)
                    )
                },
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.height(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Sort,
                        contentDescription = "${stringResource(R.string.sort_by)} " +
                                stringResource(R.string.file_extension),
                        tint = if (viewModel.uiState.collectAsState().value.sortedBy
                            == R.string.file_extension
                        ) {
                            Color.Black
                        } else {
                            Color.Transparent
                        },
                        modifier = Modifier.fillMaxHeight()
                    )
                }
            }
            HeaderCard(fieldName = stringResource(R.string.name), viewModel = viewModel)

            Spacer(modifier = Modifier.padding(start = 68.dp))
            HeaderCard(fieldName = stringResource(R.string.size), viewModel = viewModel)

            Spacer(modifier = Modifier.padding(start = 52.dp))
            HeaderCard(fieldName = stringResource(R.string.last_modified), viewModel = viewModel)
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(4.dp)
        ) {
            items(
                if (!uiState.isUpdatedShow) {
                    uiState.files
                } else {
                    uiState.updatedFiles
                }
            ) { file ->
                if (file.isFile) {
                    FileCard(file, viewModel)
                } else {
                    DirectoryCard(file, viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun HeaderCard(
    fieldName: String,
    viewModel: MainScreenViewModel,
) {
    val uiState = viewModel.uiState.collectAsState().value
    val isOrderedBy = stringResource(uiState.sortedBy) == fieldName
    Card(
        onClick = { viewModel.onTriggerEvent(MainScreenEvent.OnSort(fieldName)) },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.height(40.dp)
        ) {
            AnimatedVisibility(visible = isOrderedBy) {
                if (isOrderedBy) {
                    Row {
                        Icon(
                            imageVector = Icons.Filled.Sort,
                            contentDescription = "${stringResource(R.string.last_modified)} $fieldName",
                            modifier = Modifier.fillMaxHeight()
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    }
                }
            }
            Text(
                text = fieldName,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FileCard(
    file: File,
    viewModel: MainScreenViewModel,
) {
    Card(
        modifier = Modifier.fillMaxWidth().combinedClickable(
            onClick = { viewModel.onTriggerEvent(MainScreenEvent.OnFileClicked(file)) },
            onLongClick = { viewModel.onTriggerEvent(MainScreenEvent.OnFileLongClicked(file)) },
        )
    ) {
        Row {
            Icon(
                painter = getIcon(file),
                tint = colorResource(R.color.blue),
                contentDescription = "File ${file.name}"
            )
            Text(
                text = file.name.padEnd(40),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.size(width = 156.dp, height = 20.dp).padding(start = 4.dp)
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = getSize(file),
                modifier = Modifier.size(width = 64.dp, height = 20.dp),
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = getDataModified(file),
                textAlign = TextAlign.End
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun DirectoryCard(
    file: File,
    viewModel: MainScreenViewModel,
) {
    Card(
        modifier = Modifier.fillMaxSize(),
        onClick = { viewModel.onTriggerEvent(MainScreenEvent.OnDirectoryClicked(file)) }
    ) {
        Row(modifier = Modifier.fillMaxHeight()) {
            Icon(
                imageVector = Icons.Filled.Folder,
                tint = colorResource(R.color.orange),
                contentDescription = "Directory ${file.name}"
            )
            Text(
                text = file.name.padEnd(40),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.size(width = 156.dp, height = 20.dp).padding(start = 4.dp)
            )
            Spacer(modifier = Modifier.padding(start = 60.dp))
            Text(
                text = getDataModified(file),
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
@Preview
private fun MainPreview() {
    MaterialTheme {
        MainScreen()
    }
}