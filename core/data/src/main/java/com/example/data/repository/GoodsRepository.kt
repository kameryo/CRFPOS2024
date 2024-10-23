package com.example.data.repository

import com.example.model.Goods
import kotlinx.coroutines.flow.Flow

interface GoodsRepository {
    suspend fun add(title: String, description: String): Goods
    fun getAll(): Flow<List<Goods>>
    suspend fun getById(id: Long): Goods?

    suspend fun update(goods: Goods)
    suspend fun delete(goods: Goods)

}