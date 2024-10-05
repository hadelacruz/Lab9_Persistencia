package com.joseruiz.api_exercise.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// @Dao indica que esta interfaz contiene métodos de acceso a la base de datos

@Dao
interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(categories: List<Meal>)

    @Query("SELECT * FROM Meal")
    fun getAllMeals(): Flow<List<Meal>>
}