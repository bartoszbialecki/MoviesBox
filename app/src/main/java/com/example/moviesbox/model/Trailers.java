package com.example.moviesbox.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Trailers {
    // region VARIABLES
    @SerializedName("id")
    @Expose
    private int mId;

    @SerializedName("results")
    @Expose
    private List<Trailer> mTrailers = null;
    // endregion

    // region GETTERS AND SETTERS
    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public List<Trailer> getTrailers() {
        return mTrailers;
    }

    public void setTrailers(List<Trailer> trailers) {
        this.mTrailers = trailers;
    }
    // endregion
}
