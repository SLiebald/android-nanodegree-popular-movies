package com.liebald.popularmovies;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.liebald.popularmovies.databinding.ActivityDetailBinding;
import com.liebald.popularmovies.model.MoviePreview;
import com.liebald.popularmovies.utilities.NetworkUtils;
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
                .into(binding.imagePreview);
    }

}
