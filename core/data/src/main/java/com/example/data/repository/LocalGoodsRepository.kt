package com.example.data.repository

import com.example.database.dao.GoodsDao
import com.example.database.model.GoodsEntity
import com.example.model.Goods
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalGoodsRepository @Inject constructor(
    private val goodsDao: GoodsDao,
) : GoodsRepository {
    override suspend fun add(name: String, price: Long): Goods {
        val goods = GoodsEntity(
            id = 0,
            name = name,
            price = price,
            purchases = 0,
            remain = 0,
            isAvailable = true,
            displayOrder = 0
        )
        val id = goodsDao.add(goods)
        return Goods(
            id = id,
            name = name,
            price = price,
            purchases = 0,
            remain = 0,
            isAvailable = true,
            displayOrder = id
        )

    }

    override fun getAll(): Flow<List<Goods>> {
        return goodsDao.getAll().map { items ->
            items.map { item -> item.toModel() }
        }
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

    private fun GoodsEntity.toModel() = Goods(
        id = id,
        name = name,
        price = price,
        purchases = purchases,
        remain = remain,
        isAvailable = isAvailable,
        displayOrder = displayOrder,
    )

}