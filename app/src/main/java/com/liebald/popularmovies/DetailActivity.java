package com.liebald.popularmovies;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.liebald.popularmovies.databinding.ActivityDetailBinding;
import com.liebald.popularmovies.model.MoviePreview;
import com.liebald.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    /**
     * The databinding for the UI.
     */
    private ActivityDetailBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        MoviePreview preview = null;
        if ((preview = getIntent().getExtras().getParcelable("moviePreview")) == null) {
            return;
        }

        setTitle(preview.getTitle());

        fillLayout(preview);
    }

    private void fillLayout(MoviePreview preview) {
        binding.detailInfo.tvOverviewText.setText(preview.getOverview());
        binding.detailInfo.tvReleaseDateText.setText(preview.getRelease_date());
        binding.detailInfo.tvVoteAverageText.setText(preview.getVote_average());
        Picasso.with(this)
                .load(NetworkUtils.getThumbnailURL(preview.getPosterPath()))
                .into(binding.imagePreview);
    }

}
