package com.example.data.repository

import com.example.database.dao.RecordDao
import com.example.database.model.RecordEntity
import com.example.model.Record
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalRecordRepository @Inject constructor(
    private val recordDao: RecordDao,
) : RecordRepository {
    override suspend fun add(record: Record): Record {
        val recordEntity = RecordEntity(
            id = 0,
            time = record.time,
            total = record.total,
            fareSales = record.fareSales,
            goodsSales = record.goodsSales,
            adult = record.adult,
            child = record.child,
            goodsList = record.goodsList,
            memo = record.memo,
        )
        val id = recordDao.add(recordEntity)
        return Record(
            id = id,
            time = record.time,
            total = record.total,
            fareSales = record.fareSales,
            goodsSales = record.goodsSales,
            adult = record.adult,
            child = record.child,
            goodsList = record.goodsList,
            memo = record.memo,
        )
    }

    override fun getAll(): Flow<List<Record>> {
        return recordDao.getAll().map { items ->
            items.map { item -> item.toModel() }
        }
    }

    override suspend fun getById(id: Long): Record? {
        return recordDao.getById(id)?.toModel()
    }

    override suspend fun update(record: Record) {

    }

    override suspend fun delete(record: Record) {

    }

    private fun RecordEntity.toModel() = Record(
        id = id,
        time = time,
        total = total,
        fareSales = fareSales,
        goodsSales = goodsSales,
        adult = adult,
        child = child,
        goodsList = goodsList,
        memo = memo,
    )
}