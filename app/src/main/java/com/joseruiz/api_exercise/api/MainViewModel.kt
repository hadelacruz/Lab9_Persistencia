package com.joseruiz.api_exercise.api

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import android.util.Log
import com.joseruiz.api_exercise.data.Category
import com.joseruiz.api_exercise.data.CategoryDao
import com.joseruiz.api_exercise.data.CategoryRepository
import com.joseruiz.api_exercise.data.Meal
import com.joseruiz.api_exercise.data.MealRecipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel(
    val categoryDao: CategoryDao,
    val apiService: ApiService,
    val context: Context
): ViewModel() {
    // Variables para categories
    private val _categoriesState = MutableStateFlow(CategoryState())
    val categoriesState: StateFlow<CategoryState> = _categoriesState.asStateFlow()

    // Variables para meals
    private val _mealState = mutableStateOf(MealState())
    val mealsState: State<MealState> = _mealState

    // Variables para recipes
    private val _recipeState = mutableStateOf(RecipeState())
    val recipesState: State<RecipeState> = _recipeState

    //Llamada al context del categoryRepository
    private val categoryRepository = CategoryRepository(categoryDao, apiService, context)

    init {
        fetchCategories()
    }

    public fun fetchCategories() {
        viewModelScope.launch {
            categoryRepository.getCategories().collect { categories ->
                _categoriesState.value = _categoriesState.value.copy(
                    list = categories,
                    loading = false,
                    error = null
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