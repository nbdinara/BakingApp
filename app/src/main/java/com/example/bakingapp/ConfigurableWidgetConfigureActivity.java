package com.example.bakingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Spinner;

import com.example.bakingapp.database.AppDatabase;
import com.example.bakingapp.model.Ingredient;
import com.example.bakingapp.model.Recipe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static android.content.ContentValues.TAG;

public class ConfigurableWidgetConfigureActivity extends AppCompatActivity {

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private Spinner recipesSpinner;
    private Button chooseRecipeButton;
    private AppWidgetManager widgetManager;
    private RemoteViews views;

    public static final String SHARED_PREFS = "prefs";
    public static final String KEY_INGREDIENTS_TEXT = "keyIngredientsText";
    public static final String KEY_RECIPE_ID = "keyRecipeId";
    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private List<Recipe> mRecipes;
    private int mRecipeId = -1;
    private AppDatabase mDb;
    private List<Ingredient> mIngredients;
    private String packageName;
    private PendingIntent pendingIntent;
    private AppWidgetManager appWidgetManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // activity stuffs
        setContentView(R.layout.activity_widget_configure);

        mDb = AppDatabase.getInstance(getApplicationContext());

        Intent configIntent = getIntent();
        Bundle extras = configIntent.getExtras();
        if (extras != null) {
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_CANCELED, resultValue);

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        recipesSpinner = findViewById(R.id.spinner_recipes);
        loadRecipes();
    }

    public void addItemsOnSpinner(List<Recipe> recipes) {
        mRecipes = recipes;
        List<String> recipesName = new ArrayList<String>();
        for (int i = 0; i < recipes.size(); i++){
            recipesName.add(recipes.get(i).getName());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, recipesName);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recipesSpinner.setAdapter(dataAdapter);
    }

    public void confirmConfiguration(View v) {
        appWidgetManager = AppWidgetManager.getInstance(this);

        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        packageName = this.getPackageName();
        String recipesSelectedItem = String.valueOf(recipesSpinner.getSelectedItem());
        List<Ingredient> ingredients = getIngredientsFromRecipe(recipesSelectedItem);

    }


    public List<Ingredient> getIngredientsFromRecipe (String recipeName){
        List<Ingredient> ingredients = new ArrayList<>();
        for (int i = 0; i < mRecipes.size(); i++){
            if (mRecipes.get(i).getName().equals(recipeName)){
                mRecipeId = mRecipes.get(i).getId();
                loadIngredientsByRecipeId(mRecipeId);
                break;
            }
        }
        return  ingredients;
    }

    public String makeStringFromIngredientsList (List<Ingredient> ingredients){
        String mIngredientsString = "";

        for (int i = 0; i < ingredients.size(); i++){
            mIngredientsString = mIngredientsString + ingredients.get(i).getIngredient() + " "
                    + ingredients.get(i).getQuantity() + " "
                    + ingredients.get(i).getMeasure() + "\n";
        }

        return mIngredientsString;
    }

    private void loadRecipes() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                Log.d(TAG, "Updating list of tasks from LiveData in ViewModel");
                if (recipes != null) {
                    addItemsOnSpinner(recipes);
                }
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
                String ingredientsString = makeStringFromIngredientsList(ingredients);


                RemoteViews views = new RemoteViews(packageName, R.layout.recipe_ingredients_widget);
                views.setOnClickPendingIntent(R.id.tv_ingredients_widget, pendingIntent);
                views.setCharSequence(R.id.tv_ingredients_widget, "setText", ingredientsString);

                appWidgetManager.updateAppWidget(appWidgetId, views);

                SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(KEY_INGREDIENTS_TEXT + appWidgetId, ingredientsString);
                editor.putInt(KEY_RECIPE_ID + appWidgetId, mRecipeId);
                editor.apply();

                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                setResult(RESULT_OK, resultValue);
                finish();
            }
        });
    }


}
