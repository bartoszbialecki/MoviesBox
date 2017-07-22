package com.example.moviesbox.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.moviesbox.R;
import com.example.moviesbox.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {
    // region VARIABLES
    private List<Movie> mMovies;
    private OnItemClickListener mListener;
    // endregion

    // region CONSTRUCTORS
    public MoviesAdapter() {
        mMovies = new ArrayList<>();
    }
    // endregion

    // region LISTENERS
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    // endregion

    // region GETTERS AND SETTERS
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    // endregion

    // region PUBLIC METHODS
    public void addMovies(List<Movie> movies) {
        if (mMovies == null) {
            mMovies = new ArrayList<>();
        }

        mMovies.addAll(movies);
        notifyDataSetChanged();
    }

    public Movie getMovie(int position) {
        if (mMovies != null) {
            if (position < mMovies.size()) {
                return mMovies.get(position);
            }
        }

        return null;
    }

    public void clear() {
        if (mMovies != null) {
            mMovies.clear();
            notifyDataSetChanged();
        }
    }

    public void updateMovie(int movieId) {
        if (mMovies != null) {
            int pos = 0;

            for (Movie movie: mMovies) {
                if (movie.getId() == movieId) {
                    if (!movie.isFavorite()) {
                        mMovies.remove(movie);
                        notifyItemRemoved(pos);
                        notifyItemRangeChanged(pos, mMovies.size());

                        break;
                    }
                }

                pos++;
            }
        }
    }
    // endregion

    // region ADAPTER METHODS
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);

        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = mMovies.get(position);

        Context context = holder.itemView.getContext();

        Picasso.with(context)
                .load(movie.getPosterFullPath(false))
                .placeholder(R.color.background)
                .into(holder.posterImageView);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }
    // endregion

    // region VIEW HOLDER
    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.poster_image_view)
        ImageView posterImageView;

        private OnItemClickListener mListener;

        ViewHolder(final View itemView, OnItemClickListener listener) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            mListener = listener;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onItemClick(itemView, getLayoutPosition());
                    }
                }
            });
        }
    }
    // endregion
}
