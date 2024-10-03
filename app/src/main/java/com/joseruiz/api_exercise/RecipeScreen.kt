package com.joseruiz.api_exercise

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import coil.compose.rememberAsyncImagePainter

@Composable
fun RecipeScreen(idMeal: String?, modifier: Modifier = Modifier) {
    // Se inicializa el ViewModel
    val recipeViewModel: MainViewModel = viewModel()

    if (idMeal != null) {
        recipeViewModel.onRecipeClick(idMeal)
    }

    val viewState by recipeViewModel.recipesState // Asegúrate de usar recipeState

    Box(modifier = modifier.fillMaxSize()) {
        when {
            viewState.loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            viewState.error != null -> {
                Text("ERROR OCCURRED RECIPE: ${viewState.error}", modifier = Modifier.align(Alignment.Center))
            }
            viewState.list.isNullOrEmpty() -> { // Manejo de lista nula o vacía
                Text("No recipes found.", modifier = Modifier.align(Alignment.Center))
            }
            else -> {
                RecipeListScreen(recipes = viewState.list)
            }
        }
    }
}

@Composable
fun RecipeListScreen(recipes: List<MealRecipe>) {
    // Mostrar las recetas en una cuadrícula vertical
    LazyVerticalGrid(GridCells.Fixed(1), modifier = Modifier.fillMaxSize()) {
        items(recipes) { recipe ->
            RecipeItem(recipe = recipe)
        }
    }
}

@Composable
fun RecipeItem(recipe: MealRecipe) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Mostrar la imagen del platillo
        Image(
            painter = rememberAsyncImagePainter(recipe.strMealThumb),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(1f)
        )

        // Nombre del platillo
        Text(
            text = recipe.strMeal ?: "Unknown Meal",
            color = Color.Black,
            style = TextStyle(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(top = 4.dp)
        )

        // Categoría, área y otras propiedades del platillo
        Text(text = "Category: ${recipe.strCategory ?: "Unknown"}")
        Text(text = "Area: ${recipe.strArea ?: "Unknown"}")

        Spacer(modifier = Modifier.height(8.dp))

        // Instrucciones
        Text(
            text = "Instructions:",
            style = TextStyle(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(top = 4.dp)
        )
        Text(text = recipe.strInstructions ?: "No instructions available.")

        Spacer(modifier = Modifier.height(8.dp))

        // Ingredientes y medidas
        Text(
            text = "Ingredients:",
            style = TextStyle(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(top = 4.dp)
        )

        // Usar la función para obtener la lista de ingredientes
        val ingredients = recipe.toIngredientList()
        ingredients.forEach { ingredient ->
            Text(text = "${ingredient.name}: ${ingredient.measure}")
        }
    }
}
