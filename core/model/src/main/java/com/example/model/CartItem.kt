package com.example.model

data class CartItem(
    val goods: Goods,
    var quantity: Int = 0,
)