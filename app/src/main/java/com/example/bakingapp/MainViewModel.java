package com.example.bakingapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.bakingapp.database.AppDatabase;
import com.example.bakingapp.model.Recipe;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<Recipe>> recipes;


    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        recipes = database.recipeDao().loadAllRecipes();
    }

    public LiveData<List<Recipe>> getRecipes(){
        return recipes;
    }
}
