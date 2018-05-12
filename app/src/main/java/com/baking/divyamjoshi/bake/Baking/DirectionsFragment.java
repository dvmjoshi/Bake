package com.baking.divyamjoshi.bake.Baking;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baking.divyamjoshi.bake.R;
import com.baking.divyamjoshi.bake.RecipeResponse;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Divyam on 06-12-2017.
 */

public class DirectionsFragment extends Fragment {
    @Nullable
    @BindView(R.id.simple_exo_play_view)SimpleExoPlayerView mSimpleExoPlayerView;
    @Nullable @BindView(R.id.direction)TextView mDirections;
    @Nullable @BindView(R.id.direction_appBar)AppBarLayout appBarLayout;
    @Nullable @BindView(R.id.directions_toolbar)Toolbar toolbar;
    @Nullable @BindView(R.id.directions_toolbarText)TextView toolbarText;
    @Nullable @BindView(R.id.directions_toolbar_back_button)ImageButton backButton;
    @Nullable @BindView(R.id.fragment_direction_layout)LinearLayout layout;

    private static final String STEPS_LIST = "sList";
    private static final String POSITION = "position";
    private static final String IS_FULLSCREEN = "is_fullscreen";
    private static final String IS_NULL = "is_null";
    private static final String EXO_CURRENT_POSITION = "current_position";

    private ArrayList<RecipeResponse.StepsBean> sList;
    private String videoUrl;
    private String thumbnailUrl;
    private String description;
    private String directionString;
    private int position;
    private long exo_current_position = 0;
    private SimpleExoPlayer exoPlayer;
    private Unbinder unbinder;
    private boolean isFullScreen = false;
    private boolean isNull;
    private boolean playerStopped = false;
    private long playerStopPosition;

    public DirectionsFragment() {
        // Required empty public constructor
    }

    public static DirectionsFragment newInstance(ArrayList<RecipeResponse.StepsBean> sList, int position) {

        DirectionsFragment fragment = new DirectionsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(STEPS_LIST,sList);
        args.putInt(POSITION, position);

        // Set true if nothing was selected,
        // needed in case in tablet mode
        if(position == -1){
            args.putBoolean(IS_NULL,true);
        } else {
            args.putBoolean(IS_NULL,false);
        }

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        isNull = getArguments().getBoolean(IS_NULL);
        if(!isNull) {
            sList = getArguments().getParcelableArrayList(STEPS_LIST);
            position = getArguments().getInt(POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_directions, container, false);
        unbinder = ButterKnife.bind(this,view);

        checkFullScreen();

        if(savedInstanceState == null){
            isFullScreen = false;
        } else {
            isFullScreen = savedInstanceState.getBoolean(IS_FULLSCREEN);
            exo_current_position = savedInstanceState.getLong(EXO_CURRENT_POSITION);
        }


        if(!isNull) {

            videoUrl = sList.get(position).getVideoURL();
            description = sList.get(position).getShortDescription();
            directionString = sList.get(position).getDescription();
            thumbnailUrl = sList.get(position).getThumbnailURL();

            // Toolbar null when in tablet mode
            if(toolbarText != null) {
                toolbarText.setText(description);
                mDirections.setText(directionString);

                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getActivity().onBackPressed();
                    }
                });
            }

            // Add function to custom exoplayer fullscreen button
            view.findViewById(R.id.exo_full).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isFullScreen) {
                        isFullScreen = true;
                        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    } else {
                        isFullScreen = false;
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    }
                }
            });

            // Check to see if video url is not empty,
            // hide player if empty
            if (!videoUrl.equals("")) {
                initializePlayer(Uri.parse(videoUrl));
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
            } else {
                assert mSimpleExoPlayerView != null;
                mSimpleExoPlayerView.setVisibility(View.GONE);
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            }
        } else {
            // For tablet mode, if no direction selected hide layout
            assert layout != null;
            layout.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_FULLSCREEN,isFullScreen);
        outState.putLong(EXO_CURRENT_POSITION,exoPlayer.getCurrentPosition());
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer(Uri.parse(videoUrl));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || exoPlayer == null )) {
            initializePlayer(Uri.parse(videoUrl));
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23 || exoPlayer !=null) {
            playerStopPosition = exoPlayer.getCurrentPosition();
            playerStopped = true;
            releasePlayer();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
        unbinder.unbind();
    }

    /**
     * Method to initialize media player
     *
     * @param mediaUri Uri of media
     */
    private void initializePlayer(Uri mediaUri){
        if(exoPlayer == null){
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(),trackSelector,loadControl);
            mSimpleExoPlayerView.setPlayer(exoPlayer);
            String userAgent = Util.getUserAgent(getContext(),"KissTheBaker");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(),userAgent), new DefaultExtractorsFactory(),null,null);
            exoPlayer.prepare(mediaSource);
            if (exo_current_position != 0 && !playerStopped){
                exoPlayer.seekTo(exo_current_position);
            } else {
                exoPlayer.seekTo(playerStopPosition);
            }
        }
    }

    /**
     * Method to release exoPlayer
     */
    private void releasePlayer(){
        if(exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    /**
     * Method to check for current device orientation
     */
    public void checkFullScreen(){
        Configuration newConfig = new Configuration();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            isFullScreen = true;
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            isFullScreen = false;
        }
    }
}
