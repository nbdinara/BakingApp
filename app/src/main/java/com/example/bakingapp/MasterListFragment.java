package com.example.bakingapp;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bakingapp.database.AppDatabase;
import com.example.bakingapp.model.Ingredient;
import com.example.bakingapp.model.Recipe;
import com.example.bakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class MasterListFragment extends Fragment {

    private Recipe mRecipe;
    private ArrayList<Step> mSteps = new ArrayList<>();;
    private ArrayList<Ingredient> mIngredients;


    public static final String INGREDIENTS_LIST = "ingredients_list";
    public static final String STEPS_LIST = "steps_list";
    public static final String RECIPE = "recipe";

    AppDatabase mDb;

    // Define a new interface OnImageClickListener that triggers a callback in the host activity
    OnImageClickListener mCallback;

    // OnImageClickListener interface, calls a method in the host activity named onImageSelected
    public interface OnImageClickListener {
        void onImageSelected(int position);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (OnImageClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }
    }

    public MasterListFragment() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mSteps = savedInstanceState.getParcelableArrayList(STEPS_LIST);
            mRecipe = savedInstanceState.getParcelable(RECIPE);
            mIngredients = savedInstanceState.getParcelableArrayList(INGREDIENTS_LIST);
        } else {


            int id = mRecipe.getRecipe_id();
            mDb = AppDatabase.getInstance(getContext());
            mSteps = loadStepsFromDb(id);
            Log.d(ContentValues.TAG, "recipe_id: " + mRecipe.getRecipe_id());
            mIngredients = loadIngredientsFromDb(id);

            Log.d(ContentValues.TAG, "ready" + mIngredients.size());

        }



        MasterListAdapter mAdapter = new MasterListAdapter(getContext(), mSteps);
        View rootView =  inflater.inflate(R.layout.fragment_master_list, container, false);
        //  TextView ingredientsListView =  rootView.findViewById(R.recipe_id.tv_ingredients);
        ListView stepsListView =  rootView.findViewById(R.id.lv_steps);

        stepsListView.setAdapter(mAdapter);



        MasterIngredientsListAdapter mIngredientsAdapter =
                new MasterIngredientsListAdapter(getContext(), R.layout.ingredient_list_item, mIngredients);
        ListView ingredientsListView = rootView.findViewById(R.id.lv_ingredients);

        ingredientsListView.setAdapter(mIngredientsAdapter);

        stepsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Trigger the callback method and pass in the position that was clicked
                mCallback.onImageSelected(position);
            }
        });

        return rootView;
    }

    public void setData(Recipe recipe) {
        mRecipe = recipe;
        Log.d(TAG, "setData: recipe" + recipe.getRecipe_id());
    }


    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putParcelableArrayList(STEPS_LIST, (ArrayList<Step>) mSteps);
        currentState.putParcelable(RECIPE, mRecipe);
        currentState.putParcelableArrayList(INGREDIENTS_LIST, (ArrayList<Ingredient>) mIngredients);
    }

    public ArrayList<Ingredient> loadIngredientsFromDb(int recipe_id){
        mIngredients = new ArrayList<>();
        RecipeDetailViewModelFactory factory = new RecipeDetailViewModelFactory(mDb, recipe_id);
        final RecipeDetailsViewModel ingredientsViewModel
                = ViewModelProviders.of(this, factory).get(RecipeDetailsViewModel.class);
        ingredientsViewModel.getIngredients().observe(this, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(@Nullable List<Ingredient> ingredients) {
                Log.d(ContentValues.TAG, "I am here one");
                for (int i =0; i< ingredients.size(); i++){
                    mIngredients.add(ingredients.get(i));
                }
                Log.d(ContentValues.TAG, "I am here onee" + mIngredients.size() + "recipe id"
                        + mIngredients.get(1).getRecipeId());

            }
        });

    return mIngredients;
    }

    public ArrayList<Step> loadStepsFromDb(int recipe_id){
        final int id = mRecipe.getRecipe_id();

        RecipeDetailViewModelFactory factory = new RecipeDetailViewModelFactory(mDb, 1);
        final RecipeDetailsViewModel stepsViewModel
                = ViewModelProviders.of(this, factory).get(RecipeDetailsViewModel.class);
        stepsViewModel.getSteps().observe(this, new Observer<List<Step>>() {
            @Override
            public void onChanged(List<Step> steps) {
                Log.d(ContentValues.TAG, "I am here twoo" + recipe_id);
                mSteps = (ArrayList<Step>) steps;
                Log.d(ContentValues.TAG, "I am here two" + steps.size());
            }
        });
        return mSteps;
    }

}
