package com.example.bakingapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bakingapp.model.Step;

import java.util.List;

public class MasterListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Step> mSteps;

    public MasterListAdapter(Context context, List<Step> steps) {
        mContext = context;
        mSteps = steps;
    }

    @Override
    public int getCount() {
        return mSteps.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) {
            // If the view is not recycled, this creates a new ImageView to hold an image
            textView = new TextView(mContext);
            // Define the layout parameters
        } else {
            textView = (TextView) convertView;
        }

        // Set the image resource and return the newly created ImageView
        textView.setText(mSteps.get(position).getShortDescription());
        return textView;
    }
}
