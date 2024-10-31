package com.example.feature_goods

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
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
import com.example.model.Goods

@Composable
fun GoodsScreen(
    viewModel: GoodsListViewModel,
    back: () -> Unit,
    toAddGoodsScreen: () -> Unit,
    toEdit: (Long) -> Unit,
) {
    val items = viewModel.items.collectAsState(initial = emptyList())
    GoodsScreen(
        goodsList = items.value,
        back = back,
        toAddGoodsScreen = toAddGoodsScreen,
        toEdit = toEdit,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GoodsScreen(
    goodsList: List<Goods>,
    back: () -> Unit,
    toAddGoodsScreen: () -> Unit,
    toEdit: (Long) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.goods_manage)) },
                navigationIcon = {
                    IconButton(onClick = back) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = toAddGoodsScreen) {
                        Icon(Icons.Filled.Add, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
        ) {
            items(
                count = goodsList.size,
                key = { index -> goodsList[index].id },
                itemContent = {
                    GoodsListItem(
                        goods = goodsList[it],
                        onClick = {
                            toEdit(goodsList[it].id)
                        }

                    )
                }

            )
        }
    }
}

@Composable
fun GoodsListItem(
    goods: Goods,
    onClick: () -> Unit,
) {
    ListItem(
        headlineContent = {
            Row {
                Text(goods.name)
                Text(goods.price.toString())
            }
        },

        modifier = Modifier.clickable {
            onClick()
        }
    )
    HorizontalDivider()

}