package com.liebald.popularmovies;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.liebald.popularmovies.Loader.MoviePreviewAsyncTaskLoader;
import com.liebald.popularmovies.model.MoviePreview;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MoviesPreviewAdapter.GridItemClickListener, LoaderManager.LoaderCallbacks<List<MoviePreview>> {
    //TODO: Attribute TMDb:
// You shall use the TMDb logo to identify your use of the TMDb APIs.
// You shall place the following notice prominently on your application:
// This product uses the TMDb API but is not endorsed or certified by TMDb."
// Any use of the TMDb logo in your application shall be less prominent than
// the logo or mark that primarily describes the application and your use of
// the TMDb logo shall not imply any endorsement by TMDb.
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int ID_MOVIE_PREVIEW_LOADER = 42;
    /**
     * The {@link RecyclerView} displaying the movie previews.
     */
    private RecyclerView mPreviewList;
    private ProgressBar mProgressBar;
    /**
     * The Adapter for managing the {@link RecyclerView} displaying the preview.
     */
    private MoviesPreviewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find the RecyclerView
        mPreviewList = findViewById(R.id.recycler_main);
        mProgressBar = findViewById(R.id.pb_loading);
        // set its LayoutManager to a gridLayoutManager

        int spanCount = 2;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            spanCount = 4;
        }

        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        mPreviewList.setLayoutManager(layoutManager);


        //setup the mAdapter
        mAdapter = new MoviesPreviewAdapter(this, this);
        mPreviewList.setAdapter(mAdapter);

        getSupportLoaderManager().initLoader(ID_MOVIE_PREVIEW_LOADER, null, this);

    }


    @Override
    public void onGridItemClick(int clickedItemIndex) {
        MoviePreview moviePreview = mAdapter.getItem(clickedItemIndex);
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("moviePreview", moviePreview);
        startActivity(intent);

    }

    @Override
    public Loader<List<MoviePreview>> onCreateLoader(int id, Bundle args) {
        MoviePreviewAsyncTaskLoader mLoader = new MoviePreviewAsyncTaskLoader(this);
        mLoader.forceLoad();
        Log.i(TAG, "Started Loader");
        return mLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<MoviePreview>> loader, List<MoviePreview> data) {
        mAdapter.swapItems(data);

        mProgressBar.setVisibility(View.GONE);
        mPreviewList.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<List<MoviePreview>> loader) {
        //TODO: anything to do here?
    }
}
