package com.norigin.datastorage;

import android.content.Context;
import android.util.Log;

import com.bluelinelabs.logansquare.LoganSquare;
import com.norigin.entity.Movie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for loading movies from storage
 *
 * Created by ibohdan on 5/2/2016.
 */
public class MovieStorage {

    private static final String TAG = MovieStorage.class.getSimpleName();

    private Context context;

    public MovieStorage(Context context) {
        this.context = context;
    }

    /**
     * Parse movies.json
     *
     * @return array of loaded movies
     */
    public List<Movie> loadMovies() {
        try {
            return LoganSquare.parseList(context.getAssets().open("movies.json"), Movie.class);
        } catch (IOException e) {
            Log.e(TAG, "loadMovies", e);
        }
        return new ArrayList<>();
    }
}
