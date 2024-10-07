package com.joseruiz.api_exercise.dao

import androidx.room.*
import com.joseruiz.api_exercise.data.MealRecipe
import kotlinx.coroutines.flow.Flow


// @Dao indica que esta interfaz contiene métodos de acceso a la base de datos

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipes: List<MealRecipe>)

    @Query("SELECT * FROM MealRecipe WHERE idMeal = :idMeal")
    fun getAllRecipe(idMeal: String): Flow<List<MealRecipe>>
}