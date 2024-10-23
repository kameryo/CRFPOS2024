package com.example.data.repository

import com.example.database.dao.GoodsDao
import com.example.model.Goods
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalGoodsRepository @Inject constructor(
    private val goodsDao: GoodsDao,
) : GoodsRepository {
    override suspend fun add(title: String, description: String): Goods {
        TODO("Not yet implemented")
    }

    override fun getAll(): Flow<List<Goods>> {
        TODO("Not yet implemented")
    }

    override suspend fun getById(id: Long): Goods? {
        TODO("Not yet implemented")
    }

    override suspend fun update(goods: Goods) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(goods: Goods) {
        TODO("Not yet implemented")
    }

}