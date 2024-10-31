package com.example.feature_record

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Record
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RecordScreen(
    back: () -> Unit,
    viewModel: RecordViewModel,
    toEdit: (Long) -> Unit,
    toSummarizeRecord: () -> Unit
) {
    val items = viewModel.items.collectAsState(initial = emptyList())
    RecordScreen(
        recordList = items.value,
        back = back,
        toEdit = toEdit,
        toSummarizeRecord = toSummarizeRecord,
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecordScreen(
    recordList: List<Record>,
    back: () -> Unit,
    toEdit: (Long) -> Unit,
    toSummarizeRecord: () -> Unit,
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
                actions = {
                    IconButton(onClick = toSummarizeRecord) {
                        Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Add")
                    }
                }
            )
        }
    ) { paddingValues ->
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.date_time),
                fontSize = 25.sp,
                modifier = Modifier.width(350.dp)
            )

            Text(
                text = stringResource(id = R.string.total),
                fontSize = 25.sp,
                modifier = Modifier.width(200.dp)
            )

            Text(
                text = stringResource(id = R.string.adult),
                fontSize = 25.sp,
                modifier = Modifier.width(200.dp)
            )

        }
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
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = convertUnixTimeToDateTime(record.time / 1000),
                    fontSize = 25.sp,
                    modifier = Modifier.width(350.dp)
                )

                Text(
                    text = stringResource(id = R.string.yen, record.total),
                    fontSize = 25.sp,
                    modifier = Modifier.width(200.dp)
                )

                Text(
                    text = record.adult.toString(),
                    fontSize = 25.sp,
                    modifier = Modifier.width(200.dp)
                )

            }
        },

        modifier = Modifier.clickable {
            onClick()
        }
    )
    HorizontalDivider()
}

private fun convertUnixTimeToDateTime(unixTime: Long): String {
    val date = Date(unixTime * 1000)
    val formatter = SimpleDateFormat("yyyyMMdd HH:mm:ss", Locale.getDefault())
    return formatter.format(date)
}