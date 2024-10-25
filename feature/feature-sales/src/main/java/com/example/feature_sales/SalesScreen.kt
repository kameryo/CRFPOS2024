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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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

private const val MAX_PRICE_DIGITS = 7
private const val MAX_TICKET_DIGITS = 4


@Composable
fun SalesScreen(
    viewModel: SalesViewModel,
    back: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

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
) {
    var adultCount by rememberSaveable { mutableIntStateOf(0) }
    var childCount by rememberSaveable { mutableIntStateOf(0) }

    var adultManualCountText by rememberSaveable { mutableStateOf("") }
    var childManualCountText by rememberSaveable { mutableStateOf("") }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.crfpos)) },
                navigationIcon = {
                    IconButton(onClick = back) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
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
            adultCount = adultCount,
            childCount = childCount,
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

            )
    }

    LaunchedEffect(uiState) {
        when (uiState) {
            SalesViewModel.UiState.Initial -> {
                reset()
            }

            is SalesViewModel.UiState.Loading -> {}
            SalesViewModel.UiState.Idle -> {}
            SalesViewModel.UiState.ResetError -> {}


            is SalesViewModel.UiState.UpdatingScreen -> {
                adultCount = uiState.state.adultCount
                childCount = uiState.state.childCount
                moveToIdle()
            }


            SalesViewModel.UiState.UpdateSuccess -> {

            }

            SalesViewModel.UiState.Resetting -> {}
            SalesViewModel.UiState.UpdateInProgress -> {}
            SalesViewModel.UiState.ResetSuccess -> {}
        }
    }
}

@Composable
private fun SalesScreenContent(
    modifier: Modifier = Modifier,
    adultCount: Int,
    childCount: Int,
    adultManualCountText: String,
    childManualCountText: String,
    onChangeAdultCount: (Int) -> Unit,
    onChangeChildCount: (Int) -> Unit,
    onChangeAdultManualCountText: (String) -> Unit,
    onChangeChildManualCountText: (String) -> Unit,
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
            ShowCartItems()
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(25.dp),
        ) {
            ShowSummary()
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
private fun ShowCartItems() {
    LazyColumn {
//                items(bindModel.selectedGoods) { selected ->
//                    RequestingProductItemView(
//                        productName = selected.name,
//                        numOfOrder = selected.numOfOrder,
//                        unitPrice = selected.unitPrice,
//                        onClickMinus = { onClickMinusForSelectedGoods(selected) },
//                        onClickPlus = { onClickPlusForSelectedGoods(selected) },
//                        onClickDelete = { onClickDeleteForSelectedGoods(selected) },
//                    )
//                }
    }
}

@Composable
private fun ShowSummary() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = stringResource(R.string.fare),
                    fontSize = 25.sp,
                )
                Text(
//                            text = stringResource(id = R.string.yen, bindModel.subtotalFare),
                    text = stringResource(id = R.string.yen),

                    fontSize = 25.sp,
                    modifier = Modifier
                        .width(
                            // MAX_PRICE_DIGITS分のwidthを確保する
                            with(LocalDensity.current) {
                                MaterialTheme.typography.titleLarge.fontSize.toDp() * MAX_PRICE_DIGITS
                            }
                        ),
                    textAlign = TextAlign.End
                )
            }
            Spacer(modifier = Modifier.height(1.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = stringResource(id = R.string.buppan),
                    fontSize = 25.sp,
                )
                Text(
//                            text = stringResource(id = R.string.yen, bindModel.subtotalGoods),
                    text = stringResource(id = R.string.yen),

                    fontSize = 25.sp,
                    modifier = Modifier
                        .width(
                            // MAX_PRICE_DIGITS分のwidthを確保する
                            with(LocalDensity.current) {
                                MaterialTheme.typography.titleLarge.fontSize.toDp() * MAX_PRICE_DIGITS
                            }
                        ),
                    textAlign = TextAlign.End
                )
            }
            Spacer(modifier = Modifier.height(1.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(id = R.string.sum),
                    fontSize = 30.sp,
                )
                Text(
//                            text = stringResource(
//                                id = R.string.yen,
//                                bindModel.subtotalFare + bindModel.subtotalGoods
//                            ),
                    text = stringResource(
                        id = R.string.yen,
//                                bindModel.subtotalFare + bindModel.subtotalGoods
                    ),
                    fontSize = 30.sp,
                    modifier = Modifier
                        .width(
                            // MAX_PRICE_DIGITS分のwidthを確保する
                            with(LocalDensity.current) {
                                MaterialTheme.typography.titleLarge.fontSize.toDp() * MAX_PRICE_DIGITS
                            }
                        ),
                    textAlign = TextAlign.End
                )
            }
        }
        Column {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Column {
//                            if (bindModel.subtotalFare != 0) {
                    if (true) {
                        Text(
                            text = stringResource(id = R.string.normal_ticket),
                            fontSize = 27.sp,
                        )
                        Text(
                            text = stringResource(id = R.string.accompany_ticket),
                            fontSize = 27.sp
                        )
                    }
//                            if (bindModel.selectedGoods.any { it.name == "運転体験券" }) {
                    if (true) {
                        Text(
                            text = stringResource(id = R.string.driving_ticket),
                            fontSize = 27.sp
                        )
                    }
                }
                Column {
//                            if (bindModel.subtotalFare != 0) {
                    if (true) {
                        Text(
                            text = stringResource(
                                id = R.string.mai,
//                                        bindModel.subtotalFare / 100
                            ),
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
                            text = stringResource(
                                id = R.string.mai,
//                                        bindModel.adultCount + bindModel.childCount - bindModel.subtotalFare / 100
                            ),
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
//                            if (bindModel.selectedGoods.any { it.name == "運転体験券" }) {
                    if (true) {
                        Text(
                            text = stringResource(
                                id = R.string.mai,
//                                        bindModel.selectedGoods.find { it.name == "運転体験券" }?.numOfOrder
//                                            ?: 0
//
                            ),
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
            onClick = {},
//                    onClick = onClickAdjust,

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
private fun RequestingProductItemView(
    productName: String,
    numOfOrder: Int,
    unitPrice: Long,
    onClickMinus: () -> Unit,
    onClickPlus: () -> Unit,
    onClickDelete: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = productName,
            fontSize = 20.sp,
            color = Color.Black
        )
        Text(
            text = "$numOfOrder",
            fontSize = 20.sp,
            color = Color.Black
        )
        Text(
            text = "$unitPrice",
            fontSize = 20.sp,
            color = Color.Black
        )
        IconButton(onClick = onClickMinus) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
        }
        IconButton(onClick = onClickPlus) {
            Icon(Icons.Filled.ArrowForward, contentDescription = "Forward")
        }
        IconButton(onClick = onClickDelete) {
            Icon(Icons.Filled.Delete, contentDescription = "Delete")
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(
    device = "spec:width=1920px,height=1200px,dpi=230",
    showBackground = true,
    showSystemUi = true,
    backgroundColor = 0xFFCCCCCC
)
private fun SalesScreenPreview() {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.crfpos)) },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
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

            )
    }
}

