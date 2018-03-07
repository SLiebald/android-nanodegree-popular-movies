package com.liebald.popularmovies.model;

/**
 * Model for a movie review. Holds all relevant Values for a movie review.
 */
public class Review {

    /**
     * The review text.
     */
    private String review;

    /**
     * The name of the reviewer.
     */
    private String reviewer;

    /**
     * The tmdb movie ID of the movie.
     */
    private int review_id;

    public Review(@SuppressWarnings("SameParameterValue") String review, @SuppressWarnings("SameParameterValue") String reviewer, int review_id) {
        this.review = review;
        this.reviewer = reviewer;
        this.review_id = review_id;
    }

    public Review() {
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public int getReview_id() {
        return review_id;
    }

    public void setReview_id(int review_id) {
        this.review_id = review_id;
    }


}
