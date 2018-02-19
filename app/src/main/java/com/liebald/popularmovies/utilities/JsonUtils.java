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
    private static final String TAG = JsonUtils.class.getSimpleName();

    private static final String RESULTS_KEY = "results";
    private static final String POSTER_PATH_KEY = "poster_path";
    private static final String TITLE_KEY = "title";
    private static final String OVERVIEW_KEY = "overview";
    private static final String VOTE_AVERAGE_KEY = "vote_average";
    private static final String RELEASE_DATE_KEY = "release_date";

    /**
     * Parses a json String containing multiple movie Previews into a {@link List} of {@link MoviePreview}s.
     *
     * @param json The json to parse.
     * @return The parsed {@link List} of {@link MoviePreview}s. Null if an error occurred.
     */
    public static List<MoviePreview> parseMoviePreviews(String json) {
        try {
            ArrayList<MoviePreview> moviePreviews = new ArrayList<>();

            JSONObject jsonMoviePreviews = new JSONObject(json);
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
        return null;
    }

    /**
     * Parses a single movie preview from json.
     *
     * @param json Json containing a single MoviePreview.
     * @return The parsed {@link MoviePreview}.
     */
    private static MoviePreview parseMoviePreview(String json) {
        //TODO: complete with the other moviePreview Information.
        try {
            MoviePreview moviePreview = new MoviePreview();
            JSONObject jsonMoviePreview = new JSONObject(json);
            moviePreview.setPosterPath(jsonMoviePreview.getString(POSTER_PATH_KEY));
            moviePreview.setTitle(jsonMoviePreview.getString(TITLE_KEY));
            moviePreview.setOverview(jsonMoviePreview.getString(OVERVIEW_KEY));
            moviePreview.setVote_average(jsonMoviePreview.getString(VOTE_AVERAGE_KEY));
            moviePreview.setRelease_date(jsonMoviePreview.getString(RELEASE_DATE_KEY));
//            Log.d(TAG, moviePreview.getPosterPath());
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
     * @throws JSONException Thrown if a parsing error occurs.
     */
    private static List<String> readListFromJsonArray(JSONArray array) throws JSONException {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            result.add(array.getString(i));
        }
        return result;
    }

}
