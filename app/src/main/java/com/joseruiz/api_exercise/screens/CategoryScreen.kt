package com.joseruiz.api_exercise.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.joseruiz.api_exercise.api.ApiService
import com.joseruiz.api_exercise.api.MainViewModel
import com.joseruiz.api_exercise.api.checkForInternet
import com.joseruiz.api_exercise.api.recipeService
import com.joseruiz.api_exercise.data.AppDatabase
import com.joseruiz.api_exercise.data.Category
import com.joseruiz.api_exercise.data.CategoryDao

@Composable
fun CategoryScreen(modifier: Modifier = Modifier, navController: NavController) {
    val context = LocalContext.current
    val categoryDao: CategoryDao = AppDatabase.getDatabase(context).categoryDao()
    val apiService = recipeService

    // Inicializar el ViewModel directamente
    val categoryViewModel: MainViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MainViewModel(categoryDao, apiService, context) as T
            }
        }
    )

    // Recolectar el estado del ViewModel usando la función collectAsState()
    val viewState by categoryViewModel.categoriesState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            viewState.loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            viewState.error != null -> {
                Text("ERROR OCURRED", modifier = Modifier.align(Alignment.Center))
            }
            else -> {
                CategoryScreen(categories = viewState.list, navController)
            }
        }
    }
}


@Composable
fun CategoryScreen(categories: List<Category>, navController: NavController){
    LazyVerticalGrid(GridCells.Fixed(2), modifier = Modifier.fillMaxSize()) {
        items(categories){
                category -> CategoryItem(category = category, navController)
        }
    }
}

@Composable
fun CategoryItem(category: Category, navController: NavController){
    Column (
        modifier = Modifier.padding(8.dp).fillMaxSize()
        , horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            painter = rememberAsyncImagePainter(category.strCategoryThumb)
            , contentDescription = null
            , modifier = Modifier
                .fillMaxSize()
                .aspectRatio(1f)
                .clickable {
                    // Aquí se debería de mandar a llamar el navController
                    navController.navigate("meal/${category.strCategory}")
                }
        )

        Text(
            text = category.strCategory
            , color = Color.Black
            , style = TextStyle(fontWeight = FontWeight.Bold)
            , modifier = Modifier.padding(top=4.dp)
        )
    }
}