package com.liebald.popularmovies.model;

/**
 * Model for a movies video. Holds all relevant values for a movie video.
 */
public class Video {

    /**
     * The key of the video.
     */
    private String key;

    /**
     * The name of the video.
     */
    private String name;


    @SuppressWarnings("SameParameterValue")
    public Video(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public Video() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
