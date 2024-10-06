package com.joseruiz.api_exercise.models

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.joseruiz.api_exercise.api.ApiService
import com.joseruiz.api_exercise.api.recipeService
import com.joseruiz.api_exercise.data.Category
import com.joseruiz.api_exercise.repository.CategoryRepository
import com.joseruiz.api_exercise.data.MealRecipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel(
    val dao: Any,
    val apiService: ApiService,
    val context: Context
): ViewModel() {
    // Variables para categories
    private val _categoriesState = MutableStateFlow(CategoryState())
    val categoriesState: StateFlow<CategoryState> = _categoriesState.asStateFlow()

    // Variables para recipes
    private val _recipeState = mutableStateOf(RecipeState())
    val recipesState: State<RecipeState> = _recipeState

    //Llamada al context del categoryRepository
    private val categoryRepository = CategoryRepository(dao, apiService, context)

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

    private fun fetchRecipes(idMeal: String) {
        viewModelScope.launch {
            try {
                val response = recipeService.getRecipes(idMeal)

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