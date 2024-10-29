package com.example.data.repository

import com.example.model.Record
import kotlinx.coroutines.flow.Flow

interface RecordRepository {
    suspend fun add(record: Record): Record
    fun getAll(): Flow<List<Record>>
    suspend fun getById(id: Long): Record?

    suspend fun update(record: Record)
    suspend fun delete(record: Record)
}