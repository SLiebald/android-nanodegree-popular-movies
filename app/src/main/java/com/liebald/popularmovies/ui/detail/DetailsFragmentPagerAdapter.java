package com.liebald.popularmovies.ui.detail;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.liebald.popularmovies.R;
import com.liebald.popularmovies.model.MoviePreview;

class DetailsFragmentPagerAdapter extends FragmentPagerAdapter {


    private final int movieID;
    private final Context mContext;

    DetailsFragmentPagerAdapter(Context context, FragmentManager fm, int movie_id) {
        super(fm);
        mContext = context;
        this.movieID = movie_id;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(MoviePreview.MOVIE_ID_KEY, movieID);
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new DetailsTrailerFragment();
                break;
            default://default is 1, since we only have these two cases
                fragment = new DetailsReviewsFragment();
                break;
        }
        fragment.setArguments(bundle);

        return fragment; // shouldn't happen
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.tab_trailer);
            case 1:
                return mContext.getString(R.string.tab_reviews);
            default:
                return null;
        }
    }
}
