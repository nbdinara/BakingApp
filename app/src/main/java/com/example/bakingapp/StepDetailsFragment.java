package com.example.bakingapp;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    public static final String PLAYER_CURRENT_POS = "player_current_pos";
    public static final String PLAYER_IS_READY = "player_is_ready";

    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private long mCurrentPosition;
    private boolean isPlayerReady;

    public StepDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(savedInstanceState != null) {
            mId = savedInstanceState.getInt(STEP_INDEX);
            mSteps = savedInstanceState.getParcelableArrayList(STEPS_LIST);
            mCurrentPosition = savedInstanceState.getLong(PLAYER_CURRENT_POS);
            isPlayerReady = savedInstanceState.getBoolean(PLAYER_IS_READY);
        } else {
            mCurrentPosition = -1;
        }

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_step_details, container, false);
        //ImageView imageView = rootView.findViewById(R.id.media_player);
        final TextView textView = rootView.findViewById(R.id.tv_step_full_description);
        Button previousButton = rootView.findViewById(R.id.btn_previous);
        Button nextButton = rootView.findViewById(R.id.btn_next);

        mPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.media_player);
        setVideo(mSteps.get(mId).getVideoURL(), mSteps.get(mId).getThumbnailURL());
        if (mCurrentPosition != -1){
            mExoPlayer.seekTo(mCurrentPosition);
            mExoPlayer.setPlayWhenReady(isPlayerReady);
        }


        int orientation = getResources().getConfiguration().orientation;
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);

        if (orientation == Configuration.ORIENTATION_LANDSCAPE && !tabletSize) {
            // In landscape
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mPlayerView.getLayoutParams();
            params.width=params.MATCH_PARENT;
            params.height=params.MATCH_PARENT;
            mPlayerView.setLayoutParams(params);

           // mPlayerView.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
          //  mPlayerView.getLayoutParams().height = LinearLayout.LayoutParams.MATCH_PARENT;
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        }

        if (tabletSize){
            previousButton.setVisibility(View.GONE);
            nextButton.setVisibility(View.GONE);
        }

        //imageView.setImageResource();
        if (mSteps != null) {
            textView.setText(mSteps.get(mId).getDescription());
            previousButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mId > 0){
                        mId--;
                        releasePlayer();
                        setVideo(mSteps.get(mId).getVideoURL(), mSteps.get(mId).getThumbnailURL());
                        textView.setText(mSteps.get(mId).getDescription());
                    } else {
                        Toast.makeText(getContext(), "This is the first step", Toast.LENGTH_LONG).show();
                    }

                }
            });

            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mId < mSteps.size()-1){
                        mId++;
                        releasePlayer();
                        textView.setText(mSteps.get(mId).getDescription());
                        setVideo(mSteps.get(mId).getVideoURL(), mSteps.get(mId).getThumbnailURL());
                    } else {
                        Toast.makeText(getContext(), "This is the last step", Toast.LENGTH_LONG).show();
                    }
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


    public void setVideo (String videoURL, String thumbnailURL){
        if (isNotNullOrEmpty(videoURL)) {
            initializePlayer(Uri.parse(videoURL));
            mPlayerView.getVideoSurfaceView().setVisibility(View.VISIBLE);
            mPlayerView.setVisibility(View.VISIBLE);
        } else if (isNotNullOrEmpty(thumbnailURL)){
            initializePlayer(Uri.parse(thumbnailURL));
            mPlayerView.getVideoSurfaceView().setVisibility(View.VISIBLE);
            mPlayerView.setVisibility(View.VISIBLE);
        } else if (!isNotNullOrEmpty(videoURL) && !isNotNullOrEmpty(thumbnailURL)){
            //mPlayerView.setBackground(null);
            mPlayerView.getVideoSurfaceView().setVisibility(View.GONE);
            mPlayerView.setVisibility(View.GONE);
        }

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
        currentState.putLong(PLAYER_CURRENT_POS, Math.max(0, mExoPlayer.getCurrentPosition()));
        currentState.putBoolean(PLAYER_IS_READY, mExoPlayer.getPlayWhenReady());
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
