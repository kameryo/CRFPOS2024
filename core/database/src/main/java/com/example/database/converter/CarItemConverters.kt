package com.example.database.converter

import androidx.room.TypeConverter
import com.example.model.CartItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CarItemConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromList(selectedList: List<CartItem>): String {
        return gson.toJson(selectedList)
    }

    @TypeConverter
    fun toList(selectedListString: String): List<CartItem> {
        val listType = object : TypeToken<List<CartItem>>() {}.type
        return gson.fromJson(selectedListString, listType)
    }
}