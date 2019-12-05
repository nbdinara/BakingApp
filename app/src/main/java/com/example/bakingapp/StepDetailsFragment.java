package com.example.bakingapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.Duration;

import static android.content.ContentValues.TAG;


public class StepDetailsFragment extends Fragment {

    private ArrayList<Step> mSteps;
    private int mId;

    public static final String STEPS_LIST = "steps_list";
    public static final String STEP_INDEX = "step_index";

    public StepDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(savedInstanceState != null) {
            mId = savedInstanceState.getInt(STEP_INDEX);
            mSteps = savedInstanceState.getParcelableArrayList(STEPS_LIST);
        }

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_step_details, container, false);
        ImageView imageView = rootView.findViewById(R.id.media_player);
        final TextView textView = rootView.findViewById(R.id.tv_step_full_description);
        Button previousButton = rootView.findViewById(R.id.btn_previous);
        Button nextButton = rootView.findViewById(R.id.btn_next);

        //imageView.setImageResource();
        if (mSteps != null) {
            textView.setText(mSteps.get(mId).getDescription());
            previousButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mId > 0){
                        mId--;
                    } else {
                        Toast.makeText(getContext(), "This is the first step", Toast.LENGTH_LONG).show();
                    }
                    textView.setText(mSteps.get(mId).getDescription());
                }
            });

            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mId < mSteps.size()-1){
                        mId++;
                    } else {
                        Toast.makeText(getContext(), "This is the last step", Toast.LENGTH_LONG).show();
                    }
                    textView.setText(mSteps.get(mId).getDescription());
                }
            });
        } else {
            Log.d(TAG, "This fragment doesn't have step: ");
        }
        return rootView;
    }

    public void setSteps(ArrayList<Step> steps) {
        mSteps = steps;
    }


    public void setId(int id) {
        mId = id;
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putParcelableArrayList(STEPS_LIST, (ArrayList<Step>) mSteps);
        currentState.putInt(STEP_INDEX, mId);
    }


}
