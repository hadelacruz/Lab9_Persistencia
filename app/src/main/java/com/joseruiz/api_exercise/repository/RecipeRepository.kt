package com.joseruiz.api_exercise.repository

import android.content.Context
import android.util.Log
import com.joseruiz.api_exercise.api.ApiService
import com.joseruiz.api_exercise.api.checkForInternet
import com.joseruiz.api_exercise.dao.RecipeDao

import com.joseruiz.api_exercise.data.MealRecipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

data class RecipeRepository(
    private val dao: Any,
    private val apiService: ApiService,
    private val context: Context
){
    val recipeDao = dao as RecipeDao
    // Obteniendo las recetas ya sea de la API o de la base de datos local
    fun getRecipe(idMeal: String):  Flow<List<MealRecipe>> = flow{
        if (checkForInternet(context)) {
            Log.i("SI hay internet Recipe", "Si hay internet R3")
            // Si hay internet, obtener de la API
            val recipesFromApi = apiService.getRecipes(idMeal).meals
            Log.i("API Response", "Número de recetas obtenidas de la API: ${recipesFromApi?.size ?: 0}")


            // Mapea la lista a una nueva lista de 'Meal' con la categoría establecida
            val recipesWithMeals = recipesFromApi?.map { recipe ->
                MealRecipe(
                    idMeal = idMeal,
                    strMeal = recipe.strMeal,
                    strDrinkAlternate = recipe.strDrinkAlternate,
                    strCategory = recipe.strCategory,
                    strArea = recipe.strArea,
                    strInstructions = recipe.strInstructions,
                    strMealThumb = recipe.strMealThumb,
                    strTags = recipe.strTags,
                    strYoutube = recipe.strYoutube,
                    strIngredient1 = recipe.strIngredient1,
                    strIngredient2 = recipe.strIngredient2,
                    strIngredient3 = recipe.strIngredient3,
                    strIngredient4 = recipe.strIngredient4,
                    strIngredient5 = recipe.strIngredient5,
                    strIngredient6 = recipe.strIngredient6,
                    strIngredient7 = recipe.strIngredient7,
                    strIngredient8 = recipe.strIngredient8,
                    strIngredient9 = recipe.strIngredient9,
                    strIngredient10 = recipe.strIngredient10,
                    strIngredient11 = recipe.strIngredient11,
                    strIngredient12 = recipe.strIngredient12,
                    strIngredient13 = recipe.strIngredient13,
                    strIngredient14 = recipe.strIngredient14,
                    strIngredient15 = recipe.strIngredient15,
                    strIngredient16 = recipe.strIngredient16,
                    strIngredient17 = recipe.strIngredient17,
                    strIngredient18 = recipe.strIngredient18,
                    strIngredient19 = recipe.strIngredient19,
                    strIngredient20 = recipe.strIngredient20,
                    strMeasure1 = recipe.strMeasure1,
                    strMeasure2 = recipe.strMeasure2,
                    strMeasure3 = recipe.strMeasure3,
                    strMeasure4 = recipe.strMeasure4,
                    strMeasure5 = recipe.strMeasure5,
                    strMeasure6 = recipe.strMeasure6,
                    strMeasure7 = recipe.strMeasure7,
                    strMeasure8 = recipe.strMeasure8,
                    strMeasure9 = recipe.strMeasure9,
                    strMeasure10 = recipe.strMeasure10,
                    strMeasure11 = recipe.strMeasure11,
                    strMeasure12 = recipe.strMeasure12,
                    strMeasure13 = recipe.strMeasure13,
                    strMeasure14 = recipe.strMeasure14,
                    strMeasure15 = recipe.strMeasure15,
                    strMeasure16 = recipe.strMeasure16,
                    strMeasure17 = recipe.strMeasure17,
                    strMeasure18 = recipe.strMeasure18,
                    strMeasure19 = recipe.strMeasure19,
                    strMeasure20 = recipe.strMeasure20
                )

            }

            // Insertar en la base de datos local solo si no es nulo
            recipesWithMeals?.let {
                // Asegúrate de usar una corrutina para operaciones suspendida
                    recipeDao.insertRecipe(it)
                    Log.i("Database", "Recetas insertadas en la base de datos local: ${it.size}")

            }

            // Emitir la lista de recetas obtenida de la API
            emit(recipesWithMeals ?: emptyList())
        } else {
            Log.i("No hay internet", "no hay internet")
            // Si no hay internet, obtener de la base de datos local
            recipeDao.getAllRecipe(idMeal).collect { recipesFromApi ->
                Log.i("Database", "Recetas recuperadas de la base de datos local: ${recipesFromApi.size}")
                emit(recipesFromApi)
            }
        }
    }

}