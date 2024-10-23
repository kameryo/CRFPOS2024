package com.example.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "goods"
)
data class GoodsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val price: Int,
    val purchases: Int,
    val remain: Int,
    val isAvailable: Boolean,
    val displayOrder: Long,
)