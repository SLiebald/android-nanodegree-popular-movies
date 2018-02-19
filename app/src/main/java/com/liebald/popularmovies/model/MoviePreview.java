package com.liebald.popularmovies.model;


/**
 * Model for a movie Preview. Holds all relevant Values for a movie preview.
 */
public class MoviePreview {
    /**
     * Relative path to the movies poster.
     */
    private String posterPath;


    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }
}
