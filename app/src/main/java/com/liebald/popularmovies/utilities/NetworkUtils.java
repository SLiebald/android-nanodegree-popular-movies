package com.liebald.popularmovies.utilities;


import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.liebald.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Utility class for querying the network
 */
// Based on the NetworkUtils for Project Sunshine.
public class NetworkUtils {

    /**
     * Tag for Logging in this activity.
     */
    private static final String TAG = NetworkUtils.class.getSimpleName();

    /**
     * Base Url for searching movies on themoviedb.org.
     */
    private static final String MOVIE_PREVIEW_BASE_URL = "https://api.themoviedb.org/3/movie";

    /**
     * Base Url for images from tmdb.
     */
    private static final String MOVIE_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";

    /**
     * Query parameter for the api key.
     */
    private static final String API_KEY_PARAM = "api_key";


    /**
     * Create the query URL for retrieving the JSON data for movie previews.
     * Input defines on what kind of movies to query.
     *
     * @param requestType {@link requestType} defining what data should be requested (popular/top_rated)
     * @return The URL that queries for movies following the given parameters.
     */
    public static URL getUrl(@NonNull requestType requestType) {
        Uri moviePreviewUri = Uri.parse(MOVIE_PREVIEW_BASE_URL).buildUpon().appendPath(requestType.name())
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.TMDB_API_KEY)
                .build();
        try {
            return new URL(moviePreviewUri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, "Malformed URL from URI: " + moviePreviewUri);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Executes a HTTP query to the given {@link URL} and returns the result.
     *
     * @param url URL to query for the HTTP response.
     * @return The content of the HTTP response, null if no response.
     * @throws IOException If an error on network operation occurred.
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner = scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }

    /**
     * Returns the complete Path for the given relative poster path.
     *
     * @param posterPath The relative path.
     * @return The {@link Uri} that points to the according image resource.
     */
    public static Uri getThumbnailURL(String posterPath) {
        return Uri.parse(MOVIE_IMAGE_BASE_URL).buildUpon()
                .appendPath("w342")
                .appendEncodedPath(posterPath)
                .build();
    }

    /**
     * Enum with possible parameters to sort the result by.
     */
    public enum requestType {
        popular,
        top_rated
    }


}
