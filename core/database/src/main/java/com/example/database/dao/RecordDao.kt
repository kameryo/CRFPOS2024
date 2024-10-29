package com.example.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.database.model.RecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordDao {
    @Insert
    suspend fun add(record: RecordEntity): Long

    @Query("SELECT * FROM record order by id desc")
    fun getAll(): Flow<List<RecordEntity>>

    @Query("SELECT * FROM record WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): RecordEntity?

    @Update
    suspend fun update(entity: RecordEntity)

    @Delete
    suspend fun delete(entity: RecordEntity)
}