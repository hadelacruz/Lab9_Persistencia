package com.joseruiz.api_exercise.repository

import android.content.Context
import android.util.Log
import com.joseruiz.api_exercise.api.ApiService
import com.joseruiz.api_exercise.api.checkForInternet
import com.joseruiz.api_exercise.data.Meal
import com.joseruiz.api_exercise.dao.MealDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MealsRepository(
    private val dao: Any,
    private val apiService: ApiService,
    private val context: Context
) {
    val mealDao = dao as MealDao
    // Obteniendo categorías ya sea de la API o de la base de datos local
    fun getMeals(categoryName: String): Flow<List<Meal>> = flow {
        if (checkForInternet(context)) {
            Log.i("SI hay internet", "Si hay internet")
            // Si hay internet, obtener de la API
            val mealsFromApi = apiService.getMeals(categoryName).meals
            // Insertar en la base de datos local
            mealDao.insertMeal(mealsFromApi)  // Asegúrate de que esto sea suspend
            // Emitir la lista de categorías obtenida de la API
            emit(mealsFromApi)
        } else {
            Log.i("No hay internet", "no hay internet")
            // Si no hay internet, obtener de la base de datos local
            mealDao.getAllMeals().collect { mealsFromDb ->
                emit(mealsFromDb)
            }
        }
    }
}