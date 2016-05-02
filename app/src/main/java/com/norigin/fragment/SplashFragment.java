package com.norigin.fragment;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.norigin.R;
import com.norigin.entity.Movie;
import com.norigin.util.ImageHelper;

/**
 * Fragment for showing info about current movie
 * <p>
 * Created by ibohdan on 4/29/2016.
 */
public class SplashFragment extends Fragment {

    private static final String MOVIE_SCREEN_PARAMS = "MOVIE_SCREEN_PARAMS";

    private static final int MIN_SPLASH_DELAY = 1000;  // milliseconds

    private static final int MAX_SPLASH_DELAY = 10000; // milliseconds

    private ViewHolder viewHolder;

    /**
     * Returns instance of a Splash fragment
     *
     * @param movie selected movie
     * @return instance of SplashFragment
     */
    public static SplashFragment getInstance(Movie movie) {
        SplashFragment splashFragment = new SplashFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(MOVIE_SCREEN_PARAMS, movie);
        splashFragment.setArguments(bundle);
        return splashFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (view == null) {
            viewHolder = null;
        } else {
            viewHolder = new ViewHolder(view);
            viewHolder.setData((Movie) getArguments().getSerializable(MOVIE_SCREEN_PARAMS));
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Use delay to closing splash screen (min = 1 sec)
        final Handler handler = new Handler();
        handler.postDelayed(() -> closeSplash(false), MIN_SPLASH_DELAY);

        // Use delay to closing splash screen (max = 10 sec)
        handler.postDelayed(() -> closeSplash(true), MAX_SPLASH_DELAY);
    }

    private void closeSplash(boolean forceClose) {
        Activity activity = getActivity();
        if (activity instanceof FragmentListener) {
            FragmentListener listener = (FragmentListener) activity;
            listener.splashCanClose(forceClose);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewHolder = null;
    }

    /**
     * Holder for fragment view
     */
    public class ViewHolder {

        protected final TextView titleView;

        protected final TextView yearView;

        protected final TextView descriptionView;

        protected final ImageView imageView;


        public ViewHolder(@NonNull View view) {
            titleView = (TextView) view.findViewById(R.id.title);
            yearView = (TextView) view.findViewById(R.id.year);
            descriptionView = (TextView) view.findViewById(R.id.description);
            imageView = (ImageView) view.findViewById(R.id.image);
        }

        /**
         * Map movie object to view widgets
         *
         * @param movie current selected object
         */
        public void setData(Movie movie) {
            titleView.setText(movie.getTitle());
            yearView.setText(movie.getMeta().getReleaseYear());
            descriptionView.setText(movie.getDescription());

            Display display = getActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;

            imageView.setImageBitmap(ImageHelper.decodeSampledBitmapFromResource(getResources(), "images/" + movie.getImages().getPlaceholder(), width, width));
        }
    }
}
