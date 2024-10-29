package com.example.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.CartItem

@Entity(
    tableName = "record"
)
data class RecordEntity (
    @PrimaryKey(autoGenerate = true)
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