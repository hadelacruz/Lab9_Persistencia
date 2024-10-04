package com.joseruiz.api_exercise.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

// @Dao indica que esta interfaz contiene métodos de acceso a la base de datos

@Dao
interface CategoryDao {
    @Query("SELECT * FROM Category")
    suspend fun getAllCategories(): List<Category>

    @Insert
    suspend fun insertCategory(category: Category)
}