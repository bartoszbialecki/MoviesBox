package com.example.moviesbox.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Reviews {
    // region VARIABLES
    @SerializedName("id")
    @Expose
    private int mId;

    @SerializedName("page")
    @Expose
    private int mCurrentPage;

    @SerializedName("results")
    @Expose
    private List<Review> mReviews = null;

    @SerializedName("total_pages")
    @Expose
    private int mTotalPages;

    @SerializedName("total_results")
    @Expose
    private int mTotalResults;
    // endregion

    // region GETTERS AND SETTERS
    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public int getPage() {
        return mCurrentPage;
    }

    public void setPage(int page) {
        this.mCurrentPage = page;
    }

    public List<Review> getReviews() {
        return mReviews;
    }

    public void setReviews(List<Review> reviews) {
        this.mReviews = reviews;
    }

    public int getTotalPages() {
        return mTotalPages;
    }

    public void setTotalPages(int totalPages) {
        this.mTotalPages = totalPages;
    }

    public int getTotalResults() {
        return mTotalResults;
    }

    public void setTotalResults(int totalResults) {
        this.mTotalResults = totalResults;
    }
    // endregion
}
