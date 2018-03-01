package com.liebald.popularmovies.ui.detail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.liebald.popularmovies.R;
import com.liebald.popularmovies.model.Video;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for the Trailer of a movie.
 */
public class DetailsTrailerAdapter extends RecyclerView.Adapter<DetailsTrailerAdapter.TrailerViewHolder> {

    /**
     * Tag for Logging in this activity.
     */
    private static final String TAG = DetailsTrailerAdapter.class.getSimpleName();
    private final TrailerItemClickListener mTrailerItemClickListener;
    /**
     * Context for this adapter
     */
    private final Context mContext;
    /**
     * The {@link List} of {@link Video}s that is managed by this adapter.
     */
    private List<Video> videos;

    /**
     * Constructor.
     */
    DetailsTrailerAdapter(TrailerItemClickListener trailerItemClickListener, Context context) {
        mTrailerItemClickListener = trailerItemClickListener;
        mContext = context;
        videos = new ArrayList<>();
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.details_video_item, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TrailerViewHolder holder, int position) {
        Video video = videos.get(position);
        holder.title.setText(video.getName());
        // If no videos are available tell it to the user.
        if (video.getKey().isEmpty()) {
            holder.title.setGravity(Gravity.CENTER);
        } else {
            Log.d(TAG, "Loading preview");
            Picasso.with(mContext)
                    .load("http://img.youtube.com/vi/" + video.getKey() + "/mqdefault.jpg")
                    .into(holder.videoPreview, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG, "Loading preview success");

                        }

                        @Override
                        public void onError() {
                            Log.d(TAG, "Loading preview error");

                        }
                    });
        }
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    /**
     * Swaps the List of Trailers that is managed by this adapter.
     *
     * @param videos The new List of {@link Video}s.
     */
    void swapItems(List<Video> videos) {
        this.videos = videos;
        if (videos.size() == 0) {
            this.videos.add(new Video("", "No videos available."));
        }
        notifyDataSetChanged();
    }


    /**
     * Classes implementing this Interface can be called in order to handle onClick events
     * on items managed by {@link DetailsTrailerAdapter}.
     */
    public interface TrailerItemClickListener {
        void onTrailerItemClick(String videoKey);
    }


    /**
     * Internal class for the ViewHolder
     */
    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /**
         * The TextView for the video title text.
         */
        final TextView title;

        /**
         * The TextView for the title author.
         */
        final ImageView videoPreview;

        /**
         * Constructor for {@link TrailerViewHolder}.
         *
         * @param itemView The inflated {@link View} of this {@link TrailerViewHolder}.
         */
        TrailerViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_videoTitle);
            videoPreview = itemView.findViewById(R.id.image_video);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mTrailerItemClickListener.onTrailerItemClick(videos.get(clickedPosition).getKey());
        }
    }
}
