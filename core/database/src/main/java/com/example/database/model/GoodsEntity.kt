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
    val price: Long,
    val purchases: Long,
    val remain: Long,
    val isAvailable: Boolean,
    val displayOrder: Long,
)