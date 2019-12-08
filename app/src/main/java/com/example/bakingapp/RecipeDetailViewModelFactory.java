package com.example.bakingapp;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.bakingapp.database.AppDatabase;

public class RecipeDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {


    private final AppDatabase mDb;
    private final int mRecipeId;


    public RecipeDetailViewModelFactory(AppDatabase database, int recipeId) {
        mDb = database;
        mRecipeId = recipeId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new RecipeDetailsViewModel(mDb, mRecipeId);
    }

}