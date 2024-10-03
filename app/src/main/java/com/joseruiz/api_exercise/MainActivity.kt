package com.joseruiz.api_exercise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.joseruiz.api_exercise.ui.theme.API_EXERCISETheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            API_EXERCISETheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "category") {
                        composable(route = "category"){
                            CategoryScreen(modifier = Modifier.padding(innerPadding), navController)
                        }
                        composable(route = "meal/{categoryName}") { backStackEntry ->
                            val categoryName = backStackEntry.arguments?.getString("categoryName")
                            MealScreen(categoryName = categoryName, navController = navController)
                        }
                        composable(route = "recipe/{idMeal}") { backStackEntry ->
                            val idMeal = backStackEntry.arguments?.getString("idMeal")
                            RecipeScreen(idMeal = idMeal)
                        }
                    }
                }
            }
        }
    }
}