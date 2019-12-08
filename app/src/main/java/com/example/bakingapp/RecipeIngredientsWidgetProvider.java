package com.example.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.example.bakingapp.model.Recipe;

import static com.example.bakingapp.ConfigurableWidgetConfigureActivity.KEY_INGREDIENTS_TEXT;
import static com.example.bakingapp.ConfigurableWidgetConfigureActivity.KEY_RECIPE_ID;
import static com.example.bakingapp.ConfigurableWidgetConfigureActivity.SHARED_PREFS;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeIngredientsWidgetProvider extends AppWidgetProvider {

    private static Recipe mRecipe;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        int recipeId = prefs.getInt(KEY_RECIPE_ID + appWidgetId, 0);
       // getRecipeById(recipeId);
        String ingredientsText = prefs.getString(KEY_INGREDIENTS_TEXT + appWidgetId,
                "Something went wrong. Ingredients list is empty");
        Intent intent = new Intent(context, RecipeDetailsActivity.class);
        intent.putExtra("recipe", mRecipe);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);



        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_ingredients_widget);
        views.setOnClickPendingIntent(R.id.tv_ingredients_widget, pendingIntent);
        views.setCharSequence(R.id.tv_ingredients_widget, "setText", ingredientsText);

        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    //public static int getRecipeById(int recipeId){
        //return mRecipe;
    //}
}

