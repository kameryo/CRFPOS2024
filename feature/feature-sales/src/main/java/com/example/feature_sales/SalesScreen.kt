package com.example.feature_sales

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
//import com.example.crfpos2024.ui.theme.CRFPOS2024Theme
import com.example.model.CartItem
import com.example.model.Goods

private const val MAX_NAME_DIGITS = 5
private const val MAX_PRICE_SELECT_ITEM_DIGITS = 4
private const val MAX_PRICE_SUB_DIGITS = 6
private const val MAX_TICKET_DIGITS = 4


@Composable
fun SalesScreen(
    viewModel: SalesViewModel,
    back: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val salesScreenState by viewModel.salesScreenState.collectAsState()
    val goodsList by viewModel.goodsItems.collectAsState(initial = emptyList())

    SalesScreen(
        uiState = uiState,
        back = back,
        reset = {
            viewModel.reset()
        },
        moveToIdle = {
            viewModel.moveToIdle()
        },
        onChangeAdultCount = { adultCount ->
            viewModel.updateAdultCount(adultCount)
        },
        onChangeChildCount = { childCount ->
            viewModel.updateChildCount(childCount)
        },
        salesScreenState = salesScreenState,
        goodsList = goodsList,
        onClickMinusForSelectedGoods = { cartItem ->
            viewModel.minusItem(cartItem)
        },
        onClickPlusForSelectedGoods = { cartItem ->
            viewModel.plusItem(cartItem)
        },
        onClickDeleteForSelectedGoods = { cartItem ->
            viewModel.deleteItem(cartItem)
        },
        onClickGoodsFromList = { goods ->
            viewModel.addItem(goods)
        },
        onClickAdjust = {
            viewModel.saveRecord()
            viewModel.reset()
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SalesScreen(
    uiState: SalesViewModel.UiState,
    back: () -> Unit,
    reset: () -> Unit,
    moveToIdle: () -> Unit,
    onChangeAdultCount: (Int) -> Unit,
    onChangeChildCount: (Int) -> Unit,
    onClickMinusForSelectedGoods: (CartItem) -> Unit,
    onClickPlusForSelectedGoods: (CartItem) -> Unit,
    onClickDeleteForSelectedGoods: (CartItem) -> Unit,
    salesScreenState: SalesScreenState,
    goodsList: List<Goods>,
    onClickGoodsFromList: (Goods) -> Unit,
    onClickAdjust: () -> Unit
) {

    var adultManualCountText by rememberSaveable { mutableStateOf("") }
    var childManualCountText by rememberSaveable { mutableStateOf("") }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.crfpos)) },
                navigationIcon = {
                    IconButton(onClick = back) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        reset()
                    }) {
                        Icon(Icons.Filled.Refresh, contentDescription = "Home")
                    }
                }
            )
        },
    ) { paddingValues ->

        SalesScreenContent(
            modifier = Modifier.padding(paddingValues),
            adultCount = salesScreenState.adultCount,
            childCount = salesScreenState.childCount,
            adultManualCountText = adultManualCountText,
            childManualCountText = childManualCountText,
            onChangeAdultCount = {
                onChangeAdultCount(it)
            },
            onChangeChildCount = {
                onChangeChildCount(it)
            },
            onChangeAdultManualCountText = {
                adultManualCountText = it
            },
            onChangeChildManualCountText = {
                childManualCountText = it
            },
            subFare = salesScreenState.subFare,
            subGoods = salesScreenState.subGoods,
            total = salesScreenState.total,
            isDrivingTicketInSelectedGoods = false,
            normalTicketCount = salesScreenState.normalTicketCount,
            accompanyTicketCount = salesScreenState.accompanyTicketCount,
            drivingTicketCount = salesScreenState.drivingTicketCount,
            goodsList = goodsList,
            selectedGoods = salesScreenState.selectedGoods,
            onClickMinusForSelectedGoods = { onClickMinusForSelectedGoods(it) },
            onClickPlusForSelectedGoods = { onClickPlusForSelectedGoods(it) },
            onClickDeleteForSelectedGoods = { onClickDeleteForSelectedGoods(it) },
            onClickGoodsFromList = { onClickGoodsFromList(it) },
            onClickAdjust = { onClickAdjust() }
        )
    }

    LaunchedEffect(uiState) {
        when (uiState) {
            SalesViewModel.UiState.Initial -> {
                reset()
            }

            SalesViewModel.UiState.Resetting,
            SalesViewModel.UiState.Idle -> {
            }

            SalesViewModel.UiState.ResetSuccess -> {
                moveToIdle()
            }

        }
    }
}

@Composable
private fun SalesScreenContent(
    modifier: Modifier = Modifier,
    adultCount: Int,
    childCount: Int,
    subFare: Int,
    subGoods: Int,
    total: Int,
    normalTicketCount: Int,
    accompanyTicketCount: Int,
    drivingTicketCount: Int,
    selectedGoods: List<CartItem>,

    adultManualCountText: String,
    childManualCountText: String,
    onChangeAdultCount: (Int) -> Unit,
    onChangeChildCount: (Int) -> Unit,
    onChangeAdultManualCountText: (String) -> Unit,
    onChangeChildManualCountText: (String) -> Unit,

    onClickMinusForSelectedGoods: (CartItem) -> Unit,
    onClickPlusForSelectedGoods: (CartItem) -> Unit,
    onClickDeleteForSelectedGoods: (CartItem) -> Unit,

    isDrivingTicketInSelectedGoods: Boolean,
    goodsList: List<Goods>,
    onClickGoodsFromList: (Goods) -> Unit,
    onClickAdjust: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 32.dp),
        horizontalArrangement = Arrangement.spacedBy(32.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier.width(400.dp)
        ) {
            ShowPersonCount(
                adultCount = adultCount,
                childCount = childCount,
            )

            ShowCartItems(
                selectedGoods = selectedGoods.sortedBy { it.goods.id },
                onClickMinusForSelectedGoods = { onClickMinusForSelectedGoods(it) },
                onClickPlusForSelectedGoods = { onClickPlusForSelectedGoods(it) },
                onClickDeleteForSelectedGoods = { onClickDeleteForSelectedGoods(it) },
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(25.dp),
        ) {
            ShowSummary(
                subFare = subFare,
                subGoods = subGoods,
                total = total,
                isDrivingTicketInSelectedGoods = isDrivingTicketInSelectedGoods,
                normalTicketCount = normalTicketCount,
                accompanyTicketCount = accompanyTicketCount,
                drivingTicketCount = drivingTicketCount,
                onClickAdjust = onClickAdjust,
            )

            ShowSelectPersonCount(
                adultCount = adultCount,
                childCount = childCount,
                onChangeAdultCount = {
                    onChangeAdultCount(it)
                },

                adultManualCountText = adultManualCountText,
                onChangeAdultManualCountText = {
                    onChangeAdultManualCountText(it)
                },
                onClickApplyAdultManualCountText =
                {
                    if (adultManualCountText.toIntOrNull() != null) {
                        onChangeAdultCount(adultManualCountText.toIntOrNull()!!)
                    } else {
                        onChangeAdultManualCountText("")
                    }
                },
                onChangeChildCount = {
                    onChangeChildCount(it)
                },
                childManualCountText = childManualCountText,
                onChangeChildManualCountText = {
                    onChangeChildManualCountText(it)
                },
                onClickApplyChildManualCountText = {
                    if (childManualCountText.toIntOrNull() != null) {
                        onChangeChildCount(childManualCountText.toIntOrNull()!!)
                    } else {
                        onChangeChildManualCountText("")
                    }
                },
            )

            ShowGoodsList(
                goodsList = goodsList,
                onClickGoodsFromList = { onClickGoodsFromList(it) }
            )


        }
    }
}

@Composable
private fun ShowPersonCount(
    adultCount: Int,
    childCount: Int,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .width(100.dp)
                .height(70.dp),
            contentAlignment = Alignment.Center,
        ) {
            CustomBorder(
                1.5.dp,
                Color.DarkGray,
                Color.White,
                Color.DarkGray,
                Color.White
            )
            Text(
                text = stringResource(R.string.adult),
                fontSize = 30.sp,
                color = Color.Blue
            )
        }
        Box(
            modifier = Modifier
                .width(80.dp)
                .height(70.dp)
                .background(Color.White),
            contentAlignment = Alignment.Center,
        ) {
            CustomBorder(
                1.5.dp,
                Color.DarkGray,
                Color.DarkGray,
                Color.DarkGray,
                Color.DarkGray
            )
            Text(
                text = adultCount.toString(),
                fontSize = 40.sp,
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.weight(10F))
        Box(
            modifier = Modifier
                .width(100.dp)
                .height(70.dp),
            contentAlignment = Alignment.Center,
        ) {
            CustomBorder(
                1.5.dp,
                Color.DarkGray,
                Color.White,
                Color.DarkGray,
                Color.White
            )
            Text(
                text = stringResource(R.string.child),
                fontSize = 30.sp,
                color = Color.Blue
            )
        }
        Box(
            modifier = Modifier
                .width(80.dp)
                .height(70.dp)
                .background(Color.White),
            contentAlignment = Alignment.Center,
        ) {
            CustomBorder(
                1.5.dp,
                Color.DarkGray,
                Color.DarkGray,
                Color.DarkGray,
                Color.DarkGray
            )
            Text(
                text = childCount.toString(),
                fontSize = 40.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
private fun ShowCartItems(
    selectedGoods: List<CartItem>,
    onClickMinusForSelectedGoods: (CartItem) -> Unit,
    onClickPlusForSelectedGoods: (CartItem) -> Unit,
    onClickDeleteForSelectedGoods: (CartItem) -> Unit,
) {
    LazyColumn {
        items(
            count = selectedGoods.size,
            key = { index -> selectedGoods[index].goods.id },
            itemContent = { index ->
                RequestingProductItemView(
                    productName = selectedGoods[index].goods.name,
                    numOfOrder = selectedGoods[index].quantity,
                    unitPrice = selectedGoods[index].goods.price,
                    isAvailable = selectedGoods[index].goods.isAvailable,
                    isPartOfSet = selectedGoods[index].goods.isPartOfSet,
                    setPrice = selectedGoods[index].goods.setPrice,
                    setRequiredQuantity = selectedGoods[index].goods.setRequiredQuantity,
                    onClickMinus = { onClickMinusForSelectedGoods(selectedGoods[index]) },
                    onClickPlus = { onClickPlusForSelectedGoods(selectedGoods[index]) },
                    onClickDelete = { onClickDeleteForSelectedGoods(selectedGoods[index]) },
                )
            }
        )
    }
}

@Composable
private fun ShowSummary(
    subFare: Int,
    subGoods: Int,
    total: Int,
    isDrivingTicketInSelectedGoods: Boolean,
    normalTicketCount: Int,
    accompanyTicketCount: Int,
    drivingTicketCount: Int,
    onClickAdjust: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = stringResource(R.string.fare),
                    fontSize = 30.sp,
                )
                Text(
                    text = stringResource(id = R.string.yen, subFare.toString()),
                    fontSize = 30.sp,
                    modifier = Modifier
                        .width(
                            // MAX_PRICE_DIGITS分のwidthを確保する
                            with(LocalDensity.current) {
                                MaterialTheme.typography.titleLarge.fontSize.toDp() * MAX_PRICE_SUB_DIGITS
                            }
                        ),
                    textAlign = TextAlign.End
                )
            }
            Spacer(modifier = Modifier.height(1.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = stringResource(id = R.string.buppan),
                    fontSize = 30.sp,
                )
                Text(
                    text = stringResource(id = R.string.yen, subGoods),
                    fontSize = 30.sp,
                    modifier = Modifier
                        .width(
                            // MAX_PRICE_DIGITS分のwidthを確保する
                            with(LocalDensity.current) {
                                MaterialTheme.typography.titleLarge.fontSize.toDp() * MAX_PRICE_SUB_DIGITS
                            }
                        ),
                    textAlign = TextAlign.End
                )
            }
            Spacer(modifier = Modifier.height(1.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(id = R.string.sum),
                    fontSize = 35.sp,
                )
                Text(
                    text = stringResource(id = R.string.yen, total),
                    fontSize = 35.sp,
                    modifier = Modifier
                        .width(
                            // MAX_PRICE_DIGITS分のwidthを確保する
                            with(LocalDensity.current) {
                                MaterialTheme.typography.titleLarge.fontSize.toDp() * MAX_PRICE_SUB_DIGITS
                            }
                        ),
                    textAlign = TextAlign.End
                )
            }
        }
        Column {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Column {
                    if (subFare != 0) {
                        Text(
                            text = stringResource(id = R.string.normal_ticket),
                            fontSize = 27.sp,
                        )
                        Text(
                            text = stringResource(id = R.string.accompany_ticket),
                            fontSize = 27.sp
                        )
                    }
                    if (isDrivingTicketInSelectedGoods) {
                        Text(
                            text = stringResource(id = R.string.driving_ticket),
                            fontSize = 27.sp
                        )
                    }
                }
                Column {
                    if (subFare != 0) {
                        Text(
                            text = stringResource(id = R.string.mai, normalTicketCount),
                            fontSize = 27.sp,
                            modifier = Modifier
                                .width(
                                    // MAX_TICKET_DIGITS分のwidthを確保する
                                    with(LocalDensity.current) {
                                        MaterialTheme.typography.titleLarge.fontSize.toDp() * MAX_TICKET_DIGITS
                                    }
                                ),
                            textAlign = TextAlign.End
                        )
                        Text(
                            text = stringResource(id = R.string.mai, accompanyTicketCount),
                            fontSize = 27.sp,
                            modifier = Modifier
                                .width(
                                    // MAX_TICKET_DIGITS分のwidthを確保する
                                    with(LocalDensity.current) {
                                        MaterialTheme.typography.titleLarge.fontSize.toDp() * MAX_TICKET_DIGITS
                                    }
                                ),
                            textAlign = TextAlign.End
                        )
                    }
                    if (isDrivingTicketInSelectedGoods) {
                        Text(
                            text = stringResource(id = R.string.mai, drivingTicketCount),
                            fontSize = 27.sp,
                            modifier = Modifier
                                .width(
                                    // MAX_PRICE_DIGITS分のwidthを確保する
                                    with(LocalDensity.current) {
                                        MaterialTheme.typography.titleLarge.fontSize.toDp() * MAX_TICKET_DIGITS
                                    }
                                ),
                            textAlign = TextAlign.End
                        )
                    }
                }

            }

            Spacer(modifier = Modifier.height(2.dp))
        }
        AdjustButton(
            text = stringResource(id = R.string.adjust),
            onClick = onClickAdjust,
            backgroundColor = Color.Yellow,
            textColor = Color.Black,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ShowSelectPersonCount(
    adultCount: Int,
    childCount: Int,
    adultManualCountText: String,
    childManualCountText: String,
    onChangeAdultCount: (Int) -> Unit,
    onChangeAdultManualCountText: (String) -> Unit,
    onClickApplyAdultManualCountText: () -> Unit,
    onChangeChildCount: (Int) -> Unit,
    onChangeChildManualCountText: (String) -> Unit,
    onClickApplyChildManualCountText: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        FlowRow(
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        ) {
            listOf(1, 2, 3, 4, 0).forEach { count ->
                if (adultCount == count) {
                    SquareButton(
                        text = stringResource(id = R.string.adult) + count,
                        onClick = {},
                        backgroundColor = Color.Cyan,
                        textColor = Color.Black,
                    )
                } else {
                    SquareButton(
                        text = stringResource(id = R.string.adult) + count,
                        onClick = { onChangeAdultCount(count) },
                        backgroundColor = Color.White,
                        textColor = Color.Black,
                    )
                }
            }
            TextField(
                value = adultManualCountText,
                onValueChange = onChangeAdultManualCountText,
                modifier = Modifier
                    .width(60.dp)
                    .height(80.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = MaterialTheme.typography.titleLarge,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White
                ),
                singleLine = true,
            )
            SquareButton(
                text = stringResource(id = R.string.input),
                onClick = onClickApplyAdultManualCountText,
                backgroundColor = Color.White,
                textColor = Color.Black,
            )
        }
        FlowRow(
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        ) {
            listOf(1, 2, 3, 4, 0).forEach { count ->
                if (childCount == count) {
                    SquareButton(
                        text = stringResource(id = R.string.child) + count,
                        onClick = { },
                        backgroundColor = Color.Cyan,
                        textColor = Color.Black,
                    )
                } else {
                    SquareButton(
                        text = stringResource(id = R.string.child) + count,
                        onClick = { onChangeChildCount(count) },
                        backgroundColor = Color.White,
                        textColor = Color.Black,
                    )
                }
            }
            TextField(
                value = childManualCountText,
                onValueChange = onChangeChildManualCountText,
                modifier = Modifier
                    .width(60.dp)
                    .height(80.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = MaterialTheme.typography.titleLarge,
                singleLine = true,
            )
            SquareButton(
                text = stringResource(id = R.string.input),
                onClick = onClickApplyChildManualCountText,
                backgroundColor = Color.White,
                textColor = Color.Black,
            )
        }
    }
}

@Composable
private fun ShowGoodsList(
    goodsList: List<Goods>,
    onClickGoodsFromList: (Goods) -> Unit,
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(120.dp),
    ) {
        items(
            count = goodsList.size,
            key = { index -> goodsList[index].id },
            itemContent = { index ->
                GoodsListItem(
                    name = goodsList[index].name,
                    price = goodsList[index].price,
                    isAvailable = goodsList[index].isAvailable,
                    isPartOfSet = goodsList[index].isPartOfSet,
                    setPrice = goodsList[index].setPrice,
                    setRequiredQuantity = goodsList[index].setRequiredQuantity,

                    onClick = { onClickGoodsFromList(goodsList[index]) },
                    modifier = Modifier
                )
            }
        )
    }
}

@Composable
fun GoodsListItem(
    name: String,
    price: Int,
    isAvailable: Boolean,
    isPartOfSet: Boolean,
    setPrice: Int?,
    setRequiredQuantity: Int?,

    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .padding(5.dp)
            .clickable(onClick = {
                if (isAvailable) {
                    onClick()
                }
            })
            .background(if (isAvailable) Color.White else Color.Gray)
            .padding(16.dp),
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text =
            if (isPartOfSet) {
                stringResource(
                    id = R.string.set_price_item,
                    setRequiredQuantity.toString(),
                    setPrice.toString(),
                )
            } else {
                stringResource(id = R.string.yen, price)
            },
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}


@Composable
private fun RequestingProductItemView(
    modifier: Modifier = Modifier,
    productName: String,
    numOfOrder: Int,
    unitPrice: Int,
    isAvailable: Boolean,
    isPartOfSet: Boolean,
    setPrice: Int?,
    setRequiredQuantity: Int?,
    onClickMinus: () -> Unit,
    onClickPlus: () -> Unit,
    onClickDelete: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Text(
            text = productName,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.width(
                // MAX_PRICE_DIGITS分のwidthを確保する
                with(LocalDensity.current) {
                    MaterialTheme.typography.titleLarge.fontSize.toDp() * MAX_NAME_DIGITS
                }
            )
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                FilledTonalIconButton(onClick = onClickMinus) {
                    Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = null)
                }
                Text(
                    text = numOfOrder.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(
                        // MAX_PRICE_DIGITS分のwidthを確保する
                        with(LocalDensity.current) {
                            MaterialTheme.typography.titleLarge.fontSize.toDp() * 2
                        }
                    )
                )
                FilledTonalIconButton(onClick = onClickPlus) {
                    Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = null)
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (isPartOfSet) {
                    Text(
                        text = stringResource(
                            id = R.string.set_price_item,
                            setRequiredQuantity.toString(),
                            setPrice.toString(),
                        ),
                        style = MaterialTheme.typography.titleSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(
                            // MAX_PRICE_DIGITS分のwidthを確保する
                            with(LocalDensity.current) {
                                MaterialTheme.typography.titleLarge.fontSize.toDp() * MAX_PRICE_SELECT_ITEM_DIGITS
                            }
                        )
                    )
                } else {
                    Text(
                        text = stringResource(id = R.string.yen, numOfOrder * unitPrice),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.End,
                        modifier = Modifier.width(
                            // MAX_PRICE_DIGITS分のwidthを確保する
                            with(LocalDensity.current) {
                                MaterialTheme.typography.titleLarge.fontSize.toDp() * MAX_PRICE_SELECT_ITEM_DIGITS
                            }
                        )
                    )
                }
                FilledTonalIconButton(onClick = onClickDelete) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                }
            }
        }
    }
}

@Composable
private fun CustomBorder(
    widthDp: Dp,
    topColor: Color,
    bottomColor: Color,
    leftColor: Color,
    rightColor: Color,
) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = widthDp.toPx()
        // Boxの上と左の枠を黒に描画
        drawRect(
            color = topColor,
            topLeft = Offset(0f, 0f),
            size = Size(size.width, width),
        )
        drawRect(
            color = leftColor,
            topLeft = Offset(0f, 0f),
            size = Size(width, size.height),
        )
        // Boxの下と右の辺を白に描画
        drawRect(
            color = bottomColor,
            topLeft = Offset(0f, size.height - width),
            size = Size(size.width, width),
        )
        drawRect(
            color = rightColor,
            topLeft = Offset(size.width - width, 0f),
            size = Size(width, size.height),
        )
    }
}

@Composable
private fun AdjustButton(
    text: String,
    textColor: Color,
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .width(180.dp)
            .height(120.dp)
            .padding(1.dp)
            .background(backgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        CustomBorder(
            2.dp,
            Color.Black,
            Color.Black,
            Color.Black,
            Color.Black
        )
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            color = textColor,
            fontSize = 50.sp
        )
    }
}

@Composable
private fun SquareButton(
    text: String,
    textColor: Color,
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .width(120.dp)
            .height(85.dp)
            .padding(1.dp)
            .background(backgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        CustomBorder(
            1.5.dp,
            Color.Black,
            Color.Black,
            Color.Black,
            Color.Black
        )
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            color = textColor,
        )
    }
}

@Composable
fun CRFPOS2024ThemePreview(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(background = Color(0xFFCCCCBC)),
        typography = Typography(),
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(
    device = "spec:width=1920px,height=1200px,dpi=230",
    showBackground = true,
    showSystemUi = true,
//    backgroundColor = 0xFFCCCCCC
)
private fun SalesScreenPreview() {
    CRFPOS2024ThemePreview {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.crfpos)) },
                    navigationIcon = {
                        IconButton(onClick = { }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = { }) {
                            Icon(Icons.Filled.Refresh, contentDescription = "Home")
                        }
                    }
                )
            },
        ) { paddingValues ->
            SalesScreenContent(
                modifier = Modifier.padding(paddingValues),
                adultCount = 1,
                childCount = 2,
                adultManualCountText = "3",
                childManualCountText = "3",
                onChangeAdultCount = {},
                onChangeChildCount = {},
                onChangeAdultManualCountText = {},
                onChangeChildManualCountText = {},
                subFare = 1,
                subGoods = 0,
                total = 1,
                isDrivingTicketInSelectedGoods = true,
                normalTicketCount = 1,
                accompanyTicketCount = 1,
                drivingTicketCount = 3,
                goodsList = listOf(
                    Goods(
                        id = 1,
                        name = "商品1",
                        price = 100,
                        purchases = 0,
                        remain = 0,
                        isAvailable = true,
                        displayOrder = 1,
                    ),
                    Goods(
                        id = 2,
                        name = "商品2",
                        price = 200,
                        purchases = 0,
                        remain = 0,
                        isAvailable = true,
                        displayOrder = 1,
                    ),
                    Goods(
                        id = 3,
                        name = "商品3",
                        price = 300,
                        purchases = 0,
                        remain = 0,
                        isAvailable = true,
                        displayOrder = 1,
                    ),
                    Goods(
                        id = 4,
                        name = "商品4",
                        price = 400,
                        purchases = 0,
                        remain = 0,
                        isAvailable = true,
                        displayOrder = 1,
                    ),
                    Goods(
                        id = 5,
                        name = "商品5",
                        price = 500,
                        purchases = 0,
                        remain = 0,
                        isAvailable = true,
                        displayOrder = 1,
                    ),
                    Goods(
                        id = 6,
                        name = "商品6",
                        price = 600,
                        purchases = 0,
                        remain = 0,
                        isAvailable = true,
                        displayOrder = 1,
                    ),
                    Goods(
                        id = 7,
                        name = "商品7",
                        price = 700,
                        purchases = 0,
                        remain = 0,
                        isAvailable = true,
                        displayOrder = 1,
                    ),

                    ),
                selectedGoods = listOf(
                    CartItem(
                        goods = Goods(
                            id = 1,
                            name = "商品商品1",
                            price = 100,
                            purchases = 0,
                            remain = 0,
                            isAvailable = true,
                            displayOrder = 1,
                        ),
                        quantity = 1,
                    ),
                    CartItem(
                        goods = Goods(
                            id = 2,
                            name = "商品2",
                            price = 200,
                            purchases = 0,
                            remain = 0,
                            isAvailable = true,
                            displayOrder = 1,
                        ),
                        quantity = 2,
                    ),
                    CartItem(
                        goods = Goods(
                            id = 3,
                            name = "商品3",
                            price = 300,
                            purchases = 0,
                            remain = 0,
                            isAvailable = true,
                            displayOrder = 1,
                        ),
                        quantity = 3,
                    ),
                ),
                onClickMinusForSelectedGoods = { },
                onClickPlusForSelectedGoods = {},
                onClickDeleteForSelectedGoods = {},
                onClickGoodsFromList = {},
                onClickAdjust = {}
            )

        }

    }


}

