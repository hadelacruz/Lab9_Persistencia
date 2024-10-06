package com.joseruiz.api_exercise.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.joseruiz.api_exercise.dao.CategoryDao
import com.joseruiz.api_exercise.dao.MealDao

/*
* Entidades:
*
* 1. Category
* 2. Meal
* 3. Recipe (Pero tiene llaves foraneas)
* */
@Database(entities = [Category::class, Meal::class], version = 7)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun mealDao(): MealDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "recipe_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
