package com.norigin.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.norigin.BuildConfig;
import com.norigin.R;
import com.norigin.datastorage.MovieStorage;
import com.norigin.entity.Movie;
import com.norigin.fragment.FragmentListener;
import com.norigin.fragment.SplashFragment;
import com.norigin.fragment.VideoFragment;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ibohdan on 4/29/2016.
 */
public class HomeActivity extends AppCompatActivity implements FragmentListener {

    private static final String TAG = HomeActivity.class.getSimpleName();

    public static final String MOVIE_ID = "MOVIE_ID";

    public static final String REPLACE_VIDEO_EXTRA = "REPLACE_VIDEO_EXTRA";

    private boolean movieLoaded;

    private boolean canCloseSplash;

    private ArrayList<Movie> movieList = null;

    private Subscription subscription;

    private boolean replaceVideo;

    private boolean inSavedState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        replaceVideo = savedInstanceState == null || savedInstanceState.getBoolean(REPLACE_VIDEO_EXTRA, true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        inSavedState = false;

        updateScreenOrientation();

        String id = getIntent().getStringExtra(MOVIE_ID);
        if (id == null) {
            id = BuildConfig.MOVIE_ID;
        }
        startLoadData(id);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        replaceVideo = true;
    }

    /**
     * Invoke loading data from json file if movieList is null (not loaded yet)
     *
     * @param id movie id
     */
    private void startLoadData(String id) {
        subscription = Observable.<ArrayList<Movie>>create(subscriber -> {
            ArrayList<Movie> list;
            if (movieList != null) {
                list = movieList;
            } else {
                MovieStorage movieStorage = new MovieStorage(getApplicationContext());
                list = new ArrayList<>(movieStorage.loadMovies());
            }

            subscriber.onNext(list);
            subscriber.onCompleted();
        })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    movieList = list;
                    showFragments(id);
                }, throwable -> {
                    Log.e(TAG, "startLoadData", throwable);
                });
    }

    /**
     * Attach video fragment and splash fragment to the activity
     *
     * @param id movie id
     */
    private void showFragments(String id) {
        if (!replaceVideo) {
            return;
        }

        movieLoaded = false;
        canCloseSplash = false;

        Movie movie = null;
        for (Movie localMovie : movieList) {
            if (localMovie.getId().equals(id)) {
                movie = localMovie;
                break;
            }
        }

        FragmentManager fragmentManager = getSupportFragmentManager();

        VideoFragment videoFragment = VideoFragment.getInstance(movie, movieList);
        fragmentManager.beginTransaction().replace(R.id.fragment_container_video, videoFragment).commit();

        SplashFragment splashFragment = SplashFragment.getInstance(movie);
        fragmentManager.beginTransaction().add(R.id.fragment_container_splash, splashFragment).commit();

        replaceVideo = false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(REPLACE_VIDEO_EXTRA, replaceVideo);
        inSavedState = true;
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        inSavedState = false;
    }

    @Override
    protected void onStop() {
        super.onStop();

        inSavedState = true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        updateScreenOrientation();
    }

    /**
     * Set fullscreen mode for landscape orientation
     *
     * @param visible status bar visibility flag
     */
    private void setFullscreen(boolean visible) {
        if (visible) {
            // Hide status bar
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            // Show status bar
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    /**
     * Close splash screen if movie is ready to play and splash was showing at least 1 second
     */
    private void closeSplash() {
        if (!canCloseSplash || !movieLoaded || inSavedState) {
            return;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment splashFragment = fragmentManager.findFragmentById(R.id.fragment_container_splash);

        if (splashFragment != null) {
            fragmentManager.beginTransaction().remove(splashFragment).commit();
        }

        Fragment videoFragment = fragmentManager.findFragmentById(R.id.fragment_container_video);

        if (videoFragment instanceof VideoFragment) {
            // Invoke playing the video after splash is closed
            ((VideoFragment) videoFragment).playWhenReady();
        }
    }

    /**
     * Update UI depends on orientation
     */
    public void updateScreenOrientation() {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            setFullscreen(false);
        } else {
            setFullscreen(true);
        }
    }

    /**
     * Invoke closing splash screen cause video is ready to play
     */
    @Override
    public void movieLoaded() {
        movieLoaded = true;
        closeSplash();
    }

    @Override
    public void splashCanClose(boolean forceClose) {
        canCloseSplash = true;
        if (forceClose) {
            movieLoaded = true;
        }
        closeSplash();
    }
}
