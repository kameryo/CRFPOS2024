package com.example.feature_record

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditRecordScreen(
    uiState: EditRecordViewModel.UiState,
    back: () -> Unit,
) {
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
            )
        }
    ) { innerPadding ->

        EditRecordForm(
            modifier = Modifier.padding(innerPadding)
        )

//        when (uiState) {
//            EditRecordViewModel.UiState.Initial -> {
//                EditRecordForm(
//                    modifier = Modifier.padding(innerPadding)
//                )
//            }
//
//            EditRecordViewModel.UiState.Initial -> {
//
//            }
//            is EditRecordViewModel.UiState.LoadError -> {
//
//            }
//            is EditRecordViewModel.UiState.LoadSuccess -> {
//
//            }
//            EditRecordViewModel.UiState.Loading -> {
//
//            }
//        }
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