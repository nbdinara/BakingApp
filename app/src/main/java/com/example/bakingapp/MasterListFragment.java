package com.example.bakingapp;

import android.content.Context;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
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
    private List<Ingredient> mIngredients;
    private View rootView;

    public static final String INGREDIENTS_LIST = "ingredients_list";
    public static final String STEPS_LIST = "steps_list";
    public static final String RECIPE = "recipe";
    MasterListAdapter mAdapter;
    MasterIngredientsListAdapter mIngredientsAdapter;

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
            if (rootView == null) {
                rootView = inflater.inflate(R.layout.fragment_master_list, container, false);
            }

            //  TextView ingredientsListView =  rootView.findViewById(R.id.tv_ingredients);
            ListView stepsListView = rootView.findViewById(R.id.lv_steps);
            if (mAdapter == null) {
                mAdapter = new MasterListAdapter(getContext(), mSteps);
                stepsListView.setAdapter(mAdapter);
                updateStepsListViewHeight(mAdapter, stepsListView);

                stepsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        // Trigger the callback method and pass in the position that was clicked
                        mCallback.onImageSelected(position);
                    }
                });
            }

            TextView recipeNameHeader = rootView.findViewById(R.id.tv_recipe_name_header);
            recipeNameHeader.setText(mRecipe.getName());

            if (mIngredientsAdapter == null) {

                mIngredientsAdapter = new MasterIngredientsListAdapter(getContext(), R.layout.ingredient_list_item, mIngredients);
                ListView ingredientsListView = rootView.findViewById(R.id.lv_ingredients);

                ingredientsListView.setAdapter(mIngredientsAdapter);
                updateIngredientsListViewHeight(mIngredientsAdapter, ingredientsListView);

            }


        }


        return rootView;
    }

    public void setRecipe(Recipe recipe) {
        mRecipe = recipe;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        mIngredients = ingredients;
    }

    public void setSteps(List<Step> steps) {
        mSteps = steps;
    }


    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putParcelableArrayList(STEPS_LIST, (ArrayList<Step>) mSteps);
        currentState.putParcelable(RECIPE, mRecipe);
        currentState.putParcelableArrayList(INGREDIENTS_LIST, (ArrayList<Ingredient>) mIngredients);
    }

    public static void updateIngredientsListViewHeight(MasterIngredientsListAdapter mIngredientsAdapter,
                                                       ListView mIngredientsListView) {
        if (mIngredientsAdapter == null) {
            return;
        }

        int totalHeight = 0;
        int adapterCount = mIngredientsAdapter.getCount();
        int listWidth = mIngredientsListView.getMeasuredWidth();


        for (int size = 0; size < adapterCount; size++) {
            View mView = mIngredientsAdapter.getView(size, null, mIngredientsListView);

            mView.measure(
                    View.MeasureSpec.makeMeasureSpec(listWidth, View.MeasureSpec.EXACTLY),

                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            totalHeight += mView.getMeasuredHeight();
        }
        // Change Height of ListView
        ViewGroup.LayoutParams params = mIngredientsListView.getLayoutParams();
        params.height = (totalHeight
                + (mIngredientsListView.getDividerHeight() * (adapterCount-1)));
        mIngredientsListView.setLayoutParams(params);
        //mIngredientsListView.requestLayout();

    }

    public static void updateStepsListViewHeight(MasterListAdapter mStepsAdapter,
                                                 ListView mStepsListView) {
        if (mStepsAdapter == null) {
            return;
        }
        // get listview height
        int totalHeight = 0;
        int adapterCount = mStepsAdapter.getCount();
        for (int size = 0; size < adapterCount; size++) {
            View listItem = mStepsAdapter.getView(size, null, mStepsListView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        // Change Height of ListView
        ViewGroup.LayoutParams params = mStepsListView.getLayoutParams();
        params.height = (totalHeight
                + (mStepsListView.getDividerHeight() * (adapterCount)));
        mStepsListView.setLayoutParams(params);
    }


}
