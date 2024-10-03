package com.joseruiz.api_exercise

// Nos va servir como modelos de categorias de las recetas
data class Category(
    val idCategory:String,
    val strCategory: String,
    val strCategoryThumb: String,
    val strCategoryDescription: String
)

data class CategoriesResponse(
    val categories: List<Category>
)