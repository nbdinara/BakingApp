package com.example.bakingapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.bakingapp.model.Step;

import java.util.List;

@Dao
public interface StepDao {

    @Query("SELECT * FROM step WHERE recipe_id = :recipeId")
    LiveData<List<Step>>loadStepByRecipeId(int recipeId);

    @Query("SELECT * FROM step WHERE recipe_id = :recipeId AND id = :stepId")
    LiveData<Step>loadStepByRecipeIdandStepId(int recipeId, int stepId);

    @Query("SELECT * FROM step")
    LiveData<Step>loadAllSteps();

    @Insert
    void insertStep(Step step);
}
