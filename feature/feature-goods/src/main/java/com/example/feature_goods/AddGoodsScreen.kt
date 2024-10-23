package com.example.feature_goods

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun AddGoodsScreen(
    viewModel: AddGoodsViewModel,
    back: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    AddGoodsScreen(
        uiState = uiState,
        back = back,
        add = { name, price ->
            viewModel.add(name, price)
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
    add: (String, Long) -> Unit,
    moveToIdle: () -> Unit,
) {

    var name by rememberSaveable { mutableStateOf("") }
    var price by rememberSaveable { mutableLongStateOf(0) }

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.goods_add)) },
                navigationIcon = {
                    IconButton(onClick = back) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        add(name, price)
                    }) {
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
            OutlinedTextField(
                label = { Text(stringResource(R.string.goods_price)) },
                value = if (price == 0L) "" else price.toString(),
                onValueChange = { price = it.toLongOrNull() ?: 0L },
                singleLine = true,
                modifier = Modifier
                    .padding(
                        horizontal = 16.dp,
                        vertical = 8.dp,
                    )
                    .fillMaxWidth()
            )
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