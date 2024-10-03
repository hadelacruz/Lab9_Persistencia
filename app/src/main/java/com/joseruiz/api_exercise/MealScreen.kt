package com.joseruiz.api_exercise

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter

@Composable
fun MealScreen(categoryName: String?, modifier: Modifier = Modifier, navController: NavController) {
    val mealViewModel: MainViewModel = viewModel()

    // Llama a fetchMeals si categoryName no es nulo
    if (categoryName != null) {
        mealViewModel.onCategoryClick(categoryName)
    }

    val viewstate by mealViewModel.mealsState

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            viewstate.loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            viewstate.error != null -> {
                Text("ERROR OCURRED")
            }
            else -> {
                MealScreen(meals = viewstate.list, navController)
            }
        }
    }
}

@Composable
fun MealScreen(meals: List<Meal>, navController: NavController){ //List<Recipe>
    LazyVerticalGrid(GridCells.Fixed(2), modifier = Modifier.fillMaxSize()) {
        items(meals){
            meal -> MealItem(meal = meal, navController)
        }
    }
}

@Composable
fun MealItem(meal: Meal, navController: NavController){
    Column (
        modifier = Modifier.padding(8.dp).fillMaxSize()
        , horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            painter = rememberAsyncImagePainter(meal.strMealThumb)
            , contentDescription = null
            , modifier = Modifier
                .fillMaxSize()
                .aspectRatio(1f)
                .clickable {
                    navController.navigate("recipe/${meal.idMeal}")
                }
        )

        Text(
            text = meal.strMeal
            , color = Color.Black
            , style = TextStyle(fontWeight = FontWeight.Bold)
            , modifier = Modifier.padding(top=4.dp)
        )
    }
}