package com.joseruiz.api_exercise.api

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import android.util.Log
import com.joseruiz.api_exercise.data.AppDatabase
import com.joseruiz.api_exercise.data.CategoriesResponse
import com.joseruiz.api_exercise.data.Category
import com.joseruiz.api_exercise.data.Meal
import com.joseruiz.api_exercise.data.MealRecipe


class MainViewModel: ViewModel() {
    // Variables para categories
    private val _categorieState = mutableStateOf(CategoryState())
    val categoriesState: State<CategoryState> = _categorieState

    // Variables para meals
    private val _mealState = mutableStateOf(MealState())
    val mealsState: State<MealState> = _mealState

    // Variables para recipes
    private val _recipeState = mutableStateOf(RecipeState())
    val recipesState: State<RecipeState> = _recipeState

    public fun fetchCategories(context: Context) {
        val database = AppDatabase.getDatabase(context)
        val categoryDao = database.categoryDao()

        viewModelScope.launch {
            try {
                val response: CategoriesResponse

                if (checkForInternet(context)) {
                    Log.i("SI HAY INTERNET", "SI HAY INTERNET")
                    // Obtener los datos desde la API
                    response = recipeService.getCategories()
                    // Insertar las categorías en la base de datos (solamente la lista de categorías)
                    categoryDao.insertCategory(response.categories)
                } else {
                    Log.i("NO HAY INTERNET", "NO HAY INTERNET")
                    // Obtener las categorías desde la base de datos
                    val categoriesFromDb = categoryDao.getAllCategories()
                    // Convertir la lista a CategoriesResponse
                    response = CategoriesResponse(categoriesFromDb)
                }

                _categorieState.value = _categorieState.value.copy(
                    list = response.categories,
                    loading = false,
                    error = null
                )
            } catch (e: Exception) {
                _categorieState.value = _categorieState.value.copy(
                    loading = false,
                    error = "Error fetching Categories ${e.message}"
                )
            }
        }
    }



    data class CategoryState(
        val loading: Boolean = true
        , val list: List<Category> = emptyList()
        , val error: String? = null
    )

    private fun fetchMeals(category: String){
        viewModelScope.launch { // Trabaja como mi await
            try{
                val response = recipeService.getMeals(category)
                _mealState.value = _mealState.value.copy(
                    list = response.meals,
                    loading = false,
                    error = null
                )
            }catch (e: Exception){
                _mealState.value = _mealState.value.copy(
                    loading = false
                    , error = "Error fetching Meals ${e.message}"
                )
            }
        }
    }

    fun onCategoryClick(category: String) {
        fetchMeals(category) // Llama al método privado
    }

    data class MealState(
        val loading: Boolean = true
        , val list: List<Meal> = emptyList()
        , val error: String? = null
    )

    private fun fetchRecipes(idMeal: String) {
        viewModelScope.launch {
            try {
                val response = recipeService.getRecipes(idMeal) // Usa idMeal en vez de "52772"
                Log.i("IDMEAL*************", response.toString())

                // Aquí asumiendo que solo hay una receta y que estás buscando en meals
                val meals = response.meals ?: emptyList() // Maneja el caso de null

                _recipeState.value = _recipeState.value.copy(
                    list = meals, // Asigna la lista de meals a tu estado
                    loading = false,
                    error = null
                )
            } catch (e: Exception) {
                _recipeState.value = _recipeState.value.copy(
                    loading = false,
                    error = "Error fetching Meals: ${e.message}"
                )
            }
        }
    }

    fun onRecipeClick(idMeal: String) {
        fetchRecipes(idMeal) // Llama al método privado
    }

    data class RecipeState(
        val loading: Boolean = true,
        val list: List<MealRecipe> = emptyList(), // Cambiado a MealRecipe
        val error: String? = null
    )

}