package com.liebald.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.liebald.popularmovies.model.MoviePreview;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        MoviePreview preview = getIntent().getExtras().getParcelable("moviePreview");
        Log.d(TAG, preview.getTitle());
        Log.d(TAG, preview.getOverview());
        Log.d(TAG, preview.getVote_average());
        Log.d(TAG, preview.getRelease_date());
        Log.d(TAG, preview.getPosterPath());
        setTitle(preview.getTitle());
    }
}
