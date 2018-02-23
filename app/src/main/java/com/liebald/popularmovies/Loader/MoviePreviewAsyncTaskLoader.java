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

    /**
     * Tag for Logging in this activity.
     */
    private final static String TAG = MoviePreviewAsyncTaskLoader.class.getSimpleName();

    /**
     * The requestType of the request that should be carried out. E.g. popular or top_rated
     */
    private final NetworkUtils.requestType requestType;

    /**
     * Constructor for a {@link MoviePreviewAsyncTaskLoader}.
     *
     * @param activity    The activity that created the Loader. Passed to super constructor.
     * @param requestType The requestType of the request that should be carried out.
     *                    E.g. popular or top_rated
     */
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
