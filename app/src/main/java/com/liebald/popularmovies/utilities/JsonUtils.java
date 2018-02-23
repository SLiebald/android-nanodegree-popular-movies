package com.liebald.popularmovies.utilities;

import android.util.Log;

import com.liebald.popularmovies.model.MoviePreview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for parsing json into {@link MoviePreview} objects.
 */
public class JsonUtils {

    /**
     * Tag for Logging in this activity.
     */
    private static final String TAG = JsonUtils.class.getSimpleName();

    /**
     * Json key for accessing query results.
     */
    private static final String RESULTS_KEY = "results";

    /**
     * Json key for accessing the poster path.
     */
    private static final String POSTER_PATH_KEY = "poster_path";

    /**
     * Json key for accessing the movie title.
     */
    private static final String TITLE_KEY = "title";

    /**
     * Json key for accessing the movie overview description.
     */
    private static final String OVERVIEW_KEY = "overview";

    /**
     * Json key for accessing the movies average votes.
     */
    private static final String VOTE_AVERAGE_KEY = "vote_average";

    /**
     * Json key for accessing the movies release date.
     */
    private static final String RELEASE_DATE_KEY = "release_date";

    /**
     * Parses a json String containing multiple movie Previews into a {@link List} of {@link MoviePreview}s.
     *
     * @param json The json to parse.
     * @return The parsed {@link List} of {@link MoviePreview}s. Null if an error occurred.
     */
    public static List<MoviePreview> parseMoviePreviews(String json) {
        ArrayList<MoviePreview> moviePreviews = new ArrayList<>();

        try {
            JSONObject jsonMoviePreviews = new JSONObject(json);
            if (!jsonMoviePreviews.has(RESULTS_KEY))
                return moviePreviews;
            JSONArray results = jsonMoviePreviews.getJSONArray(RESULTS_KEY);

            for (String result : readListFromJsonArray(results)) {

                MoviePreview preview = parseMoviePreview(result);
                if (preview != null)
                    moviePreviews.add(preview);
            }
            return moviePreviews;
        } catch (JSONException exception) {
            Log.e(TAG, "Error parsing the json String: " + exception);
        }
        return moviePreviews;
    }

    /**
     * Parses a single movie preview from json.
     *
     * @param json Json containing a single MoviePreview.
     * @return The parsed {@link MoviePreview}.
     */
    private static MoviePreview parseMoviePreview(String json) {
        try {
            MoviePreview moviePreview = new MoviePreview();
            JSONObject jsonMoviePreview = new JSONObject(json);
            if (jsonMoviePreview.has(POSTER_PATH_KEY))
                moviePreview.setPosterPath(jsonMoviePreview.optString(POSTER_PATH_KEY));

            if (jsonMoviePreview.has(TITLE_KEY))
                moviePreview.setTitle(jsonMoviePreview.optString(TITLE_KEY));

            if (jsonMoviePreview.has(OVERVIEW_KEY))
                moviePreview.setOverview(jsonMoviePreview.optString(OVERVIEW_KEY));

            if (jsonMoviePreview.has(VOTE_AVERAGE_KEY))
                moviePreview.setVote_average(jsonMoviePreview.optString(VOTE_AVERAGE_KEY));

            if (jsonMoviePreview.has(RELEASE_DATE_KEY))
                moviePreview.setRelease_date(jsonMoviePreview.optString(RELEASE_DATE_KEY));
            return moviePreview;
        } catch (JSONException exception) {
            Log.e(TAG, "Error parsing the json String: " + exception);
            return null;
        }
    }


    /**
     * Helper method to pars a {@link JSONArray} as {@link List <String>}.
     *
     * @param array The {@link JSONArray} to pars.
     * @return The parsed {@link List<String>}.
     */
    private static List<String> readListFromJsonArray(JSONArray array) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            result.add(array.optString(i));
        }
        return result;
    }

}
