package com.example.bakingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.bakingapp.model.Recipe;
import com.example.bakingapp.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

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
            id = intentThatStartedThisActivity.getIntExtra("id", 0);
            Log.d(TAG, "Mysteps2: " + mSteps.size() + "   id " + id);
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
