package com.example.bakingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.bakingapp.model.Recipe;
import com.example.bakingapp.model.Step;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class RecipeDetailsActivity extends AppCompatActivity implements MasterListFragment.OnImageClickListener{

    private Recipe mRecipe;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);



        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            mRecipe = intentThatStartedThisActivity.getParcelableExtra("recipe");
        }

        if (findViewById(R.id.divider)!=null){
            mTwoPane = true;
            if (savedInstanceState == null) {
                StepDetailsFragment stepFragment = new StepDetailsFragment();
                stepFragment.setSteps(mRecipe.getSteps());
                stepFragment.setId(0);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().add(R.id.step_container, stepFragment).commit();
            }
        } else {
            mTwoPane = false;
        }

        Log.d(TAG, "mRecipe steps: " + mRecipe.getSteps().size());

        MasterListFragment masterListFragment = new MasterListFragment();
        masterListFragment.setRecipe(mRecipe);
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.recipe_container, masterListFragment)
                .commit();



    }

    public void onImageSelected (int position){
        if (mTwoPane){
            StepDetailsFragment stepFragment = new StepDetailsFragment();
            stepFragment.setSteps(mRecipe.getSteps());
            stepFragment.setId(position);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.step_container, stepFragment).commit();
        } else {
            Bundle b = new Bundle();
            b.putParcelableArrayList("steps", mRecipe.getSteps());
            b.putInt("id", position);
            Context context = this;
            Class destinationClass = StepDetailsActivity.class;
            final Intent intentToStartStepDetailActivity = new Intent(context, destinationClass);
            intentToStartStepDetailActivity.putExtras(b);
            startActivity(intentToStartStepDetailActivity);
        }
    }
}
