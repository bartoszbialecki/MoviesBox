package com.example.moviesbox.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.moviesbox.R;
import com.example.moviesbox.model.Trailer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.ViewHolder> {
    // region VARIABLES
    private List<Trailer> mTrailers;
    private OnItemClickListener mListener;
    // endregion

    // region CONSTRUCTORS
    public TrailersAdapter() {
        mTrailers = new ArrayList<>();
    }
    // endregion

    // region LISTENERS
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
        void onShareItemButtonClick(int position);
    }
    // endregion

    // region GETTERS AND SETTERS
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    // endregion

    // region PUBLIC METHODS
    public void addTrailers(List<Trailer> trailers) {
        if (mTrailers == null) {
            mTrailers = new ArrayList<>();
        }

        mTrailers.addAll(trailers);
        notifyDataSetChanged();
    }

    public Trailer getTrailer(int position) {
        if (mTrailers != null) {
            if (position < mTrailers.size()) {
                return mTrailers.get(position);
            }
        }

        return null;
    }
    // endregion

    // region ADAPTER METHODS
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trailer, parent, false);

        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Trailer trailer = mTrailers.get(position);

        holder.trailerNameTextView.setText(trailer.getName());
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }
    // endregion

    // region VIEW HOLDER
    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.trailer_name_text_view)
        TextView trailerNameTextView;
        @BindView(R.id.share_trailer_button)
        ImageButton shareTrailerButton;

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

            shareTrailerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onShareItemButtonClick(getLayoutPosition());
                    }
                }
            });
        }
    }
    // endregion
}
