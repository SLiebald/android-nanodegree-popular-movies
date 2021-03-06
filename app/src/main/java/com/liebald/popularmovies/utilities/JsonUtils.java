package com.liebald.popularmovies.utilities;

import android.util.Log;

import com.liebald.popularmovies.model.MoviePreview;
import com.liebald.popularmovies.model.Review;
import com.liebald.popularmovies.model.Video;

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
     * Json key for accessing a poster path.
     */
    private static final String POSTER_PATH_KEY = "poster_path";

    /**
     * Json key for accessing a movie title.
     */
    private static final String TITLE_KEY = "title";

    /**
     * Json key for accessing a movie overview description.
     */
    private static final String OVERVIEW_KEY = "overview";

    /**
     * Json key for accessing a movies average votes.
     */
    private static final String VOTE_AVERAGE_KEY = "vote_average";

    /**
     * Json key for accessing a movies release date.
     */
    private static final String RELEASE_DATE_KEY = "release_date";

    /**
     * Json key for accessing a movies id.
     */
    private static final String ID_KEY = "id";

    /**
     * Json key for accessing a reviews author.
     */
    private static final String REVIEWER_KEY = "author";

    /**
     * Json key for accessing a reviews content.
     */
    private static final String REVIEW_KEY = "content";

    /**
     * Json key for accessing a videos key.
     */
    private static final String VIDEO_KEY_KEY = "key";

    /**
     * Json key for accessing the videos name.
     */
    private static final String VIDEO_NAME_KEY = "name";


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

            if (jsonMoviePreview.has(ID_KEY))
                moviePreview.setMovie_id(jsonMoviePreview.optInt(ID_KEY));

            return moviePreview;
        } catch (JSONException exception) {
            Log.e(TAG, "Error parsing the json String: " + exception);
            return null;
        }
    }


    /**
     * Parses a json String containing multiple movie reviews into a {@link List} of {@link Review}s.
     *
     * @param jsonObject The json to parse.
     * @return The parsed {@link List} of {@link Review}s. Null if an error occurred.
     */
    public static List<Review> parseMovieReviews(JSONObject jsonObject) {
        ArrayList<Review> movieReviews = new ArrayList<>();

        try {
            if (!jsonObject.has(RESULTS_KEY))
                return movieReviews;
            JSONArray results = jsonObject.getJSONArray(RESULTS_KEY);

            for (String result : readListFromJsonArray(results)) {

                Review review = parseMovieReview(result);
                if (review != null)
                    movieReviews.add(review);
            }
            return movieReviews;
        } catch (JSONException exception) {
            Log.e(TAG, "Error parsing the json String: " + exception);
        }
        return movieReviews;
    }

    /**
     * Parses a single movie review from json.
     *
     * @param json Json containing a single review.
     * @return The parsed {@link Review}.
     */
    private static Review parseMovieReview(String json) {
        try {
            Review movieReview = new Review();
            JSONObject jsonMovieReview = new JSONObject(json);
            if (jsonMovieReview.has(REVIEW_KEY))
                movieReview.setReview(jsonMovieReview.optString(REVIEW_KEY));

            if (jsonMovieReview.has(REVIEWER_KEY))
                movieReview.setReviewer(jsonMovieReview.optString(REVIEWER_KEY));

            if (jsonMovieReview.has(ID_KEY))
                movieReview.setReview_id(jsonMovieReview.optInt(ID_KEY));

            return movieReview;
        } catch (JSONException exception) {
            Log.e(TAG, "Error parsing the json String: " + exception);
            return null;
        }
    }


    /**
     * Parses a json String containing multiple movie reviews into a {@link List} of {@link Video}s.
     *
     * @param jsonObject The json to parse.
     * @return The parsed {@link List} of {@link Video}s. Null if an error occurred.
     */
    public static List<Video> parseMovieVideos(JSONObject jsonObject) {
        ArrayList<Video> videos = new ArrayList<>();

        try {
            if (!jsonObject.has(RESULTS_KEY))
                return videos;
            JSONArray results = jsonObject.getJSONArray(RESULTS_KEY);

            for (String result : readListFromJsonArray(results)) {

                Video video = parseMovieVideo(result);
                if (video != null)
                    videos.add(video);
            }
            return videos;
        } catch (JSONException exception) {
            Log.e(TAG, "Error parsing the json String: " + exception);
        }
        return videos;
    }

    /**
     * Parses a single movie video data from json.
     *
     * @param json Json containing a single review.
     * @return The parsed {@link Video}.
     */
    private static Video parseMovieVideo(String json) {
        try {
            Video video = new Video();
            JSONObject jsonVideo = new JSONObject(json);
            if (jsonVideo.has(VIDEO_KEY_KEY))
                video.setKey(jsonVideo.optString(VIDEO_KEY_KEY));

            if (jsonVideo.has(VIDEO_NAME_KEY))
                video.setName(jsonVideo.optString(VIDEO_NAME_KEY));

            return video;
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
