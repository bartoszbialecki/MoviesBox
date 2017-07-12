package com.example.moviesbox.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.moviesbox.R;
import com.example.moviesbox.model.Review;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {
    // region VARIABLES
    private List<Review> mReviews;
    private OnItemClickListener mListener;
    // endregion

    // region CONSTRUCTORS
    public ReviewsAdapter() {
        mReviews = new ArrayList<>();
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
    public void addReviews(List<Review> reviews) {
        if (mReviews == null) {
            mReviews = new ArrayList<>();
        }

        mReviews.addAll(reviews);
        notifyDataSetChanged();
    }

    public Review getReview(int position) {
        if (mReviews != null) {
            if (position < mReviews.size()) {
                return mReviews.get(position);
            }
        }

        return null;
    }
    // endregion

    // region ADAPTER METHODS
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);

        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Review review = mReviews.get(position);

        holder.authorTextView.setText(review.getAuthor());
        holder.contentTextView.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }
    // endregion

    // region VIEW HOLDER
    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.author_text_view)
        TextView authorTextView;
        @BindView(R.id.content_text_view)
        TextView contentTextView;

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