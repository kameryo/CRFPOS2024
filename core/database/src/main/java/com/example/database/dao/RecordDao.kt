package com.example.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.database.model.RecordEntity
import com.example.model.Record
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordDao {
    @Insert
    suspend fun add(record: RecordEntity)

    @Delete
    suspend fun delete(entity: RecordEntity)

    @Query("SELECT * FROM record order by id desc")
    fun getAll(): Flow<List<RecordEntity>>

    @Query(
        "SELECT " +
                "date(datetime(time / 1000, 'unixepoch')) AS date," +
                "COUNT(*) AS numOfSales," +
                "SUM(total) AS totalSum," +
                "SUM(fareSales) AS fareSalesSum," +
                "SUM(goodsSales) AS goodsSalesSum," +
                "SUM(adult) AS adultSum," +
                "SUM(child) AS childSum," +
                "SUM(adult) + SUM(child) AS numOfPerson" +
                " FROM Record GROUP BY date ORDER BY date DESC"
    )
    fun getSummary(): Flow<List<Summary>>


    @Query("SELECT * FROM Record WHERE DATE(DATETIME(time / 1000, 'unixepoch')) = :date ORDER BY id desc")
    suspend fun getDiaryData(date: String): List<Record>


    @Query("SELECT * FROM record WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): RecordEntity?


    data class Summary(
        val date: String,
        val numOfSales: Int,
        val totalSum: Int,
        val fareSalesSum: Int,
        val goodsSalesSum: Int,
        val adultSum: Int,
        val childSum: Int,
        val numOfPerson: Int,
    )
}