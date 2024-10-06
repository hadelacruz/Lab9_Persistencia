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
            // Obtén las comidas de la API
            val mealsFromApi = apiService.getMeals(categoryName).meals

            // Mapea la lista a una nueva lista de 'Meal' con la categoría establecida
            val mealsWithCategory = mealsFromApi.map { meal ->
                Meal(
                    strMeal = meal.strMeal,
                    strMealThumb = meal.strMealThumb,
                    idMeal = meal.idMeal,
                    category = categoryName
                )
            }

            // Inserta la nueva lista en la base de datos
            mealDao.insertMeal(mealsWithCategory)

            // Emitir la lista de categorías obtenida de la API
            emit(mealsFromApi)
        } else {
            Log.i("No hay internet", "no hay internet")
            // Si no hay internet, obtener de la base de datos local
            mealDao.getAllMeals(categoryName).collect { mealsFromDb ->
                emit(mealsFromDb)
            }
        }
    }
}