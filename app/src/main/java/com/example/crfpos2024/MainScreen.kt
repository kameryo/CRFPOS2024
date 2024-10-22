package com.example.crfpos2024

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(
    toSalesScreen: () -> Unit,
    toRecordScreen: () -> Unit,
    toGoodsScreen: () -> Unit,
    ) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(onClick = toSalesScreen) {
            Text(stringResource(R.string.sales))
        }
        Button(onClick = toRecordScreen) {
            Text(stringResource(R.string.records_manage))
        }
        Button(onClick = toGoodsScreen) {
            Text(stringResource(R.string.goods_manage))
        }
    }

}

@Composable
@Preview
private fun Preview() {
    MainScreen(
        toSalesScreen = {},
        toRecordScreen = {},
        toGoodsScreen = {},
    )
}