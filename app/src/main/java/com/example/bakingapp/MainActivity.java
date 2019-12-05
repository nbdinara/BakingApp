package com.example.bakingapp;

import androidx.annotation.Dimension;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bakingapp.model.Recipe;
import com.example.bakingapp.utilities.JsonUtils;

import java.util.ArrayList;
import static android.content.ContentValues.TAG;


public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapterOnClickHandler{


    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;
    private RecipeAdapter mRecipeAdapter;
    ArrayList<Recipe> mRecipes;
    private int NUMBER_OF_COLUMNS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_recipes);
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = findViewById(R.id.pb_loading_all_recipes_indicator);

        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            GridLayoutManager layoutManager
                    = new GridLayoutManager(this, NUMBER_OF_COLUMNS);
            mRecyclerView.setLayoutManager(layoutManager);

        } else {

            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

            mRecyclerView.setLayoutManager(layoutManager);
        }

        mRecipeAdapter = new RecipeAdapter(this);
        mRecyclerView.setAdapter(mRecipeAdapter);

        loadRecipesData();

    }

    public void loadRecipesData(){
        mRecipes = JsonUtils.getRecipesArrayFromJson(getApplicationContext());
        Log.d(TAG, "recipes array size: " + mRecipes.size());
        if (mRecipes.size() != 0){
            showRecipesData();
            mRecipeAdapter.setRecipesData(mRecipes);
        } else {
            showErrorMessage();
        }

    }

    public void showLoadingIndicator(){
        mRecyclerView.setVisibility(View.GONE);
        mErrorMessageDisplay.setVisibility(View.GONE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    public void showErrorMessage(){
        mRecyclerView.setVisibility(View.GONE);
        mLoadingIndicator.setVisibility(View.GONE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public void showRecipesData() {
        mErrorMessageDisplay.setVisibility(View.GONE);
        mLoadingIndicator.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt("position", mRecyclerView.getScrollState()); // get current recycle view position here.
        //your other code
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    public void onClick(Recipe recipe) {
        Context context = this;
        Class destinationClass = RecipeDetailsActivity.class;
        Intent intentToStartRecipeDetailActivity = new Intent(context, destinationClass);
        //Log.d(TAG, "myrecipe name: " + recipe.getName());
        //Log.d(TAG, "myrecipe step 3: " + recipe.getSteps().get(2).getDescription());
        intentToStartRecipeDetailActivity.putExtra("recipe", recipe);
        startActivity(intentToStartRecipeDetailActivity);
    }
}
