package com.example.bakingapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.bakingapp.model.Ingredient;
import com.example.bakingapp.model.Recipe;

import java.util.List;

@Dao
public interface IngredientDao {

    @Query("SELECT * FROM ingredient WHERE recipe_id = :recipeId")
    LiveData<List<Ingredient>> loadIngredientsByRecipeId(int recipeId);

    @Query("SELECT * FROM ingredient")
    LiveData<List<Ingredient>> loadAllIngredients();

    @Insert
    void insertIngredient(Ingredient ingredient);
}
