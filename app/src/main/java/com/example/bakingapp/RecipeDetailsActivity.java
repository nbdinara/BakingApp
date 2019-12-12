package com.example.bakingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bakingapp.database.AppDatabase;
import com.example.bakingapp.model.Ingredient;
import com.example.bakingapp.model.Recipe;
import com.example.bakingapp.model.Step;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

public class RecipeDetailsActivity extends AppCompatActivity implements MasterListFragment.OnImageClickListener{

    private Recipe mRecipe;
    @Nullable
    @BindView(R.id.divider) View mDivider;
    boolean mTwoPane;
    private int mRecipeId;
    private AppDatabase mDb;
    private List<Step> mSteps;
    private List<Ingredient> mIngredients;
    private Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        ButterKnife.bind(this);

        this.savedInstanceState = savedInstanceState;
        mDb = AppDatabase.getInstance(getApplicationContext());

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            mRecipeId = intentThatStartedThisActivity.getIntExtra("recipe_id", -1);
        }

        loadRecipeById(mRecipeId);

    }

    public void loadRecipeById(int recipeId){
        RecipeDetailsViewModelFactory factory = new RecipeDetailsViewModelFactory(mDb, recipeId);
        // COMPLETED (11) Declare a AddTaskViewModel variable and initialize it by calling ViewModelProviders.of
        // for that use the factory created above AddTaskViewModel
        final RecipeDetailsViewModel viewModel
                = ViewModelProviders.of(this, factory).get(RecipeDetailsViewModel.class);

        // COMPLETED (12) Observe the LiveData object in the ViewModel. Use it also when removing the observer
        viewModel.getRecipe().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(@Nullable Recipe recipe) {
                if (recipe!=null){
                    Log.d(TAG, "recipe is not null: ");
                }
                viewModel.getRecipe().removeObserver(this);
                mRecipe = recipe;
                if (mRecipe!=null){
                    Log.d(TAG, "mRecipe is not null: " + mRecipe.getName());
                }
                loadIngredientsByRecipeId(mRecipeId);
            }
        });
    }

    public void loadIngredientsByRecipeId(int recipeId){
        mIngredients = new ArrayList<>();
        RecipeDetailsViewModelFactory factory = new RecipeDetailsViewModelFactory(mDb, recipeId);
        // COMPLETED (11) Declare a AddTaskViewModel variable and initialize it by calling ViewModelProviders.of
        // for that use the factory created above AddTaskViewModel
        final RecipeDetailsViewModel viewModel
                = ViewModelProviders.of(this, factory).get(RecipeDetailsViewModel.class);

        // COMPLETED (12) Observe the LiveData object in the ViewModel. Use it also when removing the observer
        viewModel.getIngredients().observe(this, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(@Nullable List<Ingredient> ingredients) {
                viewModel.getIngredients().removeObserver(this);
                mIngredients = ingredients;
                loadStepsByRecipeId(mRecipeId);
            }
        });
    }

    public void loadStepsByRecipeId(int recipeId){
        mSteps = new ArrayList<>();
        RecipeDetailsViewModelFactory factory = new RecipeDetailsViewModelFactory(mDb, recipeId);
        // COMPLETED (11) Declare a AddTaskViewModel variable and initialize it by calling ViewModelProviders.of
        // for that use the factory created above AddTaskViewModel
        final RecipeDetailsViewModel viewModel
                = ViewModelProviders.of(this, factory).get(RecipeDetailsViewModel.class);

        // COMPLETED (12) Observe the LiveData object in the ViewModel. Use it also when removing the observer
        viewModel.getSteps().observe(this, new Observer<List<Step>>() {
            @Override
            public void onChanged(@Nullable List<Step> steps) {
                viewModel.getSteps().removeObserver(this);
                mSteps = steps;

                if (mDivider!=null){
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

                MasterListFragment masterListFragment = new MasterListFragment();
                masterListFragment.setRecipe(mRecipe);
                masterListFragment.setSteps(mSteps);
                masterListFragment.setIngredients(mIngredients);
                FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .add(R.id.recipe_container, masterListFragment)
                        .commit();
            }

        });
    }


    public void onImageSelected (int position){
        if (mTwoPane){
            StepDetailsFragment stepFragment = new StepDetailsFragment();
            stepFragment.setSteps(mSteps);
            stepFragment.setId(position);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.step_container, stepFragment).commit();
        } else {
            Bundle b = new Bundle();
            b.putParcelableArrayList("steps", (ArrayList<? extends Parcelable>) mSteps);
            b.putInt("id", position);
            Context context = this;
            Class destinationClass = StepDetailsActivity.class;
            final Intent intentToStartStepDetailActivity = new Intent(context, destinationClass);
            intentToStartStepDetailActivity.putExtras(b);
            startActivity(intentToStartStepDetailActivity);
        }
    }
}
