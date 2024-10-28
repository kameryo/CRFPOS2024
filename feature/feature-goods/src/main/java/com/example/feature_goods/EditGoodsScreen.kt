package com.example.feature_goods

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun EditGoodsScreen(
    viewModel: EditGoodsViewModel,
    back: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    EditGoodsScreen(
        uiState = uiState,
        load = {
            viewModel.load()
        },
        moveToIdle = {
            viewModel.moveToIdle()
        },
        back = back,
        update = { name, price ->
            viewModel.update(name, price)

        },
        showDeleteDialog = {
            viewModel.showDeleteDialog()
        },
        delete = {
            viewModel.delete()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditGoodsScreen(
    uiState: EditGoodsViewModel.UiState,
    load: () -> Unit,
    moveToIdle: () -> Unit,
    back: () -> Unit,
    update: (String, Long) -> Unit,
    showDeleteDialog: () -> Unit,
    delete: () -> Unit,
) {
    var name by rememberSaveable { mutableStateOf("") }
    var price by rememberSaveable { mutableLongStateOf(0) }

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
                    if (uiState is EditGoodsViewModel.UiState.Idle) {
                        IconButton(onClick = {
                            update(name, price)
                        }) {
                            Icon(imageVector = Icons.Filled.Done, contentDescription = "Save")
                        }

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
                }

            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },

        ) { innerPadding ->
        when (uiState) {
            EditGoodsViewModel.UiState.Initial,
            EditGoodsViewModel.UiState.Loading,
            is EditGoodsViewModel.UiState.LoadSuccess -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                ) {
                    CircularProgressIndicator()
                }
            }

            is EditGoodsViewModel.UiState.LoadError -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                ) {
                    Text(uiState.error.toString())
                }

            }

            is EditGoodsViewModel.UiState.Idle,
            is EditGoodsViewModel.UiState.InputError,
            EditGoodsViewModel.UiState.UpdateInProgress,
            is EditGoodsViewModel.UiState.UpdateSuccess,
            is EditGoodsViewModel.UiState.UpdateError,
            is EditGoodsViewModel.UiState.ConfirmDelete,
            is EditGoodsViewModel.UiState.DeleteError,
            EditGoodsViewModel.UiState.DeleteInProgress,
            EditGoodsViewModel.UiState.DeleteSuccess
                -> {
                EditToDoForm(
                    modifier = Modifier.padding(innerPadding),
                    name = name,
                    price = price,
                    setName = { name = it },
                    setPrice = { price = it.toLongOrNull() ?: 0L }
                )
                if (uiState is EditGoodsViewModel.UiState.ConfirmDelete) {
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
            EditGoodsViewModel.UiState.Initial -> {
                load()
            }

            EditGoodsViewModel.UiState.Loading -> {

            }

            is EditGoodsViewModel.UiState.LoadSuccess -> {
                name = uiState.goods.name
                price = uiState.goods.price
                moveToIdle()
            }

            is EditGoodsViewModel.UiState.LoadError -> {

            }

            is EditGoodsViewModel.UiState.Idle -> {

            }

            is EditGoodsViewModel.UiState.InputError -> {
                snackbarHostState.showSnackbar(
                    message = context.getString(R.string.title_empty)
                )
                moveToIdle()
            }

            EditGoodsViewModel.UiState.UpdateSuccess -> {

            }

            EditGoodsViewModel.UiState.UpdateInProgress -> {
                back()
            }

            is EditGoodsViewModel.UiState.UpdateError -> {
                snackbarHostState.showSnackbar(
                    message = uiState.e.toString()
                )
                moveToIdle()

            }

            EditGoodsViewModel.UiState.DeleteInProgress -> {

            }

            is EditGoodsViewModel.UiState.ConfirmDelete -> {

            }

            is EditGoodsViewModel.UiState.DeleteError -> {
                snackbarHostState.showSnackbar(
                    message = uiState.e.toString()
                )
                moveToIdle()
            }

            EditGoodsViewModel.UiState.DeleteSuccess -> {
                back()
            }
        }

    }

}

@Composable
fun EditToDoForm(
    modifier: Modifier = Modifier,
    name: String,
    price: Long,
    setName: (String) -> Unit,
    setPrice: (String) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            label = { Text(stringResource(R.string.goods_name)) },
            value = name,
            onValueChange = setName,
            singleLine = true,
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp,
                )
                .fillMaxWidth()

        )
        OutlinedTextField(
            label = { Text(stringResource(R.string.goods_price)) },
            value = if (price == 0L) "" else price.toString(),
//            onValueChange = { price = it.toLongOrNull() ?: 0L },
            onValueChange = setPrice,
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                )
                .padding(bottom = 8.dp)
                .fillMaxSize()
        )
    }
}