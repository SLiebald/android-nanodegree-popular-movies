package com.liebald.popularmovies.model;


import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.liebald.popularmovies.utilities.BitmapConverter;

/**
 * Model for a movie Preview. Holds all relevant Values for a movie preview.
 */
public class MoviePreview implements Parcelable {

    /**
     * Key for passing the movie ID to the review/trailer fragments.
     */
    public final static String MOVIE_ID_KEY = "movie_id";
    /**
     * CREATOR for {@link Parcelable} Implementation
     */
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
    /**
     * The relative path of the poster on tmdb.org.
     */
    private String posterPath;
    /**
     * The title of the movie.
     */
    private String title;
    /**
     * The overview description of the movie.
     */
    private String overview;
    /**
     * The average votes of the movie.
     */
    private String vote_average;
    /**
     * The release date of the movie.
     */
    private String release_date;
    /**
     * The tmdb movie ID of the movie.
     */
    private int movie_id;
    /**
     * The image thumbnail of the movie.
     */
    private Bitmap image_thumbail;

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
        this.movie_id = in.readInt();
        byte[] bytes = new byte[in.readInt()];
        in.readByteArray(bytes);
        this.image_thumbail = BitmapConverter.getImage(bytes);
    }

    public int getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
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

    public Bitmap getImage_thumbail() {
        return image_thumbail;
    }

    public void setImage_thumbail(Bitmap image_thumbail) {
        this.image_thumbail = image_thumbail;
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
        dest.writeInt(movie_id);

        // for some reason writing the bitmap directly to the Parcel doesn't work:
        // dest.writeParcelable(image_thumbail, flags);
        // If tried the app crashes without feedback when starting the detail activity.
        // Converting to a byte array and transferring that works however.
        byte[] bytes = BitmapConverter.getBytes(image_thumbail);
        dest.writeInt(bytes.length);
        // image_thumbail.writeToParcel(dest, flags);
        dest.writeByteArray(bytes);
    }

    @Override
    public String toString() {
        return "MoviePreview{" +
                "posterPath='" + posterPath + '\'' +
                ", title='" + title + '\'' +
                ", overview='" + overview + '\'' +
                ", vote_average='" + vote_average + '\'' +
                ", release_date='" + release_date + '\'' +
                ", movie_id=" + movie_id +
                '}';
    }
}
