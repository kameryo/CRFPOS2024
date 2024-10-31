package com.example.feature_record

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource

@Composable
fun EditRecordScreen(
    back: () -> Unit,
    viewModel: EditRecordViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()
    EditRecordScreen(
        uiState = uiState,
        back = back,
        showDeleteDialog = {
            viewModel.showDeleteDialog()
        },
        moveToIdle = {
            viewModel.moveToIdle()
        },
        delete = {
            viewModel.delete()
        },
        load = {
            viewModel.load()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditRecordScreen(
    uiState: EditRecordViewModel.UiState,
    back: () -> Unit,
    showDeleteDialog: () -> Unit,
    moveToIdle: () -> Unit,
    delete: () -> Unit,
    load: () -> Unit,
) {
    var menuExpanded by rememberSaveable { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.edit_goods)) },
                navigationIcon = {
                    IconButton(onClick = back) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { menuExpanded = !menuExpanded }) {
                        Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "Menu")
                    }

                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }) {
                        DropdownMenuItem(
                            text = {
                                Text(stringResource(R.string.delete))
                            },
                            onClick = {
                                menuExpanded = false
                                showDeleteDialog()
                            },
                        )
                    }
                }

            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        when (uiState) {
            is EditRecordViewModel.UiState.LoadSuccess,
            EditRecordViewModel.UiState.Loading,
            EditRecordViewModel.UiState.Initial -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                ) {
                    CircularProgressIndicator()
                }
            }

            is EditRecordViewModel.UiState.LoadError -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                ) {
                    Text(uiState.error.toString())
                }
            }


            is EditRecordViewModel.UiState.ConfirmDelete,
            is EditRecordViewModel.UiState.DeleteError,
            EditRecordViewModel.UiState.DeleteSuccess,
            is EditRecordViewModel.UiState.Idle -> {
                EditRecordForm(
                    modifier = Modifier.padding(innerPadding)
                )
                if (uiState is EditRecordViewModel.UiState.ConfirmDelete) {
                    AlertDialog(
                        onDismissRequest = {
                            moveToIdle()
                        },
                        text = {
                            Text(stringResource(R.string.delete_message))
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                delete()
                            }) {
                                Text(stringResource(android.R.string.ok))
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = {
                                moveToIdle()
                            }) {
                                Text(stringResource(android.R.string.cancel))
                            }
                        }
                    )
                }
            }

        }

    }
    LaunchedEffect(uiState) {
        when (uiState) {
            is EditRecordViewModel.UiState.Initial -> {
                load()
            }

            is EditRecordViewModel.UiState.Idle,
            is EditRecordViewModel.UiState.LoadError,
            EditRecordViewModel.UiState.Loading -> {

            }

            is EditRecordViewModel.UiState.LoadSuccess -> {
                moveToIdle()
            }

            is EditRecordViewModel.UiState.ConfirmDelete -> {

            }

            is EditRecordViewModel.UiState.DeleteError -> {
                snackbarHostState.showSnackbar(
                    message = uiState.e.toString()
                )
                moveToIdle()
            }

            EditRecordViewModel.UiState.DeleteSuccess -> {
                back()
            }
        }
    }
}

@Composable
fun EditRecordForm(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text = "Edit Record")
    }
}