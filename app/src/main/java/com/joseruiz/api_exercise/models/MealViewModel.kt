package com.joseruiz.api_exercise.models

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseruiz.api_exercise.api.ApiService
import com.joseruiz.api_exercise.data.Meal
import com.joseruiz.api_exercise.repository.MealsRepository
import kotlinx.coroutines.launch

class MealViewModel(
    val dao: Any,
    val apiService: ApiService,
    val context: Context,
    val categoryName: String
): ViewModel()  {

    // Variables para meals
    private val _mealState = mutableStateOf(MealState())
    val mealsState: State<MealState> = _mealState

    //Llamada al context del mealRepository
    private val mealRepository = MealsRepository(dao, apiService, context)

    fun fetchMeals(category: String){
        viewModelScope.launch { // Trabaja como mi await
            mealRepository.getMeals(categoryName).collect { meals ->
                _mealState.value = _mealState.value.copy(
                    list = meals,
                    loading = false,
                    error = null
                )
            }
        }
    }

    data class MealState(
        val loading: Boolean = true
        , val list: List<Meal> = emptyList()
        , val error: String? = null
    )
}