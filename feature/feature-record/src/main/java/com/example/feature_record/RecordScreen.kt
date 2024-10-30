package com.example.feature_record

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.model.Record

@Composable
fun RecordScreen(
    back: () -> Unit,
    viewModel: RecordViewModel,
    toEdit: (Long) -> Unit,
) {
    val items = viewModel.items.collectAsState(initial = emptyList())
    RecordScreen(
        recordList = items.value,
        back = back,
        toEdit = toEdit,
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecordScreen(
    recordList: List<Record>,
    back: () -> Unit,
    toEdit: (Long) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.record_manage)) },
                navigationIcon = {
                    IconButton(onClick = back) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
        ) {
            items(
                count = recordList.size,
                key = { index -> recordList[index].id },
                itemContent = {
                    RecordListItem(
                        record = recordList[it],
                        onClick = {
                            toEdit(recordList[it].id)
                        }

                    )
                }
            )
        }
    }
}

@Composable
private fun RecordListItem(
    record: Record,
    onClick: () -> Unit,
) {
    ListItem(
        headlineContent = {
            Row {
                Text(record.time.toString())
                Text(record.total.toString())
            }
        },

        modifier = Modifier.clickable {
            onClick()
        }
    )
    HorizontalDivider()
}