package com.example.bakingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.example.bakingapp.model.Step;

import java.util.ArrayList;
import static android.content.ContentValues.TAG;


public class StepDetailsActivity extends AppCompatActivity {

    private ArrayList<Step> mSteps;
    private int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

        Intent intentThatStartedThisActivity = getIntent();
        mSteps = new ArrayList<>();
        if (intentThatStartedThisActivity != null) {
            mSteps = intentThatStartedThisActivity.getParcelableArrayListExtra("steps");
            id = intentThatStartedThisActivity.getIntExtra("recipe_id", 0);
            Log.d(TAG, "Mysteps2: " + mSteps.size() + "   recipe_id " + id);
        }

        if (savedInstanceState == null) {

            StepDetailsFragment stepFragment = new StepDetailsFragment();
            stepFragment.setSteps(mSteps);
            stepFragment.setId(id);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().add(R.id.step_container, stepFragment).commit();
        }


    }


}
