package com.joseruiz.api_exercise.models

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseruiz.api_exercise.api.ApiService
import com.joseruiz.api_exercise.api.recipeService
import com.joseruiz.api_exercise.data.MealRecipe
import com.joseruiz.api_exercise.repository.RecipeRepository
import kotlinx.coroutines.launch

data class RecipeViewModel(
    val dao: Any,
    val apiService: ApiService,
    val context: Context,
    val idMeal: String
): ViewModel(){
    // Variables para recipes
    private val _recipeState = mutableStateOf(RecipeState())
    val recipesState: State<RecipeState> = _recipeState

    //Llamada al context del RecipeRepository
    private val recipeRepository =RecipeRepository(dao, apiService, context)


    fun fetchRecipes(idMeal: String) {
        viewModelScope.launch {
            recipeRepository.getRecipe(idMeal).collect { recipes ->
                try {
                    _recipeState.value = _recipeState.value.copy(
                        list = recipes, // Asigna la lista de recipes obtenidos del repositorio
                        loading = false,
                        error = null
                    )
                } catch (e: Exception) {
                    _recipeState.value = _recipeState.value.copy(
                        loading = false,
                        error = "Error fetching Recipes: ${e.message}"
                    )
                }
            }
        }
    }

    fun onRecipeClick(idMeal: String) {
        fetchRecipes(idMeal) // Llama al método para obtener recetas
    }

    data class RecipeState(
        val loading: Boolean = true,
        val list: List<MealRecipe> = emptyList(), // Cambiado a MealRecipe
        val error: String? = null
    )

}
