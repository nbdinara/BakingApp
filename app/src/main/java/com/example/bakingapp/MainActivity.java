package com.example.bakingapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bakingapp.database.AppDatabase;
import com.example.bakingapp.model.Ingredient;
import com.example.bakingapp.model.Recipe;
import com.example.bakingapp.model.Step;
import com.example.bakingapp.utilities.JsonUtils;
import com.example.bakingapp.utilities.NetworkUtils;

import java.net.URL;
import java.util.List;

import static android.content.ContentValues.TAG;


public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapterOnClickHandler{


    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;
    private RecipeAdapter mRecipeAdapter;
    List<Recipe> mRecipes;
    private int NUMBER_OF_COLUMNS = 3;
    private AppDatabase mDb;

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

        mDb = AppDatabase.getInstance(getApplicationContext());

        loadRecipesData();
        loadIngredients();
        loadRecipes();
        loadIngredientsById();
    }

    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                Log.d(TAG, "Updating list of tasks from LiveData in ViewModel");
                if (recipes.size()!= 0) {
                    showRecipesData();
                    mRecipeAdapter.setRecipesData(recipes);
                    Log.d(TAG, "setUpVIewModel: " + recipes.get(0).getName());

                }else{
                    showErrorMessage();
                }
            }
        });
    }

    public void loadRecipesData(){
        //mRecipes = JsonUtils.getRecipesArrayFromJson(getApplicationContext());
        if(isOnline()) {
            new FetchRecipesTask().execute();
        } else {
           setupViewModel();
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
        intentToStartRecipeDetailActivity.putExtra("recipe_id", recipe.getRecipe_id());
        Log.d(TAG, "onClick: intentMain" + recipe.getRecipe_id() + recipe.getName());
        startActivity(intentToStartRecipeDetailActivity);

    }


    class FetchRecipesTask extends AsyncTask<Void, Void, List<Recipe>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoadingIndicator();
        }

        @Override
        protected List<Recipe> doInBackground(Void... params) {


            URL recipesRequestUrl = NetworkUtils.buildUrl();

            try {
                String jsonRecipesResponse = NetworkUtils
                        .getResponseFromHttpUrl(recipesRequestUrl);

                mRecipes =  JsonUtils
                        .getRecipesArrayFromJson(jsonRecipesResponse);

                Log.d(TAG, "Simple json array size: " + mRecipes.size());
                return mRecipes;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Recipe> mRecipes) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (mRecipes != null) {
                mRecipeAdapter.setRecipesData(mRecipes);
                showRecipesData();
                for (Recipe recipe: mRecipes){
                    checkIfMovieInDatabase(recipe);
                }

            } else {
                showErrorMessage();
            }
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void checkIfMovieInDatabase(final Recipe recipe) {

        Log.d(TAG, "checkIfMovieInDatabase: " + recipe.getRecipe_id());
        Recipe mDbRecipe = new Recipe(recipe.getRecipe_id(), recipe.getName(), recipe.getServing(), recipe.getImage());
        int id = mDbRecipe.getRecipe_id();
        MainIdViewModelFactory factory = new MainIdViewModelFactory(mDb, id);
        MainIdViewModel viewModel
                = ViewModelProviders.of(this, factory).get(MainIdViewModel.class);
        viewModel.getRecipe().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(@Nullable final Recipe mRecipe) {
                viewModel.getRecipe().removeObserver(this);
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        if (mRecipe == null) {
                            mDb.recipeDao().insertRecipe(mDbRecipe);
                            Log.d(TAG, "mDbRecipe: " + mDbRecipe.getName() + mDbRecipe.getRecipe_id());
                            List<Ingredient> ingredients = recipe.getIngredients();
                            List<Step> steps = recipe.getSteps();
                            for (Ingredient ingredient: ingredients){
                                mDb.ingredientDao().insertIngredient(ingredient);
                                Log.d(TAG, "I was here and inserted ingredient: " + ingredient.getId() + ingredient.getIngredient());
                            }
                            for (Step step: steps){
                                mDb.stepDao().insertStep(step);
                                Log.d(TAG, "I was here and inserted step: " + step.getId());
                            }
                        }
                    }
                });

            }
        });
    }


    private void loadIngredients() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getIngredients().observe(this, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(@Nullable List<Ingredient> ingredients) {
                Log.d(TAG, "Updating list of tasks from LiveData in ViewModel");
                if (ingredients.size()!= 0) {
                    Log.d(TAG, "blyat: " + ingredients.get(0).getIngredient() +
                            ingredients.get(0).getRecipeId() + " " + ingredients.get(0).getId());
                    Log.d(TAG, "blyat 2: " + ingredients.get(1).getIngredient()+
                            ingredients.get(0).getRecipeId()+ " " + ingredients.get(1).getId());
                }
            }
        });
    }


    private void loadRecipes() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                Log.d(TAG, "Updating list of tasks from LiveData in ViewModel");
                if (recipes.size()!= 0) {
                    Log.d(TAG, "suka: " + recipes.get(0).getName() +
                            recipes.get(0).getRecipe_id());
                    Log.d(TAG, "suka 2: " + recipes.get(1).getName()+
                            recipes.get(1).getRecipe_id());
                }
            }
        });
    }

    private void loadIngredientsById() {
        int id = 1;
        MainIdViewModelFactory factory = new MainIdViewModelFactory(mDb, id);
        final MainIdViewModel viewModel
                = ViewModelProviders.of(this, factory).get(MainIdViewModel.class);
        viewModel.getIngredients().observe(this, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(@Nullable List<Ingredient> ingredients) {
                Log.d(TAG, "gruzhu po id ingr");
                if (ingredients.size()!= 0) {
                    Log.d(TAG, "tvvar: " + ingredients.get(0).getIngredient() +
                            ingredients.get(0).getRecipeId());
                    Log.d(TAG, "tvaar 2: " + ingredients.get(1).getIngredient()+
                            ingredients.get(1).getRecipeId());
                }
            }
        });
    }
}
