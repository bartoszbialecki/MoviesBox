package com.example.moviesbox.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Movies {
    // region VARIABLES
    @SerializedName("page")
    @Expose
    private int mCurrentPage = 0;

    @SerializedName("total_results")
    @Expose
    private int mTotalResults;

    @SerializedName("total_pages")
    @Expose
    private int mTotalPages;

    @SerializedName("results")
    @Expose
    private List<Movie> mMovies = null;
    // endregion

    // region GETTERS AND SETTERS
    public int getCurrentPage() {
        return mCurrentPage;
    }

    public void setCurrentPage(int page) {
        this.mCurrentPage = page;
    }

    public int getTotalResults() {
        return mTotalResults;
    }

    public void setTotalResults(int totalResults) {
        this.mTotalResults = totalResults;
    }

    public int getTotalPages() {
        return mTotalPages;
    }

    public void setTotalPages(int totalPages) {
        this.mTotalPages = totalPages;
    }

    public List<Movie> getMovies() {
        return mMovies;
    }

    public void setMovies(List<Movie> movies) {
        this.mMovies = movies;
    }
    // endregion
}
