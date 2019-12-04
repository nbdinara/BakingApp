package com.example.bakingapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bakingapp.model.Step;

import java.util.List;


public class MasterListFragment extends Fragment {

    private List<Step> mSteps;

    public MasterListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_master_list, container, false);
        TextView ingredientsListView =  rootView.findViewById(R.id.tv_ingredients);
        ListView stepsListView =  rootView.findViewById(R.id.lv_steps);

        MasterListAdapter mAdapter = new MasterListAdapter(getContext(), mSteps);

        stepsListView.setAdapter(mAdapter);

        return rootView;
    }

    public void setSteps(List<Step> steps) {
        mSteps = steps;
    }
}
