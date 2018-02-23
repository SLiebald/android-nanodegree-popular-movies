package com.liebald.popularmovies;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.util.Log;

import com.liebald.popularmovies.databinding.ActivityDetailBinding;
import com.liebald.popularmovies.model.MoviePreview;
import com.liebald.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

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
     * The data binding for the UI.
     */
    private ActivityDetailBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        Bundle extras = getIntent().getExtras();


        MoviePreview preview;
        if (extras != null) {
            if ((preview = getIntent().getExtras().getParcelable("moviePreview")) == null) {
                return;
            }
        } else return;


        setTitle(preview.getTitle());

        fillLayout(preview);
        Log.d(TAG, "Detailed Preview filled");
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
            binding.detailInfo.tvReleaseDateText.setText(preview.getRelease_date());
        if (preview.getVote_average() != null && !preview.getVote_average().isEmpty())
            binding.detailInfo.tvVoteAverageText.setText(getString(R.string.detail_rating, preview.getVote_average()));
        Picasso.with(this)
                .load(NetworkUtils.getThumbnailURL(preview.getPosterPath()))
                .into(binding.imagePreview, new Callback() {
                    @Override
                    public void onSuccess() {
                        updateBackground();
                    }

                    @Override
                    public void onError() {
                        Log.e(TAG, "Could not load image");
                    }
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
                    binding.detailInfo.tvReleaseDateLabel.setTextColor(swatch.getTitleTextColor());
                    binding.detailInfo.tvReleaseDateText.setTextColor(swatch.getBodyTextColor());
                    binding.detailInfo.tvVoteAverageLabel.setTextColor(swatch.getTitleTextColor());
                    binding.detailInfo.tvVoteAverageText.setTextColor(swatch.getBodyTextColor());
                }

            }
        });
    }
}
