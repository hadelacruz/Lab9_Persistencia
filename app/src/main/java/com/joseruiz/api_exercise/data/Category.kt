package com.joseruiz.api_exercise.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Nos va servir como modelos de categorias de las recetas
@Entity(tableName = "Category")
data class Category(
    @PrimaryKey val idCategory: String,
    val strCategory: String,
    val strCategoryThumb: String,
    val strCategoryDescription: String
)

data class CategoriesResponse(
    val categories: List<Category>
)