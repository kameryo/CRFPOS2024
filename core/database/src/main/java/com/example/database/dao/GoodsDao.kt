package com.example.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.database.model.GoodsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GoodsDao {
    @Insert
    suspend fun add(goods: GoodsEntity): Long

    @Query("SELECT * FROM goods order by displayOrder asc")
    fun getAll(): Flow<List<GoodsEntity>>

    @Query("SELECT * FROM goods WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): GoodsEntity?

    @Update
    suspend fun update(entity: GoodsEntity)

    @Delete
    suspend fun delete(entity: GoodsEntity)
}