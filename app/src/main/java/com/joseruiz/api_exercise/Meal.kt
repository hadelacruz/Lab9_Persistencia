package com.joseruiz.api_exercise

data class Meal(
    val strMeal: String,
    val strMealThumb: String,
    val idMeal: String
)

data class MealsResponse(
    val meals: List<Meal>
)