package com.example.bakingapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bakingapp.model.Step;

import static android.content.ContentValues.TAG;


public class StepDetailsFragment extends Fragment {

    Step mStep;

    public StepDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_step_details, container, false);
        ImageView imageView = rootView.findViewById(R.id.media_player);
        TextView textView = rootView.findViewById(R.id.tv_step_full_description);

        //imageView.setImageResource();
        if (mStep != null) {
            textView.setText(mStep.getDescription());
        } else {
            Log.d(TAG, "This fragment doesn't have step: ");
        }
        return rootView;
    }

    public void setmStep(Step step) {
        mStep = step;
    }



}
