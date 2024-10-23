package com.example.model

data class Goods(
    val id: Long,
    val name: String,
    val price: Int,
    val purchases: Int,
    val remain: Int,
    val isAvailable: Boolean,
    val displayOrder: Long,
)