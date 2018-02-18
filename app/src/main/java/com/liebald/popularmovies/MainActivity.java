package com.liebald.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
//TODO: Attribute TMDb:
// You shall use the TMDb logo to identify your use of the TMDb APIs.
// You shall place the following notice prominently on your application:
// This product uses the TMDb API but is not endorsed or certified by TMDb."
// Any use of the TMDb logo in your application shall be less prominent than
// the logo or mark that primarily describes the application and your use of
// the TMDb logo shall not imply any endorsement by TMDb.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
