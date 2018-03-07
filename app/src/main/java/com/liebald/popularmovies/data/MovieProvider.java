/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.liebald.popularmovies.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * {@link ContentProvider} for the locally stored favorites of the popularmovies app.
 */
// Based on the ContentProvider for Project Sunshine.
public class MovieProvider extends ContentProvider {

    // Constants for mapping URIs with the requested data. Used by the {@link UriMatcher}.
    private static final int CODE_MOVIE = 100;
    private static final int CODE_MOVIE_WITH_ID = 101;
    /**
     * Tag for Logging in this activity.
     */
    private static final String TAG = MovieProvider.class.getSimpleName();
    /*
     * The URI Matcher used by this content provider.
     */
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mMovieDbHelper;

    /**
     * Creates the UriMatcher that will match each URI to the CODE_MOVIE and
     * CODE_MOVIE_WITH_ID constants defined above.
     *
     * @return A UriMatcher that correctly matches the constants for CODE_MOVIE and CODE_MOVIE_WITH_ID
     */
    private static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        // This URI is content://com.liebald.popularmovies/movie/
        matcher.addURI(authority, MovieContract.PATH_MOVIE, CODE_MOVIE);

        /*
         * Example URI: content://com.liebald.popularmovies/movie/235
         * The "/#" means that if PATH_MOVIE is followed by a number,
         * it has to return the CODE_MOVIE_WITH_ID code.
         * This number is the ID of the requested movie.
         */
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/#", CODE_MOVIE_WITH_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mMovieDbHelper = new MovieDbHelper(getContext());
        return true;
    }


    /**
     * Inserts the details of a single movie preview into the database.
     *
     * @param uri   The URI of the insertion request. This must not be null.
     * @param value A set of column_name/value pairs to add to the database.
     *              This must not be null
     * @return The URI of the inserted data.
     */
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues value) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)) {
            case CODE_MOVIE:
                int id = value.getAsInteger(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
                db.insert(MovieContract.MovieEntry.TABLE_NAME, null, value);
                if (getContext() != null)
                    getContext().getContentResolver().notifyChange(uri, null);
                Uri baseUri = MovieContract.BASE_CONTENT_URI.buildUpon().path(MovieContract.PATH_MOVIE).build();
                Log.d(TAG, "Inserted movie with base uri " + baseUri.toString() + " and id " + id);
                return ContentUris.withAppendedId(baseUri, id);
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri + " only inserts without parameters are supported");
        }
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        throw new RuntimeException(
                "Bulk insert not implemented since one can only mark one movie at a time as favorite.");
    }

    /**
     * Handles query requests from clients of this {@link ContentProvider}. Can be queried for all movie data stored or individual
     * movies based on their id.
     *
     * @param uri           The URI to query
     * @param projection    The list of columns to put into the cursor. If null, all columns are
     *                      included.
     * @param selection     A selection criteria to apply when filtering rows. If null, then all
     *                      rows are included.
     * @param selectionArgs You may include ?s in selection, which will be replaced by
     *                      the values from selectionArgs, in order that they appear in the
     *                      selection.
     * @param sortOrder     How the rows in the cursor should be sorted.
     * @return A Cursor containing the results of the query.
     */
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Cursor cursor;

        switch (sUriMatcher.match(uri)) {
            // query for a specific movie.
            case CODE_MOVIE_WITH_ID: {

                // The movie ID is the last part of the URI.
                String id = uri.getLastPathSegment();
                // set the selectionArguments to match the given id.
                String[] selectionArguments = new String[]{id};

                cursor = mMovieDbHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,

                        projection,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);
                break;
            }

            // If the id at the end is missing all db entries should be returned.
            case CODE_MOVIE: {
                cursor = mMovieDbHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (getContext() != null)
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    /**
     * Deletes a movie data at a given URI. Only supports deletion of one item at a time.
     *
     * @param uri           The full URI to the item that should be deleted.
     * @param selection     An optional restriction to apply to rows when deleting.
     * @param selectionArgs Used in conjunction with the selection statement
     * @return The number of rows deleted
     */
    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        /* Users of the delete method will expect the number of rows deleted to be returned. */
        int numRowsDeleted;

        switch (sUriMatcher.match(uri)) {

            case CODE_MOVIE_WITH_ID:
                // The movie ID is the last part of the URI.
                String id = uri.getLastPathSegment();
                // set the selectionArguments to match the given id.
                String[] selectionArguments = new String[]{id};
                numRowsDeleted = mMovieDbHelper.getWritableDatabase().delete(
                        MovieContract.MovieEntry.TABLE_NAME,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ",
                        selectionArguments);
                break;
            default:
                // Since a user can only remove one movie at a time from his favorites we don't need the
                // possibility to delete all entries at once.
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        /* If we actually deleted any rows, notify that a change has occurred to this URI */
        if (numRowsDeleted != 0 && getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mMovieDbHelper.close();
        super.shutdown();
    }
}