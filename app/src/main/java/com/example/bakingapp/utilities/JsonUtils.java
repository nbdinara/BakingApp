package com.example.bakingapp.utilities;

import com.example.bakingapp.model.Ingredient;
import com.example.bakingapp.model.Recipe;
import com.example.bakingapp.model.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final String ID = "id";
    private static final String NAME = "name";

    private static final String INGREDIENTS = "ingredients";
    private static final String QUANTITY = "quantity";
    private static final String MEASURE = "measure";
    private static final String INGREDIENT = "ingredient";

    private static final String STEPS = "steps";
    private static final String STEP_ID = "id";
    private static final String SHORT_DESCRIPTION = "shortDescription";
    private static final String DESCRIPTION = "description";
    private static final String VIDEO_URL = "videoURL";
    private static final String THUMBNAIL_URL = "thumbnailURL";

    private static final String SERVING = "servings";
    private static final String IMAGE = "image";



    public static List<Recipe> getRecipesArrayFromJson(String recipesJsonStr) {

        ArrayList<Recipe> parsedRecipes = new ArrayList<>();
        ArrayList<Ingredient> parsedIngredients = new ArrayList<>();
        ArrayList<Step> parsedSteps = new ArrayList<>();

       // title, release date, movie poster, vote average, and plot synopsis.
        try {
            JSONObject jsonObject = new JSONObject(recipesJsonStr);
            JSONArray results = jsonObject.optJSONArray("");
            assert results != null;
            for (int i = 0; i < results.length(); i++){
                JSONObject recipe = results.optJSONObject(i);
                int id = recipe.optInt(ID);
                String name = recipe.optString(NAME);
                JSONArray ingredients = recipe.optJSONArray(INGREDIENTS);
                for (int j = 0; j < ingredients.length(); j++){
                    JSONObject ingredient = ingredients.optJSONObject(j);
                    double quantity = ingredient.optDouble(QUANTITY);
                    String measure = ingredient.optString(MEASURE);
                    String ingredientName = ingredient.optString(INGREDIENT);
                    Ingredient mIngredient = new Ingredient(quantity, measure, ingredientName);
                    parsedIngredients.add(mIngredient);
                }

                JSONArray steps = recipe.optJSONArray(STEPS);
                for (int j = 0; j < steps.length(); j++){
                    JSONObject step = ingredients.optJSONObject(j);
                    int stepId = step.optInt(STEP_ID);
                    String shortDescription = step.optString(SHORT_DESCRIPTION);
                    String description = step.optString(DESCRIPTION);
                    String videoURL = step.optString(VIDEO_URL);
                    String thumbnailURL = step.optString(THUMBNAIL_URL);
                    Step mStep = new Step(stepId, shortDescription, description, videoURL, thumbnailURL);
                    parsedSteps.add(mStep);
                }


                int serving = recipe.optInt(SERVING);
                String image = recipe.optString(IMAGE);

                Recipe mRecipe = new Recipe(id, name, parsedIngredients, parsedSteps, serving,
                        image);
                parsedRecipes.add(mRecipe);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return parsedRecipes;
    }


}
