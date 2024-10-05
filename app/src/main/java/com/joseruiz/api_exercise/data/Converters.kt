package com.joseruiz.api_exercise.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromCategoryList(categories: List<Category>?): String {
        return Gson().toJson(categories)
    }

    @TypeConverter
    fun toCategoryList(categoriesString: String): List<Category> {
        val listType = object : TypeToken<List<Category>>() {}.type
        return Gson().fromJson(categoriesString, listType)
    }
}

