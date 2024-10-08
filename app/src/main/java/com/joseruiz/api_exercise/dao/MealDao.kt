package com.joseruiz.api_exercise.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.joseruiz.api_exercise.data.Meal
import kotlinx.coroutines.flow.Flow

// @Dao indica que esta interfaz contiene métodos de acceso a la base de datos

@Dao
interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(categories: List<Meal>)

    @Query("SELECT * FROM Meal WHERE category = :category")
    fun getAllMeals(category: String): Flow<List<Meal>>
}