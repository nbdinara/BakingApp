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
import java.util.List;

import javax.xml.datatype.Duration;

import static android.content.ContentValues.TAG;


public class StepDetailsFragment extends Fragment {

    private ArrayList<Step> mSteps;
    private int mId;

    public static final String STEPS_LIST = "steps_list";
    public static final String STEP_INDEX = "step_index";
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private String mVideoURL;
    private String mThumbnailURL;

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
        //ImageView imageView = rootView.findViewById(R.id.media_player);
        final TextView textView = rootView.findViewById(R.id.tv_step_full_description);
        Button previousButton = rootView.findViewById(R.id.btn_previous);
        Button nextButton = rootView.findViewById(R.id.btn_next);

        mPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.media_player);


        mVideoURL = mSteps.get(mId).getVideoURL();
        mThumbnailURL = mSteps.get(mId).getThumbnailURL();

        if (isNotNullOrEmpty(mVideoURL)) {
            initializePlayer(Uri.parse(mSteps.get(mId).getVideoURL()));
        } else if (isNotNullOrEmpty(mThumbnailURL)){
            initializePlayer(Uri.parse(mSteps.get(mId).getThumbnailURL()));
        } else if (!isNotNullOrEmpty(mVideoURL) && !isNotNullOrEmpty(mThumbnailURL)){
            //mPlayerView.setBackground(null);
            mPlayerView.getVideoSurfaceView().setVisibility(View.GONE);
            mPlayerView.setVisibility(View.GONE);
        }

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

    public static boolean isNotNullOrEmpty(String str) {
        if(str != null && !str.isEmpty())
            return true;
        return false;
    }



    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
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

    @Override
    public void onPause() {
        super.onPause();
        pausePlayer();

    }

    @Override
    public void onResume() {
        super.onResume();
        startPlayer();
    }

    private void pausePlayer(){
        if (mExoPlayer!=null) {
            mExoPlayer.setPlayWhenReady(false);
            mExoPlayer.getPlaybackState();
        }
    }
    private void startPlayer(){
        if (mExoPlayer !=null) {
            mExoPlayer.setPlayWhenReady(true);
            mExoPlayer.getPlaybackState();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();

    }
}
