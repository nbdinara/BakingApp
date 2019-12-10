package com.example.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.bakingapp.database.AppDatabase;
import com.example.bakingapp.model.Recipe;

import java.util.Random;

import static android.content.ContentValues.TAG;
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
        String ingredientsText = prefs.getString(KEY_INGREDIENTS_TEXT + appWidgetId,
                "Something went wrong. Ingredients list is empty");



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

    private class FetchRecipeTask extends AsyncTask<Void, Void, Void> {

        private Context context;
        private AppWidgetManager appWidgetManager;
        private int appWidgetId;
        private int recipeId;
        private String ingredientsText;
        Recipe recipe = null;

        public FetchRecipeTask(Context context, AppWidgetManager appWidgetManager, int appWidgetId,
                               int recipeId, String ingredientsText){
            this.context = context;
            this.appWidgetManager = appWidgetManager;
            this.appWidgetId = appWidgetId;
            this.recipeId = recipeId;
            this.ingredientsText = ingredientsText;

        }

        @Override
        protected Void doInBackground(Void... params) {

            AppDatabase mDb = AppDatabase.getInstance(context);
            recipe = mDb.recipeDao().loadRecipeById1(recipeId);
            Log.d(TAG, "doInBackground: ");
            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            Intent intent = new Intent(context, RecipeDetailsActivity.class);
            intent.putExtra("recipe_id", recipe.getId());
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);



            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_ingredients_widget);
            views.setOnClickPendingIntent(R.id.tv_ingredients_widget, pendingIntent);
            views.setCharSequence(R.id.tv_ingredients_widget, "setText", ingredientsText);

            appWidgetManager.updateAppWidget(appWidgetId, views);

        }

    }
}

