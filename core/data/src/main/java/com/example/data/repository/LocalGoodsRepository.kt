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
    override suspend fun add(goods: Goods): Goods {
        val goodsEntity = GoodsEntity(
            id = 0,
            name = goods.name,
            price = goods.price,
            purchases = goods.purchases,
            remain = goods.remain,
            isAvailable = goods.isAvailable,
            displayOrder = goods.displayOrder,
            isPartOfSet = goods.isPartOfSet,
            setId = goods.setId,
            setPrice = goods.setPrice,
            setRequiredQuantity = goods.setRequiredQuantity,
            isBulkOnly = goods.isBulkOnly,
        )
        val id = goodsDao.add(goodsEntity)
        return Goods(
            id = id,
            name = goods.name,
            price = goods.price,
            purchases = goods.purchases,
            remain = goods.remain,
            isAvailable = goods.isAvailable,
            displayOrder = goods.displayOrder,
            isPartOfSet = goods.isPartOfSet,
            setId = goods.setId,
            setPrice = goods.setPrice,
            setRequiredQuantity = goods.setRequiredQuantity,
            isBulkOnly = goods.isBulkOnly,
        )

    }

    override fun getAll(): Flow<List<Goods>> {
        return goodsDao.getAll().map { items ->
            items.map { item -> item.toModel() }
        }
    }

    override suspend fun getById(id: Long): Goods? {
        return goodsDao.getById(id)?.toModel()
    }

    override suspend fun update(goods: Goods) {
        val entity = GoodsEntity(
            id = goods.id,
            name = goods.name,
            price = goods.price,
            purchases = goods.purchases,
            remain = goods.remain,
            isAvailable = goods.isAvailable,
            displayOrder = goods.displayOrder,
        )
        goodsDao.update(entity)
    }

    override suspend fun delete(goods: Goods) {
        val entity = GoodsEntity(
            id = goods.id,
            name = goods.name,
            price = goods.price,
            purchases = goods.purchases,
            remain = goods.remain,
            isAvailable = goods.isAvailable,
            displayOrder = goods.displayOrder,
        )
        goodsDao.delete(entity)
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