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
    override suspend fun add(record: Record) {
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
        recordDao.add(recordEntity)
    }

    override fun getAll(): Flow<List<Record>> {
        return recordDao.getAll().map { items ->
            items.map { item -> item.toModel() }
        }
    }

    override fun getDateList(): Flow<List<RecordDao.Summary>> {
        return recordDao.getSummary()
    }

    override suspend fun getDiaryData(date: String): List<Record> {
        return recordDao.getDiaryData(date)
    }

    override suspend fun getById(id: Long): Record? {
        return recordDao.getById(id)?.toModel()
    }


    override suspend fun delete(record: Record) {
        val recordEntity = RecordEntity(
            id = record.id,
            time = record.time,
            total = record.total,
            fareSales = record.fareSales,
            goodsSales = record.goodsSales,
            adult = record.adult,
            child = record.child,
            goodsList = record.goodsList,
            memo = record.memo,
        )
        recordDao.delete(recordEntity)
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