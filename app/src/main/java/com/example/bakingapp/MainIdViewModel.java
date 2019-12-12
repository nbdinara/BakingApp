package com.example.bakingapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.bakingapp.database.AppDatabase;
import com.example.bakingapp.model.Recipe;


public class MainIdViewModel extends ViewModel {

    // Constant for logging
    private static final String TAG = MainIdViewModel.class.getSimpleName();

    private LiveData<Recipe> recipe;


    public MainIdViewModel(AppDatabase database, int recipeId) {
        recipe = database.recipeDao().loadRecipeById(recipeId);
    }
    public LiveData<Recipe> getRecipe(){
        return recipe;
    }
}