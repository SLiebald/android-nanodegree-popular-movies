package com.liebald.popularmovies.ui.detail;

import android.content.ContentValues;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.liebald.popularmovies.R;
import com.liebald.popularmovies.data.MovieContract;
import com.liebald.popularmovies.databinding.ActivityDetailBinding;
import com.liebald.popularmovies.model.MoviePreview;
import com.liebald.popularmovies.utilities.AppExecutors;
import com.liebald.popularmovies.utilities.BitmapConverter;

import java.util.concurrent.Executor;

/**
 * This class implements the logic behind the DetailActivity, which shows the Details
 * of a {@link MoviePreview}.
 */
public class DetailActivity extends AppCompatActivity {

    /**
     * Tag for Logging in this activity.
     */
    private static final String TAG = DetailActivity.class.getSimpleName();
    /**
     * The {@link MoviePreview} displayed in the current detail screen.
     */
    MoviePreview preview;
    /**
     * The data binding for the UI.
     */
    private ActivityDetailBinding binding;
    /**
     * Executor for talking to the Content Provider
     */
    private Executor diskIoExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        Bundle extras = getIntent().getExtras();


        if (extras != null) {
            if ((preview = getIntent().getExtras().getParcelable("moviePreview")) == null) {
                return;
            }
        } else return;
        // Setup the viewpager
        DetailsFragmentPagerAdapter detailsFragmentPagerAdapter = new DetailsFragmentPagerAdapter(this, getSupportFragmentManager(), preview.getMovie_id());
        ViewPager viewPager = binding.detailInfo.viewpagerDetails;
        viewPager.setAdapter(detailsFragmentPagerAdapter);
        TabLayout tabLayout = binding.detailInfo.tabsDetails;
        tabLayout.setupWithViewPager(binding.detailInfo.viewpagerDetails);
        setTitle(preview.getTitle());

        fillLayout(preview);
        Log.d(TAG, "Detailed Preview filled");
        diskIoExecutor = AppExecutors.getInstance().diskIO();
        binding.detailMaster.requestFocus();

    }

    /**
     * Fills the layout with the information from the given {@link MoviePreview}.
     *
     * @param preview The Details for the {@link MoviePreview}
     */

    private void fillLayout(MoviePreview preview) {
        if (preview.getOverview() != null && !preview.getOverview().isEmpty())
            binding.detailInfo.tvOverviewText.setText(preview.getOverview());
        if (preview.getRelease_date() != null && !preview.getRelease_date().isEmpty())
            binding.tvReleaseDateText.setText(preview.getRelease_date());
        if (preview.getVote_average() != null && !preview.getVote_average().isEmpty())
            binding.tvVoteAverageText.setText(getString(R.string.detail_rating, preview.getVote_average()));
        if (preview.getImage_thumbail() != null) {
            binding.imagePreview.setImageBitmap(preview.getImage_thumbail());
        }
    }

    /**
     * Sets the favorite icon depending on whether the movie is cached or not.
     *
     * @param menu The menu that holds the favorite item.
     */
    private void checkFavoriteStatus(Menu menu) {
        diskIoExecutor.execute(() -> {
            Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.buildMovieUriWithId(preview.getMovie_id()),
                    new String[]{MovieContract.MovieEntry.COLUMN_MOVIE_ID},
                    null,
                    null,
                    null);
            MenuItem item = menu.findItem(R.id.menu_favorite);
            if (cursor != null && cursor.getCount() > 0) {
                runOnUiThread(() -> {
                    item.setTitle(R.string.favorite_icon);
                    item.setIcon(R.drawable.ic_favorite_white_24dp);
                });
            } else {
                runOnUiThread(() -> {
                    item.setTitle(R.string.not_favorite_icon);
                    item.setIcon(R.drawable.ic_favorite_border_white_24dp);
                });

            }
            if (cursor != null)
                cursor.close();
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.details_menu, menu);
        checkFavoriteStatus(menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_favorite:
                if (item.getTitle().equals(getString(R.string.favorite_icon))) {
                    removeMovieFromCache(item);
                } else {
                    addMovieToLocalCache(item);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Adds the current Movie to the local {@link android.content.ContentProvider}.
     */

    private void addMovieToLocalCache(MenuItem item) {
        diskIoExecutor.execute(() -> {
            // collect all the data
            ContentValues movieValues = new ContentValues();
            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, preview.getMovie_id());
            movieValues.put(MovieContract.MovieEntry.COLUMN_DESCRIPTION, preview.getOverview());
            movieValues.put(MovieContract.MovieEntry.COLUMN_RATING, preview.getVote_average());
            movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, preview.getRelease_date());
            movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, preview.getTitle());
            if (binding.imagePreview.getDrawable() != null) {
                Bitmap bitmap = ((BitmapDrawable) binding.imagePreview.getDrawable()).getBitmap();
                movieValues.put(MovieContract.MovieEntry.COLUMN_THUMBNAIL, BitmapConverter.getBytes(bitmap));
            }
            // insert it into the database.
            getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, movieValues);
            Log.d(TAG, "Inserted movie into local database.");

            runOnUiThread(() -> {
                item.setTitle(R.string.favorite_icon);
                item.setIcon(R.drawable.ic_favorite_white_24dp);
            });

        });

    }

    /**
     * Removes the current Movie from the local {@link android.content.ContentProvider}.
     */
    private void removeMovieFromCache(MenuItem item) {
        diskIoExecutor.execute(() -> {
            int affectedRows = getContentResolver().delete(MovieContract.MovieEntry.buildMovieUriWithId(preview.getMovie_id()), null, null);
            Log.d(TAG, "Deleted movie from local database. " + affectedRows + " rows affected.");
            runOnUiThread(() -> {
                item.setTitle(R.string.not_favorite_icon);
                item.setIcon(R.drawable.ic_favorite_border_white_24dp);
            });
        });
    }

    /**
     * Updates the Background color of the activity depending on the bitmap image of the
     * preview poster.
     */
    // inspired by https://stackoverflow.com/questions/25673021/changing-the-background-of-a-view-to-match-the-color-of-the-album-art
    private void updateBackground() {
        Bitmap bitmap = ((BitmapDrawable) binding.imagePreview.getDrawable()).getBitmap();
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(@NonNull Palette palette) {
                Palette.Swatch swatch = palette.getVibrantSwatch();
                if (swatch == null) swatch = palette.getMutedSwatch();
                if (swatch != null) {
                    binding.detailMaster.setBackgroundColor(swatch.getRgb());
                    binding.detailInfo.tvOverviewLabel.setTextColor(swatch.getTitleTextColor());
                    binding.detailInfo.tvOverviewText.setTextColor(swatch.getBodyTextColor());
                    binding.tvReleaseDateLabel.setTextColor(swatch.getTitleTextColor());
                    binding.tvReleaseDateText.setTextColor(swatch.getBodyTextColor());
                    binding.tvVoteAverageLabel.setTextColor(swatch.getTitleTextColor());
                    binding.tvVoteAverageText.setTextColor(swatch.getBodyTextColor());
                }

            }
        });
    }
}
