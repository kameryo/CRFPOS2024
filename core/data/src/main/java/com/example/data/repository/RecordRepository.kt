package com.example.data.repository

import com.example.database.dao.RecordDao
import com.example.model.Record
import kotlinx.coroutines.flow.Flow

interface RecordRepository {
    suspend fun add(record: Record)

    suspend fun delete(record: Record)

    fun getAll(): Flow<List<Record>>

    fun getDateList(): Flow<List<RecordDao.Summary>>

    suspend fun getDiaryData(date: String): List<Record>

    suspend fun getById(id: Long): Record?

}