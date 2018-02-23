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

    private final NetworkUtils.requestType requestType;

    public MoviePreviewAsyncTaskLoader(Activity activity, String requestType) {
        super(activity);
        Log.i(TAG, "AsyncTaskLoader initialized");
        this.requestType = NetworkUtils.requestType.valueOf(requestType);
    }

    @Override
    public List<MoviePreview> loadInBackground() {
        try {
            Log.d(TAG, requestType.name());
            String jsonMoviePreviews = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.getUrl(requestType));
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
