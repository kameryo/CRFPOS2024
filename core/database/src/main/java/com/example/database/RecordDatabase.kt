package com.example.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.database.converter.CarItemConverters
import com.example.database.dao.RecordDao
import com.example.database.model.RecordEntity

@Database(
    entities = [
        RecordEntity::class
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(CarItemConverters::class)
abstract class RecordDatabase: RoomDatabase() {
    abstract fun recordDao(): RecordDao
}