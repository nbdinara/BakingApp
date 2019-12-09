package com.example.bakingapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.bakingapp.database.AppDatabase;
import com.example.bakingapp.model.Ingredient;
import com.example.bakingapp.model.Recipe;

import java.util.List;

public class MainIdViewModel extends ViewModel {

    // Constant for logging
    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<Recipe> recipe;
    private LiveData<List<Ingredient>> ingredients;


    public MainIdViewModel(AppDatabase database, int recipeId) {
        recipe = database.recipeDao().loadRecipeById(recipeId);
        ingredients = database.ingredientDao().loadIngredientsByRecipeId(recipeId);
    }


    public LiveData<Recipe> getRecipe() {
        return recipe;
    }

    public LiveData<List<Ingredient>> getIngredients(){
        return ingredients;
    }
}

