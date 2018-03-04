package com.liebald.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.liebald.popularmovies.data.MovieContract.MovieEntry;

/**
 * Manages a local database for movies data.
 */
// Based on the DbHelper for Project Sunshine.
public class MovieDbHelper extends SQLiteOpenHelper {

    /**
     * The name of the database.
     */
    public static final String DATABASE_NAME = "movies.db";

    /**
     * The version of the database.
     */
    private static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        // Create statement for the movies table in the database.

        final String SQL_CREATE_MOVIES_TABLE =

                "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +

                        // The movie ID should be unique and can be used as primary key.
                        MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                        MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                        MovieEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL," +
                        MovieEntry.COLUMN_RATING + " REAL NOT NULL, " +
                        MovieEntry.COLUMN_THUMBNAIL + " BLOB NOT NULL, " +
                        // In case a movie already exists in the database just update it.
                        " UNIQUE (" + MovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";

        // Create the table in the database.
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Just recreate the db in case of a version upgrade.
        // This deletes the favorites of a user!
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}