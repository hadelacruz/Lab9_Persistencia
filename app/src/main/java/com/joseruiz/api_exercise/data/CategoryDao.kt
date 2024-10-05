package com.joseruiz.api_exercise.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns

// @Dao indica que esta interfaz contiene métodos de acceso a la base de datos

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(categories: List<Category>)

    @Query("SELECT * FROM Category")
    suspend fun getAllCategories(): List<Category>
}