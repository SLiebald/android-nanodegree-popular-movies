package com.liebald.popularmovies.model;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Model for a movie Preview. Holds all relevant Values for a movie preview.
 */
public class MoviePreview implements Parcelable {

    static final Parcelable.Creator<MoviePreview> CREATOR
            = new Creator<MoviePreview>() {
        @Override
        public MoviePreview createFromParcel(Parcel source) {
            return new MoviePreview(source);
        }

        @Override
        public MoviePreview[] newArray(int size) {
            return new MoviePreview[size];
        }
    };
    private String posterPath;
    private String title;
    private String overview;
    private String vote_average;
    private String release_date;

    /**
     * Default constructor.
     */
    public MoviePreview() {
    }

    /**
     * Constructor for {@link Parcelable} restoration of the {@link MoviePreview}.
     *
     * @param in The {@link Parcel} to read.
     */
    private MoviePreview(Parcel in) {
        this.title = in.readString();
        this.overview = in.readString();
        this.posterPath = in.readString();
        this.vote_average = in.readString();
        this.release_date = in.readString();
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(posterPath);
        dest.writeString(vote_average);
        dest.writeString(release_date);
    }

    @Override
    public String toString() {
        return "MoviePreview{" +
                "posterPath='" + posterPath + '\'' +
                ", title='" + title + '\'' +
                ", overview='" + overview + '\'' +
                ", top_rated='" + vote_average + '\'' +
                ", release_date='" + release_date + '\'' +
                '}';
    }
}
