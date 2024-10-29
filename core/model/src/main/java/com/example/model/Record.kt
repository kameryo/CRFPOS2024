package com.example.model

data class Record(
    val id: Long,
    val time: Long,
    val total: Int,
    val fareSales: Int,
    val goodsSales: Int,
    val adult: Int,
    val child: Int,
    val goodsList: List<CartItem>?,
    val memo: String
)