package com.example.model

data class Goods(
    val id: Long,
    val name: String,
    val price: Long,
    val purchases: Long,
    val remain: Long,
    val isAvailable: Boolean,
    val displayOrder: Long,
)