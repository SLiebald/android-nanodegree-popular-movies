package com.liebald.popularmovies.Loader;


import android.app.Activity;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.liebald.popularmovies.model.MoviePreview;
import com.liebald.popularmovies.utilities.JsonUtils;
import com.liebald.popularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * AsyncTaskLoader for retrieving MoviePreviews from the website.
 */
public class MoviePreviewAsyncTaskLoader extends AsyncTaskLoader<List<MoviePreview>> {


    private final static String TAG = MoviePreviewAsyncTaskLoader.class.getSimpleName();


    public MoviePreviewAsyncTaskLoader(Activity activity) {
        super(activity);
        Log.i(TAG, "AsyncTaskLoader initialized");
    }

    @Override
    public List<MoviePreview> loadInBackground() {
        try {
            String jsonMoviePreviews = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.getUrl(NetworkUtils.SortBy.popularity));
            return JsonUtils.parseMoviePreviews(jsonMoviePreviews);
        } catch (IOException e) {
            Log.e(TAG, "Error loading from URL: " + e.getMessage());
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public void deliverResult(List<MoviePreview> result) {
        super.deliverResult(result);
        Log.i(TAG, "Finished loading");
    }
}
