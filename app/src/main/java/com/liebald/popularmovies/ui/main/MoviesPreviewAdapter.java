package com.liebald.popularmovies.ui.main;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.liebald.popularmovies.R;
import com.liebald.popularmovies.model.MoviePreview;
import com.liebald.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for the Previews of popular movies.
 */
public class MoviesPreviewAdapter extends RecyclerView.Adapter<MoviesPreviewAdapter.PreviewViewHolder> {

    /**
     * Tag for Logging in this activity.
     */
    private static final String TAG = MoviesPreviewAdapter.class.getSimpleName();

    /**
     * The {@link GridItemClickListener} that will be called in order to handle onClick events
     * on items managed by this class.
     */
    private final GridItemClickListener mGridItemClickListener;

    /**
     * Context of the Class using this Adapter.
     */
    private final Context mContext;

    /**
     * The {@link List} of {@link MoviePreview}s that is managed by this adapter.
     */
    private List<MoviePreview> moviePreviews;

    /**
     * Constructor. Takes a {@link GridItemClickListener} that will be called on clicks on
     * items managed by the adapter.
     *
     * @param listener The {@link GridItemClickListener} to use for onClick events.
     */
    MoviesPreviewAdapter(Context context, GridItemClickListener listener) {
        mGridItemClickListener = listener;
        moviePreviews = new ArrayList<>();
        mContext = context;
    }

    @Override
    public PreviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.thumbnail_grid_item, parent, false);
        return new PreviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PreviewViewHolder holder, int position) {
        MoviePreview moviePreview = moviePreviews.get(position);
        Uri uri = NetworkUtils.getThumbnailURL(moviePreview.getPosterPath());
        Picasso.with(mContext)
                .load(uri)

                .into(holder.imagePreview, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                        Log.e(TAG, "Could not load image");
                    }
                });
        holder.tvTitle.setText(moviePreview.getTitle());

    }

    @Override
    public int getItemCount() {
        return moviePreviews.size();
    }

    /**
     * Swaps the List of MoviePreviews that is managed by this adapter.
     *
     * @param moviePreviews The new List of {@link MoviePreview}s.
     */
    void swapItems(List<MoviePreview> moviePreviews) {
        this.moviePreviews = moviePreviews;
        notifyDataSetChanged();
    }

    /**
     * Returns the item of the managed List on the specified index.
     *
     * @param index Index to retrieve.
     * @return The {@link MoviePreview} at the specified Index. Null if index doesn't exist.
     */
    public MoviePreview getItem(int index) {
        if (moviePreviews.size() <= index)
            return null;
        return moviePreviews.get(index);
    }

    /**
     * Classes implementing this Interface can be called in order to handle onClick events
     * on items managed by {@link MoviesPreviewAdapter}.
     */
    public interface GridItemClickListener {
        void onGridItemClick(int clickedItemIndex);
    }

    /**
     * Internal class for the ViewHolder
     */
    class PreviewViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        /**
         * The ImageView for the Thumbnail we want to show in this {@link PreviewViewHolder}.
         */
        final ImageView imagePreview;

        /**
         * The {@link TextView} of this {@link PreviewViewHolder}.
         */
        final TextView tvTitle;

        /**
         * Constructor for {@link PreviewViewHolder}. Sets an onClick listener for the according view.
         *
         * @param itemView The inflated {@link View} of this {@link PreviewViewHolder}.
         */
        PreviewViewHolder(View itemView) {
            super(itemView);
            imagePreview = itemView.findViewById(R.id.image_preview);
            tvTitle = itemView.findViewById(R.id.tv_title);
            itemView.setOnClickListener(this);
        }

        /**
         * Called when a click on a Preview image in the RecyclerView occurs.
         *
         * @param view The View that was clicked.
         */
        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            //Call the GridItemClickListener that was registered for handling this event.
            mGridItemClickListener.onGridItemClick(clickedPosition);
        }
    }
}
