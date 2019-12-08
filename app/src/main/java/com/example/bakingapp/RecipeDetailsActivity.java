package com.example.bakingapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.bakingapp.database.AppDatabase;
import com.example.bakingapp.model.Ingredient;
import com.example.bakingapp.model.Recipe;
import com.example.bakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class RecipeDetailsActivity extends AppCompatActivity implements MasterListFragment.OnImageClickListener{

    private Recipe mRecipe;
    private boolean mTwoPane;
    private ArrayList<Ingredient> mIngredients;
    private ArrayList<Step> mSteps;

    private AppDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);



        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            mRecipe = intentThatStartedThisActivity.getParcelableExtra("recipe");
        }

        loadStepsFromDb(mRecipe.getRecipeId());
        loadIngredientsFromDb(mRecipe.getRecipeId());


        if (findViewById(R.id.divider)!=null){
            mTwoPane = true;
            if (savedInstanceState == null) {
                StepDetailsFragment stepFragment = new StepDetailsFragment();
                stepFragment.setSteps(mSteps);
                stepFragment.setId(0);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().add(R.id.step_container, stepFragment).commit();
            }
        } else {
            mTwoPane = false;
        }

        Log.d(TAG, "mRecipe steps: " + mSteps.size());

        MasterListFragment masterListFragment = new MasterListFragment();
        masterListFragment.setData(mRecipe, mIngredients, mSteps);
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.recipe_container, masterListFragment)
                .commit();

    }


    public void onImageSelected (int position){
        if (mTwoPane){
            StepDetailsFragment stepFragment = new StepDetailsFragment();
            stepFragment.setSteps(mSteps); //TTTUUUUUt
            stepFragment.setId(position);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.step_container, stepFragment).commit();
        } else {
            Bundle b = new Bundle();
            b.putParcelableArrayList("steps", mSteps);
            b.putInt("recipe_id", position);
            Context context = this;
            Class destinationClass = StepDetailsActivity.class;
            final Intent intentToStartStepDetailActivity = new Intent(context, destinationClass);
            intentToStartStepDetailActivity.putExtras(b);
            startActivity(intentToStartStepDetailActivity);
        }
    }

    public void loadIngredientsFromDb(int recipe_id){
        mIngredients = new ArrayList<>();
        RecipeDetailViewModelFactory factory = new RecipeDetailViewModelFactory(mDb, recipe_id);
        final RecipeDetailsViewModel viewModel
                = ViewModelProviders.of(this, factory).get(RecipeDetailsViewModel.class);
        Log.d(TAG, "I am here");
        viewModel.getIngredients().observe(this, new Observer <List<Ingredient>>() {
            @Override
            public void onChanged(@Nullable final List<Ingredient> ingredients) {
                mIngredients = (ArrayList<Ingredient>)ingredients;
            }
        });


    }

    public void loadStepsFromDb(int recipe_id){
        mSteps = new ArrayList<>();
        RecipeDetailViewModelFactory factory = new RecipeDetailViewModelFactory(mDb, recipe_id);
        final RecipeDetailsViewModel viewModel
                = ViewModelProviders.of(this, factory).get(RecipeDetailsViewModel.class);
        Log.d(TAG, "I am here");
        viewModel.getSteps().observe(this, new Observer<List<Step>>() {
            @Override
            public void onChanged(@Nullable final List<Step> steps) {
                mSteps = (ArrayList<Step>) steps;
            }
        });
    }


}
