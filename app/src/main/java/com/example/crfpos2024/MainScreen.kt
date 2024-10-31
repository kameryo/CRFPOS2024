package com.example.crfpos2024

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.crfpos2024.ui.theme.CRFPOS2024Theme

@Composable
fun MainScreen(
    toSalesScreen: () -> Unit,
    toRecordScreen: () -> Unit,
    toGoodsScreen: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(start = 20.dp, end = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        CustomButton(
            text = stringResource(R.string.sales),
            textColor = Color.Black,
            backgroundColor = Color.LightGray,
            onClick = toSalesScreen,
            modifier = Modifier.padding(bottom = 15.dp)
        )

        Spacer(modifier = Modifier.height(15.dp))

        CustomButton(
            text = stringResource(R.string.records_manage),
            textColor = Color.Black,
            backgroundColor = Color.LightGray,
            onClick = toRecordScreen,
            modifier = Modifier.padding(bottom = 15.dp)
        )

        Spacer(modifier = Modifier.height(15.dp))

        CustomButton(
            text = stringResource(R.string.goods_manage),
            textColor = Color.Black,
            backgroundColor = Color.LightGray,
            onClick = toGoodsScreen,
            modifier = Modifier.padding(bottom = 15.dp)
        )

    }

}

@Composable
private fun CustomButton(
    text: String,
    textColor: Color,
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .width(350.dp)
            .height(180.dp)
            .padding(1.dp)
            .background(backgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        CustomBorder(
            4.dp,
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
@Preview(
    device = "spec:width=1920px,height=1200px,dpi=230",
    showBackground = true,
    showSystemUi = true,
//    backgroundColor = 0xFFFFFFFF
)
private fun Preview() {
    CRFPOS2024Theme {
        MainScreen(
            toSalesScreen = {},
            toRecordScreen = {},
            toGoodsScreen = {},
        )
    }

}