package com.example.bakingapp;

import android.app.Activity;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bakingapp.model.Ingredient;

import java.util.ArrayList;


public class MasterIngredientsListAdapter extends ArrayAdapter<Ingredient> {

    private Context mContext;
    //private List<Ingredient> mIngredients;
    private int mLayoutResourceId;
    private ArrayList<Ingredient> mIngredients = new ArrayList<>();



    public MasterIngredientsListAdapter(Context context, int layoutResourceId, ArrayList<Ingredient> ingredients) {
        super(context, layoutResourceId, ingredients);

        mContext = context;
        mLayoutResourceId = layoutResourceId;
        mIngredients = ingredients;
    }

    static class ViewHolder {
        TextView ingredientTextView;
        TextView quantityTextView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Ingredient currentIngredient = getItem(position);
        if (convertView == null) {
            // If the view is not recycled, this creates a new ImageView to hold an image
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(mLayoutResourceId, parent, false);
            holder = new ViewHolder();

            holder.ingredientTextView = (TextView)convertView.findViewById(R.id.tv_ingredient_name);
            holder.quantityTextView =  (TextView) convertView.findViewById(R.id.tv_ingredient_quantity);
            convertView.setTag(holder);
            // Define the layout parameters
        } else {
            holder = (ViewHolder) convertView.getTag();;
        }

        // Set the image resource and return the newly created ImageView
        String quantity = currentIngredient.getQuantity() + " "
                + currentIngredient.getMeasure();
        holder.ingredientTextView.setText(currentIngredient.getIngredient());
        holder.quantityTextView.setText(quantity);
        return convertView;
    }
}
