package com.example.bakingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bakingapp.model.Recipe;
import com.example.bakingapp.utilities.JsonUtils;

import org.json.JSONException;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import static android.content.ContentValues.TAG;


public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapterOnClickHandler{


    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;
    private RecipeAdapter mRecipeAdapter;
    ArrayList<Recipe> mRecipes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_recipes);
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = findViewById(R.id.pb_loading_all_recipes_indicator);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

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
        Toast.makeText(context, recipe.getName(), Toast.LENGTH_SHORT)
                .show();
    }




}
