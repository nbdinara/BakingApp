package com.example.bakingapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.bakingapp.model.Recipe;

import java.util.List;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM recipe")
    LiveData<List<Recipe>> loadAllRecipes();

    @Query("SELECT * FROM recipe WHERE recipe_id = :id")
    LiveData<Recipe> loadRecipeById(int id);

    @Insert
    void insertRecipe(Recipe recipe);

}
