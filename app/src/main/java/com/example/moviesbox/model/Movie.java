package com.example.moviesbox.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Movie implements Parcelable {
    // region CONSTANTS
    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185";
    private static final String LARGE_POSTER_BASE_URL = "http://image.tmdb.org/t/p/w780";
    // endregion

    // region VARIABLES
    @SerializedName("vote_count")
    @Expose
    private int mVoteCount;

    @SerializedName("id")
    @Expose
    private int mId;

    @SerializedName("video")
    @Expose
    private boolean mHasVideo;

    @SerializedName("vote_average")
    @Expose
    private double mVoteAverage;

    @SerializedName("title")
    @Expose
    private String mTitle;

    @SerializedName("popularity")
    @Expose
    private double mPopularity;

    @SerializedName("poster_path")
    @Expose
    private String mPosterPath;

    @SerializedName("original_language")
    @Expose
    private String mOriginalLanguage;

    @SerializedName("original_title")
    @Expose
    private String mOriginalTitle;

    @SerializedName("genre_ids")
    @Expose
    private List<Integer> mGenreIds = null;

    @SerializedName("backdrop_path")
    @Expose
    private String mBackdropPath;

    @SerializedName("adult")
    @Expose
    private boolean mAdult;

    @SerializedName("overview")
    @Expose
    private String mOverview;

    @SerializedName("release_date")
    @Expose
    private Date mReleaseDate;

    private List<Trailer> mTrailers;
    private List<Review> mReviews;
    // endregion

    // region CONSTRUCTORS
    public Movie(Parcel in) {
        mVoteCount = in.readInt();
        mId = in.readInt();
        mHasVideo = in.readByte() == 1;
        mVoteAverage = in.readDouble();
        mTitle = in.readString();
        mPopularity = in.readDouble();
        mPosterPath = in.readString();
        mOriginalLanguage = in.readString();
        mOriginalTitle = in.readString();

        List<Integer> genres = new ArrayList<>();
        in.readList(genres, null);

        if (genres.size() > 0) {
            mGenreIds = genres;
        }

        mBackdropPath = in.readString();
        mAdult = in.readByte() == 1;
        mOverview = in.readString();

        long releaseTime = in.readLong();

        if (releaseTime > 0) {
            mReleaseDate = new Date(releaseTime);
        }

        this.mTrailers = new ArrayList<>();
        in.readTypedList(mTrailers, Trailer.CREATOR);

        this.mReviews = new ArrayList<>();
        in.readTypedList(mReviews, Review.CREATOR);
    }
    // endregion

    // region GETTERS AND SETTERS
    public int getVoteCount() {
        return mVoteCount;
    }

    public void setVoteCount(int voteCount) {
        this.mVoteCount = voteCount;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public boolean hasVideo() {
        return mHasVideo;
    }

    public void setVideo(boolean video) {
        this.mHasVideo = video;
    }

    public double getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.mVoteAverage = voteAverage;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public double getPopularity() {
        return mPopularity;
    }

    public void setPopularity(double popularity) {
        this.mPopularity = popularity;
    }

    public String getPosterPath(boolean large) {
        return String.format("%s%s", large ? LARGE_POSTER_BASE_URL : POSTER_BASE_URL, mPosterPath);
    }

    public void setPosterPath(String posterPath) {
        this.mPosterPath = posterPath;
    }

    public String getOriginalLanguage() {
        return mOriginalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.mOriginalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.mOriginalTitle = originalTitle;
    }

    public List<Integer> getGenreIds() {
        return mGenreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.mGenreIds = genreIds;
    }

    public String getBackdropPath() {
        return mBackdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.mBackdropPath = backdropPath;
    }

    public boolean adult() {
        return mAdult;
    }

    public void setAdult(boolean adult) {
        this.mAdult = adult;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String overview) {
        this.mOverview = overview;
    }

    public Date getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.mReleaseDate = releaseDate;
    }

    public List<Trailer> getTrailers() {
        return mTrailers;
    }

    public void setTrailers(List<Trailer> trailers) {
        this.mTrailers = trailers;
    }

    public List<Review> getReviews() {
        return mReviews;
    }

    public void setReviews(List<Review> reviews) {
        this.mReviews = reviews;
    }
    // endregion

    // region PARCELABLE METHODS
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(mVoteCount);
        parcel.writeInt(mId);
        parcel.writeByte((byte)(mHasVideo ? 1 : 0));
        parcel.writeDouble(mVoteAverage);
        parcel.writeString(mTitle);
        parcel.writeDouble(mPopularity);
        parcel.writeString(mPosterPath);
        parcel.writeString(mOriginalLanguage);
        parcel.writeString(mOriginalTitle);
        parcel.writeList(mGenreIds);
        parcel.writeString(mBackdropPath);
        parcel.writeByte((byte)(mAdult ? 1 : 0));
        parcel.writeString(mOverview);
        parcel.writeLong(mReleaseDate != null ? mReleaseDate.getTime() : 0);
        parcel.writeTypedList(mTrailers);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    // endregion
}
