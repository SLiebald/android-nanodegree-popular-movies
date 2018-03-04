package com.liebald.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines table and column names for the movie preview database.
 */
// Based on the WeatherContract for Project Sunshine.
public class MovieContract {

    /**
     * The "Content authority" used for this content provider.
     */
    public static final String CONTENT_AUTHORITY = "com.liebald.popularmovies";

    /**
     * Create the base URI which apps will use to contact
     * the content provider of the popular movies app.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * The path for querying movie data.
     */
    public static final String PATH_MOVIE = "movie";

    /* Inner class that defines the table contents of the movies table */
    public static final class MovieEntry implements BaseColumns {

        /**
         * The base CONTENT_URI used to query the Movies table from the content provider
         */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIE)
                .build();

        /**
         * Used internally as the name of our movies table.
         */
        public static final String TABLE_NAME = "movies";

        /**
         * Movie ID as returned by the tmdp API.
         */
        public static final String COLUMN_MOVIE_ID = "movie_id";

        /**
         * Movie overview is stored as String.
         */
        public static final String COLUMN_DESCRIPTION = "overview";

        /**
         * Movie release date is stored as String.
         */
        public static final String COLUMN_RELEASE_DATE = "release";

        /**
         * Movie rating is stored as float/real.
         */
        public static final String COLUMN_RATING = "rating";

        /**
         * Movie preview image is stored as a binary large object.
         */
        public static final String COLUMN_THUMBNAIL = "thumbnail";

        /**
         * Builds a URI that adds the movie id to the end of the movie content URI path.
         * This is used to query the details about a specific movie by its id.
         *
         * @param id The tmdb ID of a movie.
         * @return Uri to query details about a single movie entry
         */
        public static Uri buildMovieUriWithId(int id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(id))
                    .build();
        }

    }
}