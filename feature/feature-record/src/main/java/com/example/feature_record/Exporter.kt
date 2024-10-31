package com.example.feature_record

import android.content.Context
import android.util.Log
import com.example.model.Record
import java.io.File
import java.io.IOException

class Exporter {
    fun writeRecordsToCSV(recordsList: List<Record>, date: String, context: Context?) {
        try {
            val fileName = "$date.csv"

            File(context?.filesDir, fileName).writer().use {

                it.write("ID,Time,Total,Fare Sales,Goods Sales,Adult,Child,\n")

                for (record in recordsList) {
                    // 各行のデータを書き込む
                    it.write(record.id.toString())
                    it.write(",")
                    it.write(record.time.toString())
                    it.write(",")
                    it.write(record.total.toString())
                    it.write(",")
                    it.write(record.fareSales.toString())
                    it.write(",")
                    it.write(record.goodsSales.toString())
                    it.write(",")
                    it.write(record.adult.toString())
                    it.write(",")
                    it.write(record.child.toString())
                    it.write(",")
                    for (goods in record.goodsList!!) {
                        it.write(goods.goods.name)
                        it.write(",")
                        it.write(goods.quantity.toString())
                        it.write(",")
                    }
                    it.write("\n")
                }

            }

            Log.d("FilesDir", "finish"+ context?.filesDir)

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}