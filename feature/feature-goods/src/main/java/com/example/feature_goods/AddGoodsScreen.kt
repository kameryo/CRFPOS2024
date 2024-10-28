package com.example.feature_goods

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
fun AddGoodsScreen(
    viewModel: AddGoodsViewModel,
    back: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    AddGoodsScreen(
        uiState = uiState,
        back = back,
        add = { goods ->
            viewModel.add(goods)
        },
        moveToIdle = {
            viewModel.moveToIdle()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddGoodsScreen(
    uiState: AddGoodsViewModel.UiState,
    back: () -> Unit,
    add: (Goods) -> Unit,
    moveToIdle: () -> Unit,
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

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.goods_add)) },
                navigationIcon = {
                    IconButton(onClick = back) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (name.isNotEmpty() && price.isNotEmpty()) {
                                val goods = Goods(
                                    id = 0,
                                    name = name,
                                    price = price.toInt(),
                                    purchases = 1,
                                    remain = 1,
                                    isAvailable = isAvailable,
                                    displayOrder = if (displayOrder.isNotEmpty()) displayOrder.toInt() else 1,
                                    isPartOfSet = isPartOfSet,
                                    setId = if (setId.isNotEmpty()) setId.toLong() else null,
                                    setPrice = if (setPrice.isNotEmpty()) setPrice.toInt() else null,
                                    setRequiredQuantity = if (setRequiredQuantity.isNotEmpty()) setRequiredQuantity.toInt() else null,
                                    isBulkOnly = if (isPartOfSet) isBulkOnly else false,
                                )
                                add(goods)
                            }
                        },
                    ) {
                        Icon(Icons.Filled.Done, contentDescription = "Add")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },

        ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {

            // 商品名の入力
            OutlinedTextField(
                label = { Text(stringResource(R.string.goods_name)) },
                value = name,
                onValueChange = { name = it },
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
                onValueChange = { price = it },
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
                    onCheckedChange = { isAvailable = it }
                )
            }

            // 表示順の入力
            OutlinedTextField(
                label = { Text(stringResource(R.string.display_order)) },
                value = displayOrder,
                onValueChange = { displayOrder = it },
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
                    onCheckedChange = { isPartOfSet = it }
                )
            }

            // セットIDの入力
            OutlinedTextField(
                value = setId,
                onValueChange = { setId = it },
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
                value = setPrice,
                onValueChange = { setPrice = it },
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
                onValueChange = { setRequiredQuantity = it },
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
                    onCheckedChange = { isBulkOnly = it }
                )
            }
        }
    }

    LaunchedEffect(uiState) {
        when (uiState) {
            AddGoodsViewModel.UiState.Idle -> {
                //何もなし
            }

            AddGoodsViewModel.UiState.InputError -> {
                snackbarHostState.showSnackbar(
                    message = context.getString(R.string.title_empty)
                )
                moveToIdle()
            }

            AddGoodsViewModel.UiState.Success -> {
                back()
            }

            is AddGoodsViewModel.UiState.AddError -> {
                snackbarHostState.showSnackbar(
                    message = uiState.e.toString()
                )
                moveToIdle()

            }

        }
    }
}