package com.liebald.popularmovies.ui.detail;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.liebald.popularmovies.R;
import com.liebald.popularmovies.model.MoviePreview;
import com.liebald.popularmovies.utilities.JsonUtils;
import com.liebald.popularmovies.utilities.NetworkUtils;
import com.liebald.popularmovies.utilities.VolleyRequests;

import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsTrailerFragment extends Fragment implements DetailsTrailerAdapter.TrailerItemClickListener {
    /**
     * Tag for Logging in this activity.
     */
    private static final String TAG = DetailsTrailerFragment.class.getSimpleName();
    private DetailsTrailerAdapter mTrailerAdapter;

    public DetailsTrailerFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details_trailer, container, false);


        // Setup the recyclerView and adapters for trailers.
        RecyclerView mReviewsList = view.findViewById(R.id.recycler_trailer);

        // In Portrait orientation the bottom action bar cuts of the last item of the recyclerview.
        // Therefore we need to add a fitting bottom margin in this case. In landscape orientation the bar is on the side.
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            ViewGroup.MarginLayoutParams margins = (ViewGroup.MarginLayoutParams) mReviewsList.getLayoutParams();
            margins.setMargins(0, 0, 0, (int) getResources().getDimension(R.dimen.margin_bottom_action_bar));
            mReviewsList.setLayoutParams(margins);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mReviewsList.setLayoutManager(layoutManager);
        mTrailerAdapter = new DetailsTrailerAdapter(this, getContext());
        mReviewsList.setAdapter(mTrailerAdapter);

        //Fill the Reviews
        Bundle bundle = this.getArguments();
        int movieID = 0;
        if (bundle != null)
            movieID = bundle.getInt(MoviePreview.MOVIE_ID_KEY);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, NetworkUtils.getVideosUrl(movieID), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mTrailerAdapter.swapItems(JsonUtils.parseMovieVideos(response));
                        Log.d(TAG, response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error on loading Reviews via volley: " + error.getMessage());
                    }
                });
        VolleyRequests.getInstance(getContext()).addToRequestQueue(jsObjRequest);
        return view;
    }

    // based on https://stackoverflow.com/questions/42024058/how-to-open-youtube-video-link-in-android-app
    @Override
    public void onTrailerItemClick(String videoKey) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoKey)));
        } catch (ActivityNotFoundException ex) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + videoKey)));
        }
    }
}
