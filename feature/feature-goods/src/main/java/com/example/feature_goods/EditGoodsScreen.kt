package com.example.feature_goods

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.model.Goods

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
        update = { goods ->
            viewModel.update(goods)
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
    update: (Goods) -> Unit,
    showDeleteDialog: () -> Unit,
    delete: () -> Unit,
) {
    var name by rememberSaveable { mutableStateOf("") }
    var price by rememberSaveable { mutableStateOf("") }
    var isAvailable by rememberSaveable { mutableStateOf(true) }
    var displayOrder by rememberSaveable { mutableStateOf("") }
    var isPartOfSet by rememberSaveable { mutableStateOf(false) }
    var setId by rememberSaveable { mutableStateOf("") }
    var setPrice by rememberSaveable { mutableStateOf("") }
    var setRequiredQuantity by rememberSaveable { mutableStateOf("") }
    var isBulkOnly by rememberSaveable { mutableStateOf(false) }

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
                        IconButton(
                            onClick = {
                                if (name.isNotEmpty() && price.isNotEmpty()) {
                                    val goods = Goods(
                                        id = 0,
                                        name = name,
                                        price = price.toInt(),
                                        purchases = 1,
                                        remain = 1,
                                        isAvailable = true,
                                        displayOrder = if (displayOrder.isNotEmpty()) displayOrder.toInt() else 1,
                                        isPartOfSet = isPartOfSet,
                                        setId = if (isPartOfSet) setId.toLong() else null,
                                        setPrice = if (setPrice.isNotEmpty()) setPrice.toInt() else null,
                                        setRequiredQuantity = if (setRequiredQuantity.isNotEmpty()) setRequiredQuantity.toInt() else null,
                                        isBulkOnly = if (isPartOfSet) isBulkOnly else false,
                                    )
                                    update(goods)
                                }
                            },
                        ) {
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
                    isAvailable = isAvailable,
                    displayOrder = displayOrder,
                    isPartOfSet = isPartOfSet,
                    setId = setId,
                    setPriceString = setPrice,
                    setRequiredQuantity = setRequiredQuantity,
                    isBulkOnly = isBulkOnly,

                    setName = { name = it },
                    setPrice = { price = it },
                    setIsAvailable = { isAvailable = it },
                    setDisplayOrder = { displayOrder = it },
                    setIsPartOfSet = { isPartOfSet = it },
                    setSetId = { setId = it },
                    setSetRequiredQuantity = { setRequiredQuantity = it },
                    setSetPrice = { setPrice = it },
                    setIsBulkOnly = { isBulkOnly = it }
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
                price = uiState.goods.price.toString()
                isAvailable = uiState.goods.isAvailable
                displayOrder = uiState.goods.displayOrder.toString()
                isPartOfSet = uiState.goods.isPartOfSet
                setId = uiState.goods.setId?.toString() ?: ""
                setPrice = uiState.goods.setPrice?.toString() ?: ""
                setRequiredQuantity = uiState.goods.setRequiredQuantity?.toString() ?: ""
                isBulkOnly = uiState.goods.isBulkOnly
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
    price: String,
    isAvailable: Boolean,
    displayOrder: String,
    isPartOfSet: Boolean,
    setId: String,
    setPriceString: String,
    setRequiredQuantity: String,
    isBulkOnly: Boolean,

    setName: (String) -> Unit,
    setPrice: (String) -> Unit,
    setIsAvailable: (Boolean) -> Unit,
    setDisplayOrder: (String) -> Unit,
    setIsPartOfSet: (Boolean) -> Unit,
    setSetId: (String) -> Unit,
    setSetPrice: (String) -> Unit,
    setSetRequiredQuantity: (String) -> Unit,
    setIsBulkOnly: (Boolean) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // 商品名の入力
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

        // 価格の入力
        OutlinedTextField(
            label = { Text(stringResource(R.string.goods_price)) },
            value = price,
            onValueChange = setPrice,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp,
                )
                .fillMaxWidth()
        )

        // 販売可能かどうかのフラグ
        Row(
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp,
                )
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(R.string.sales_available))
            Spacer(modifier = Modifier.width(16.dp))
            Checkbox(
                checked = isAvailable,
                onCheckedChange = setIsAvailable
            )
        }

        // 表示順の入力
        OutlinedTextField(
            label = { Text(stringResource(R.string.display_order)) },
            value = displayOrder,
            onValueChange = setDisplayOrder,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp,
                )
                .fillMaxWidth()
        )

        // セット販売かどうかのフラグ
        Row(
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp,
                )
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(R.string.set_sales_only))
            Spacer(modifier = Modifier.width(8.dp))
            Checkbox(
                checked = isPartOfSet,
                onCheckedChange = setIsPartOfSet
            )
        }

        // セットIDの入力
        OutlinedTextField(
            value = setId,
            onValueChange = setSetId,
            label = { Text(stringResource(R.string.set_id)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp,
                )
                .fillMaxWidth()
        )

        // セット価格の入力
        OutlinedTextField(
            value = setPriceString,
            onValueChange = setSetPrice,
            label = { Text(stringResource(R.string.set_price)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp,
                )
                .fillMaxWidth()
        )

        // セットに必要な個数の入力
        OutlinedTextField(
            value = setRequiredQuantity,
            onValueChange = setSetRequiredQuantity,
            label = { Text(stringResource(R.string.set_required_quantity)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp,
                )
                .fillMaxWidth()
        )

        // まとめ買い専用フラグの入力
        Row(
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp,
                )
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(R.string.bulk_only))
            Spacer(modifier = Modifier.width(8.dp))
            Checkbox(
                checked = isBulkOnly,
                onCheckedChange = setIsBulkOnly
            )
        }

    }
}