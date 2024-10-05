package com.joseruiz.api_exercise.data

import android.content.Context
import com.joseruiz.api_exercise.api.ApiService
import com.joseruiz.api_exercise.api.checkForInternet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CategoryRepository(
    private val categoryDao: CategoryDao,
    private val apiService: ApiService,
    private val context: Context
) {
    // Obteniendo categorías ya sea de la API o de la base de datos local
    fun getCategories(): Flow<List<Category>> = flow {
        if (checkForInternet(context)) {
            // Si hay internet, obtener de la API
            val categoriesFromApi = apiService.getCategories().categories
            // Insertar en la base de datos local
            categoryDao.insertCategory(categoriesFromApi)  // Asegúrate de que esto sea suspend
            // Emitir la lista de categorías obtenida de la API
            emit(categoriesFromApi)
        } else {
            // Si no hay internet, obtener de la base de datos local
            categoryDao.getAllCategories().collect { categoriesFromDb ->
                emit(categoriesFromDb)
            }
        }
    }
}


