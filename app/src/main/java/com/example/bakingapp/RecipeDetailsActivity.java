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
    private ArrayList<Step> mSteps  = new ArrayList<>();

    private AppDatabase mDb;
    int id;
    int recipeId;
    MasterListFragment masterListFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            mRecipe = intentThatStartedThisActivity.getParcelableExtra("recipe");
            id = intentThatStartedThisActivity.getIntExtra("recipe_id", -1);
            Log.d(TAG, "intent send: " + mRecipe.getName() + mRecipe.getRecipe_id() + "///" + id);
            masterListFragment = new MasterListFragment();
            masterListFragment.setData(mRecipe);
        }



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


//        Log.d(TAG, "onCreatechto: " + mIngredients.size());
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



}
