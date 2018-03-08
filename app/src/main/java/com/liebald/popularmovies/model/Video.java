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


    /**
     * Constructor of a video trailer that takes its youtube key and its name.
     *
     * @param key  Key of the video on youtube.
     * @param name Name of the video.
     */
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
