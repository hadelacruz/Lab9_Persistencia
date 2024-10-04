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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.joseruiz.api_exercise.api.MainViewModel
import com.joseruiz.api_exercise.api.checkForInternet
import com.joseruiz.api_exercise.data.Category

@Composable
fun CategoryScreen(modifier: Modifier = Modifier, navController: NavController){
    // Se inicializa el componente
    val categoryViewModel: MainViewModel = viewModel()
    // Se realiza la llamada al metodo que trae las categorias
    val context = LocalContext.current
    categoryViewModel.fetchCategories(context)

    val viewstate by categoryViewModel.categoriesState

    Box(modifier = Modifier.fillMaxSize()){
        when{
            viewstate.loading -> {
                CircularProgressIndicator(modifier.align(Alignment.Center))
            }

            viewstate.error != null ->{
                Text("ERROR OCURRED")
            }

            else -> {
                CategoryScreen(categories = viewstate.list, navController)
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