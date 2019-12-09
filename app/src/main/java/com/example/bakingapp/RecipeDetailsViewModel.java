package com.example.bakingapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.bakingapp.database.AppDatabase;
import com.example.bakingapp.model.Ingredient;
import com.example.bakingapp.model.Recipe;
import com.example.bakingapp.model.Step;
import java.util.List;

public class RecipeDetailsViewModel extends ViewModel {

    // Constant for logging
    private static final String TAG = RecipeDetailsViewModel.class.getSimpleName();

    private LiveData<List<Ingredient>> ingredients;
    private LiveData<List<Step>> steps;
    private LiveData<Recipe> recipe;


    public RecipeDetailsViewModel(AppDatabase database, int recipeId) {
        steps = database.stepDao().loadStepByRecipeId(recipeId);
        ingredients = database.ingredientDao().loadIngredientsByRecipeId(recipeId);
        recipe = database.recipeDao().loadRecipeById(recipeId);
    }

    public LiveData<List<Ingredient>> getIngredients() {
        return ingredients;
    }

    public LiveData<List<Step>> getSteps() {
        return steps;
    }

    public LiveData<Recipe> getRecipe(){
        return recipe;
    }
}