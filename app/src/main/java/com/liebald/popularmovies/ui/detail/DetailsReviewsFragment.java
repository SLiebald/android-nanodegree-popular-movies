package com.liebald.popularmovies.ui.detail;


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
public class DetailsReviewsFragment extends Fragment {

    /**
     * Tag for Logging in this activity.
     */
    private static final String TAG = DetailsReviewsFragment.class.getSimpleName();
    private DetailsReviewsAdapter mReviewsAdapter;

    public DetailsReviewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details_reviews, container, false);


        // Setup the recyclerView and adapter  for reviews.
        RecyclerView recyclerView = view.findViewById(R.id.recycler_reviews);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mReviewsAdapter = new DetailsReviewsAdapter();
        recyclerView.setAdapter(mReviewsAdapter);

        //Fill the Reviews
        Bundle bundle = this.getArguments();
        int movieID = 0;
        if (bundle != null)
            movieID = bundle.getInt(MoviePreview.MOVIE_ID_KEY);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, NetworkUtils.getReviewsUrl(movieID), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mReviewsAdapter.swapItems(JsonUtils.parseMovieReviews(response));
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

}
