package com.liebald.popularmovies.ui.about;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.liebald.popularmovies.R;

/**
 * Activity for About texts like giving credits for tmdb for using their API.
 */
public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
}
