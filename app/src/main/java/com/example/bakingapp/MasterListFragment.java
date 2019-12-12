package com.example.bakingapp;

import android.content.Context;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bakingapp.model.Ingredient;
import com.example.bakingapp.model.Recipe;
import com.example.bakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


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
    @BindView(R.id.lv_steps) ListView stepsListView;
    @BindView(R.id.tv_recipe_name_header)TextView recipeNameHeader;
    @BindView(R.id.lv_ingredients) ListView ingredientsListView;


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
            ButterKnife.bind(this, rootView);

            //  TextView ingredientsListView =  rootView.findViewById(R.id.tv_ingredients);
            if (mAdapter == null) {
                mAdapter = new MasterListAdapter(getContext(), mSteps);
                stepsListView.setAdapter(mAdapter);
                updateIngredientsListViewHeight(stepsListView);

                stepsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        // Trigger the callback method and pass in the position that was clicked
                        mCallback.onImageSelected(position);
                    }
                });
            }


            recipeNameHeader.setText(mRecipe.getName());

            if (mIngredientsAdapter == null) {

                mIngredientsAdapter = new MasterIngredientsListAdapter(getContext(), R.layout.ingredient_list_item, mIngredients);


                ingredientsListView.setAdapter(mIngredientsAdapter);
                updateIngredientsListViewHeight(ingredientsListView);

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

    public static void updateIngredientsListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        int i;
        for (i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();

        }

        //add divider height to total height as many items as there are in listview
        totalHeight += listView.getDividerHeight()*i;
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);

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
