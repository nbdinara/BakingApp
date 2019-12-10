package com.example.bakingapp;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.model.Recipe;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {

    private List<Recipe> mRecipesData;
    private final RecipeAdapterOnClickHandler mClickHandler;


    public interface RecipeAdapterOnClickHandler {
        void onClick(Recipe recipe);
    }

    public RecipeAdapter(RecipeAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }


    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mRecipeNameView;
        private final TextView mRecipeServingView;
        private final ProgressBar mProgressBar;

        private RecipeAdapterViewHolder(View view) {
            super(view);
            mRecipeNameView = view.findViewById(R.id.tv_recipe_name);
            mRecipeServingView = view.findViewById(R.id.tv_recipe_serving);
            mProgressBar = view.findViewById(R.id.pb_loading_each_recipe_name_indicator);
            view.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Recipe recipe = mRecipesData.get(adapterPosition);
            mClickHandler.onClick(recipe);
        }
    }


    @NonNull
    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.recipe_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;


        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new RecipeAdapterViewHolder(view);
    }




    @Override
    public void onBindViewHolder(RecipeAdapterViewHolder recipeAdapterViewHolder, int position) {
        String name = mRecipesData.get(position).getName();
        recipeAdapterViewHolder.mRecipeNameView.setText(name);
        int serving = mRecipesData.get(position).getServing();
        recipeAdapterViewHolder.mRecipeServingView.setText("Serving: " + Integer.toString(serving));

    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        if (null == mRecipesData) return 0;
        return mRecipesData.size();
    }

    public void setRecipesData(List<Recipe> recipesData) {
        mRecipesData = recipesData;
        notifyDataSetChanged();
    }

}
