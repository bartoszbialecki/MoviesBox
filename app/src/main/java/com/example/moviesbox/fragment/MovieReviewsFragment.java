package com.example.moviesbox.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.moviesbox.R;
import com.example.moviesbox.adapter.ReviewsAdapter;
import com.example.moviesbox.data.MoviesRepository;
import com.example.moviesbox.data.Settings;
import com.example.moviesbox.model.Movie;
import com.example.moviesbox.model.Reviews;
import com.example.moviesbox.service.MoviesApiClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.mateware.snacky.Snacky;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MovieReviewsFragment extends Fragment {
    // region CONSTANTS
    private static final String EXTRA_MOVIE_ID = "movie_id";
    // endregion

    // region VARIABLES
    @BindView(R.id.reviews_recycler_view)
    RecyclerView reviewsRecyclerView;
    @BindView(R.id.empty_text_view)
    TextView emptyTextView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private Movie mMovie;
    private Unbinder mUnbinder;
    private ReviewsAdapter mAdapter;
    // endregion

    // region PUBLIC METHODS
    public static MovieReviewsFragment newInstance(int movieId) {
        MovieReviewsFragment fragment = new MovieReviewsFragment();
        Bundle args = new Bundle();

        if (movieId > 0) {
            args.putInt(EXTRA_MOVIE_ID, movieId);
        }

        fragment.setArguments(args);

        return fragment;
    }
    // endregion

    // region LIFE CYCLE METHODS
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reviews, container, false);

        mUnbinder = ButterKnife.bind(this, view);

        setupAdapter();
        setupUI();
        getReviews();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mUnbinder.unbind();
    }
    // endregion

    // region PRIVATE METHODS
    private void initArguments() {
        Bundle bundle = getArguments();

        if (bundle != null) {
            if (bundle.containsKey(EXTRA_MOVIE_ID)) {
                int movieId = bundle.getInt(EXTRA_MOVIE_ID, 0);

                if (movieId > 0) {
                    mMovie = MoviesRepository.getInstance().getMovie(movieId);
                }
            }
        }
    }

    private void setupUI() {
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        reviewsRecyclerView.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        reviewsRecyclerView.addItemDecoration(itemDecoration);
    }

    private void setupAdapter() {
        mAdapter = new ReviewsAdapter();
        reviewsRecyclerView.setAdapter(mAdapter);
    }

    private void updateUI() {
        if (reviewsRecyclerView != null) {
            if (mAdapter.getItemCount() > 0) {
                if (reviewsRecyclerView.getVisibility() == View.GONE) {
                    reviewsRecyclerView.setVisibility(View.VISIBLE);
                }

                if (emptyTextView.getVisibility() == View.VISIBLE) {
                    emptyTextView.setVisibility(View.GONE);
                }
            } else {
                if (reviewsRecyclerView.getVisibility() == View.VISIBLE) {
                    reviewsRecyclerView.setVisibility(View.GONE);
                }

                if (emptyTextView.getVisibility() == View.GONE) {
                    emptyTextView.setVisibility(View.VISIBLE);
                }
            }

            progressBar.setVisibility(View.GONE);
        }
    }

    private void getReviews() {
        if (mMovie != null) {
            if (mMovie.getReviews() == null || mMovie.getReviews().size() == 0) {
                MoviesApiClient.getInstance().getReviews(mMovie.getId(), 1, Settings.getInstance().getCurrentLanguage())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<Reviews>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onSuccess(@NonNull Reviews reviews) {
                                mAdapter.addReviews(reviews.getReviews());
                                mMovie.setReviews(reviews.getReviews());
                                updateUI();
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                showErrorMessage(e.getLocalizedMessage());
                            }
                        });
            } else {
                mAdapter.addReviews(mMovie.getReviews());
                updateUI();
            }
        }
    }

    private void showErrorMessage(String errorMessage) {
        Snacky.builder()
                .setActivty(getActivity())
                .setText(errorMessage)
                .error()
                .show();
    }
    // endregion
}
