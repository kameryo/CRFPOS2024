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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.database.dao.RecordDao

@Composable
fun SummarizeRecordScreen(
    back: () -> Unit,
    viewModel: SummarizeRecordViewModel,
) {
    val items = viewModel.items.collectAsState(initial = emptyList())
    val context = LocalContext.current
    SummarizeRecordScreen(
        back = back,
        recordDateList = items.value,
        toExportCSV = { date ->
            viewModel.exportRecordToCSV(date = date, context = context)
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SummarizeRecordScreen(
    back: () -> Unit,
    recordDateList: List<RecordDao.Summary>,
    toExportCSV: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.diary_summary)) },
                navigationIcon = {
                    IconButton(onClick = back) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },

                )
        },
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
        ) {
            items(
                count = recordDateList.size,
                key = { index -> recordDateList[index].date },
                itemContent = {
                    SummaryRecordItem(
                        recordSummary = recordDateList[it],
                        onClick = {
                            toExportCSV(recordDateList[it].date)
                        }

                    )
                }
            )
        }
    }
}

@Composable
fun SummaryRecordItem(
    recordSummary: RecordDao.Summary,
    onClick: () -> Unit,
) {
    ListItem(
        headlineContent = {
            Row {
                Text(recordSummary.date)
                Text(recordSummary.totalSum.toString())
            }
        },

        modifier = Modifier.clickable {
            onClick()
        }
    )
    HorizontalDivider()
}