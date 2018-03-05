package com.liebald.popularmovies.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.liebald.popularmovies.Loader.MoviePreviewAsyncTaskLoader;
import com.liebald.popularmovies.R;
import com.liebald.popularmovies.data.MovieContract;
import com.liebald.popularmovies.model.MoviePreview;
import com.liebald.popularmovies.ui.about.AboutActivity;
import com.liebald.popularmovies.ui.detail.DetailActivity;
import com.liebald.popularmovies.utilities.AppExecutors;
import com.liebald.popularmovies.utilities.BitmapConverter;
import com.liebald.popularmovies.utilities.NetworkUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity implements MoviesPreviewAdapter.GridItemClickListener,
        LoaderManager.LoaderCallbacks<List<MoviePreview>> {

    /**
     * Tag for Logging in this activity.
     */
    private static final String TAG = MainActivity.class.getSimpleName();

    /**
     * The ID of the Loader used to request data over the network.
     */
    private static final int ID_MOVIE_PREVIEW_LOADER = 42;

    /**
     * The Key for an Intent extra that specifies what exactly is requested.
     */
    private static final String REQUEST_TYPE_KEY = "request_type";

    /**
     * The disabled {@link MenuItem} that is used to display the type of the currently requested
     * Previews. E.g. Popular or Top rated.
     */
    private MenuItem menu_request_type;

    /**
     * The {@link RecyclerView} displaying the movie previews.
     */
    private RecyclerView mPreviewList;

    /**
     * The {@link ProgressBar} shown while loading the content via the loader.
     */
    private ProgressBar mProgressBar;

    /**
     * The Adapter for managing the {@link RecyclerView} displaying the preview.
     */
    private MoviesPreviewAdapter mAdapter;

    /**
     * Executor for talking to the Content Provider
     */
    private Executor diskIoExecutor;

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
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);
        //GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);

        mPreviewList.setLayoutManager(layoutManager);

        //setup the mAdapter
        mAdapter = new MoviesPreviewAdapter(this, this);
        mPreviewList.setAdapter(mAdapter);

        diskIoExecutor = AppExecutors.getInstance().diskIO();


        if (!isNetworkAvailable()) {
            loadFavorites();
            Toast.makeText(this, "No Internet available, showing favorites.", Toast.LENGTH_SHORT).show();
        } else {
            Bundle bundle = new Bundle();
            //Set the default request type to popular movies
            bundle.putString(REQUEST_TYPE_KEY, NetworkUtils.requestType.popular.name());
            getSupportLoaderManager().initLoader(ID_MOVIE_PREVIEW_LOADER, bundle, this);
        }
    }

    @Override
    public void onGridItemClick(int clickedItemIndex) {
        MoviePreview moviePreview = mAdapter.getItem(clickedItemIndex);
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("moviePreview", moviePreview);
        intent.getParcelableExtra("moviePreview");
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        menu_request_type = menu.findItem(R.id.menu_request_type);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // without internet we want to show the favorites.
        if (!isNetworkAvailable() && (item.getItemId() == R.id.menu_popular || item.getItemId() == R.id.menu_top_rated)) {
            loadFavorites();
            Toast.makeText(this, "No Internet available, showing favorites.", Toast.LENGTH_SHORT).show();
            return true;
        }
        Bundle bundle = new Bundle();

        switch (item.getItemId()) {
            case R.id.menu_popular:
                bundle.putString(REQUEST_TYPE_KEY, NetworkUtils.requestType.popular.name());
                menu_request_type.setTitle(getString(R.string.popular));
                break;
            case R.id.menu_top_rated:
                bundle.putString(REQUEST_TYPE_KEY, NetworkUtils.requestType.top_rated.name());
                menu_request_type.setTitle(getString(R.string.top_rated));
                break;
            case R.id.menu_attribution:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_favorite:
                loadFavorites();
                menu_request_type.setTitle(R.string.favorites);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        getSupportLoaderManager().restartLoader(ID_MOVIE_PREVIEW_LOADER, bundle, this);
        return true;
    }

    private void loadFavorites() {
        diskIoExecutor.execute(() -> {
            Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);

            List<MoviePreview> previews = new ArrayList<>();
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    MoviePreview preview = new MoviePreview();
                    preview.setOverview(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_DESCRIPTION)));
                    preview.setMovie_id(cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID)));
                    preview.setRelease_date(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE)));
                    preview.setVote_average(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATING)));
                    preview.setTitle(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE)));

                    preview.setImage_thumbail(BitmapConverter.getImage(cursor.getBlob(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_THUMBNAIL))));
                    previews.add(preview);
                    Log.d(TAG, "" + preview.getImage_thumbail());
                }
                cursor.close();
                runOnUiThread(() -> {
                    mAdapter.swapItems(previews);
                });
            }
        });
    }

    @NonNull
    @Override
    public Loader<List<MoviePreview>> onCreateLoader(int id, Bundle args) {
        MoviePreviewAsyncTaskLoader mLoader = new MoviePreviewAsyncTaskLoader(this, args.getString(REQUEST_TYPE_KEY));
        mLoader.forceLoad();
        Log.i(TAG, "Started Loader");
        return mLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<MoviePreview>> loader, List<MoviePreview> data) {
        mAdapter.swapItems(data);
        mProgressBar.setVisibility(View.GONE);
        mPreviewList.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<MoviePreview>> loader) {
        //TODO: anything to do here?
    }

    /**
     * Method that checks whether network is available.
     *
     * @return True if a network is available.
     */
    // based on https://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

}
