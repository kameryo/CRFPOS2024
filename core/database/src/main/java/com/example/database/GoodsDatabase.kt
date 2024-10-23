package com.example.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.database.dao.GoodsDao
import com.example.database.model.GoodsEntity

@Database(
    entities = [
        GoodsEntity::class
    ],
    version = 1,
    exportSchema = true,
)
abstract class GoodsDatabase: RoomDatabase() {
    abstract fun goodsDao(): GoodsDao
}