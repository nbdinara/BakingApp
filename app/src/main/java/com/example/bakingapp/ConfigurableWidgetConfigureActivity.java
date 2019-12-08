package com.example.bakingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Spinner;

import com.example.bakingapp.model.Ingredient;
import com.example.bakingapp.model.Recipe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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
    private List<Ingredient> mIngredients;
    private List<Recipe> mRecipes;
    private int mRecipeId = -1;
    private String mIngredientsString;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // activity stuffs
        setContentView(R.layout.activity_widget_configure);

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
        addItemsOnSpinner();
    }

    public void addItemsOnSpinner() {

        List<String> list = new ArrayList<String>();
        list.add("list 1");
        list.add("list 2");
        list.add("list 3");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recipesSpinner.setAdapter(dataAdapter);
    }

    public void confirmConfiguration(View v) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        String recipesSelectedItem = String.valueOf(recipesSpinner.getSelectedItem());
        mIngredients = getIngredientsFromRecipe(recipesSelectedItem);
        mIngredientsString = makeStringFromIngredientsList(mIngredients);


        RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.recipe_ingredients_widget);
        views.setOnClickPendingIntent(R.id.tv_ingredients_widget, pendingIntent);
        views.setCharSequence(R.id.tv_ingredients_widget, "setText", mIngredientsString);

        appWidgetManager.updateAppWidget(appWidgetId, views);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_INGREDIENTS_TEXT + appWidgetId, mIngredientsString);
        editor.putInt(KEY_RECIPE_ID + appWidgetId, mRecipeId);
        editor.apply();

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }


    public List<Ingredient> getIngredientsFromRecipe (String recipeName){
        mIngredients = new ArrayList<>();
        for (int i = 0; i < mRecipes.size(); i++){
            if (mRecipes.get(i).getName().equals(recipeName)){
                mIngredients = mRecipes.get(i).getIngredients();
                mRecipeId = mRecipes.get(i).getId();
                break;
            }
        }
        return  mIngredients;
    }

    public String makeStringFromIngredientsList (List<Ingredient> ingredients){
        mIngredientsString = "";

        for (int i = 0; i < ingredients.size(); i++){
            mIngredientsString = mIngredientsString + ingredients.get(i).getIngredient() + " "
                    + ingredients.get(i).getQuantity() + " "
                    + ingredients.get(i).getMeasure() + "\n";
        }

        return mIngredientsString;
    }

}
