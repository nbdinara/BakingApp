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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            mRecipe = intentThatStartedThisActivity.getParcelableExtra("recipe");
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
        //Toast.makeText(getApplicationContext(), "Id of step" + mRecipe.getSteps().get(position),
        //        Toast.LENGTH_LONG).show();
        Context context = this;
        Class destinationClass = StepDetailsActivity.class;
        Intent intentToStartStepDetailActivity = new Intent(context, destinationClass);
        intentToStartStepDetailActivity.putExtra("steps", mRecipe.getSteps());
        intentToStartStepDetailActivity.putExtra("id", position);
        startActivity(intentToStartStepDetailActivity);
    }
}
