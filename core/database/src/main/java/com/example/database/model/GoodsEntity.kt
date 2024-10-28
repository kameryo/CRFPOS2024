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
    val displayOrder: Int,
    val isPartOfSet: Boolean = false,  // セット販売かどうか
    val setId: Long? = null,           // セットID (同じセットの商品に同じIDを割り当てる)
    val setPrice: Int? = null,         // セット価格 (まとめ買い価格)
    val setRequiredQuantity: Int? = null, // セットで必要な購入個数 (例: 3個)
    val isBulkOnly: Boolean = false,    // まとめ買い専用フラグ
)