package com.liebald.popularmovies.ui.detail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liebald.popularmovies.R;
import com.liebald.popularmovies.model.Review;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for the Previews of a movie.
 */
public class DetailsReviewsAdapter extends RecyclerView.Adapter<DetailsReviewsAdapter.ReviewViewHolder> {

    /**
     * The {@link List} of {@link Review}s that is managed by this adapter.
     */
    private List<Review> reviews;

    /**
     * Constructor.
     */
    DetailsReviewsAdapter() {
        reviews = new ArrayList<>();
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.details_review_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.review.setText(review.getReview());
        holder.reviewer.setText(review.getReviewer());
        // If no reviews are available tell it to the user.
        if (review.getReview_id() == -1) {
            holder.review.setVisibility(View.GONE);
            holder.reviewer.setGravity(Gravity.CENTER);
        }
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    /**
     * Swaps the List of MoviePreviews that is managed by this adapter.
     *
     * @param reviews The new List of {@link Review}s.
     */
    void swapItems(List<Review> reviews) {
        this.reviews = reviews;
        if (reviews.size() == 0) {
            this.reviews.add(new Review("", "No reviews available.", -1));
        }
        notifyDataSetChanged();
    }

    /**
     * Internal class for the ViewHolder
     */
    class ReviewViewHolder extends RecyclerView.ViewHolder {

        /**
         * The TextView for the title text.
         */
        final TextView review;
        /**
         * The TextView for the title author.
         */
        final TextView reviewer;

        /**
         * Constructor for {@link ReviewViewHolder}.
         *
         * @param itemView The inflated {@link View} of this {@link ReviewViewHolder}.
         */
        ReviewViewHolder(View itemView) {
            super(itemView);
            review = itemView.findViewById(R.id.tv_review);
            reviewer = itemView.findViewById(R.id.tv_reviewer);
        }


    }
}
