package com.example.bakingapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bakingapp.model.Ingredient;
import com.example.bakingapp.model.Recipe;
import com.example.bakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;


public class MasterListFragment extends Fragment {

    private Recipe mRecipe;
    private List<Step> mSteps;
    private ArrayList<Ingredient> mIngredients;

    public static final String INGREDIENTS_LIST = "ingredients_list";
    public static final String STEPS_LIST = "steps_list";
    public static final String RECIPE = "recipe";

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
            mSteps = new ArrayList<>();
            mSteps = mRecipe.getSteps();
            mIngredients = new ArrayList<>();
            mIngredients = mRecipe.getIngredients();
        }
        MasterListAdapter mAdapter = new MasterListAdapter(getContext(), mSteps);
        View rootView =  inflater.inflate(R.layout.fragment_master_list, container, false);
        //  TextView ingredientsListView =  rootView.findViewById(R.id.tv_ingredients);
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

    public void setRecipe(Recipe recipe) {
        mRecipe = recipe;
    }


    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putParcelableArrayList(STEPS_LIST, (ArrayList<Step>) mSteps);
        currentState.putParcelable(RECIPE, mRecipe);
        currentState.putParcelableArrayList(INGREDIENTS_LIST, (ArrayList<Ingredient>) mIngredients);
    }
}
