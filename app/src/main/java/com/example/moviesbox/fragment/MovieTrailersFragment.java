package com.example.moviesbox.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
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
import com.example.moviesbox.adapter.TrailersAdapter;
import com.example.moviesbox.data.MoviesRepository;
import com.example.moviesbox.data.Settings;
import com.example.moviesbox.model.Movie;
import com.example.moviesbox.model.Trailer;
import com.example.moviesbox.model.Trailers;
import com.example.moviesbox.service.MoviesApiClient;
import com.example.moviesbox.util.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.mateware.snacky.Snacky;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MovieTrailersFragment extends Fragment {
    // region CONSTANTS
    private static final String EXTRA_MOVIE_ID = "movie_id";
    private static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";
    // endregion

    // region VARIABLES
    @BindView(R.id.trailers_recycler_view)
    RecyclerView trailersRecyclerView;
    @BindView(R.id.empty_text_view)
    TextView emptyTextView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private Movie mMovie;
    private Unbinder mUnbinder;
    private TrailersAdapter mAdapter;
    // endregion

    // region PUBLIC METHODS
    public static MovieTrailersFragment newInstance(int movieId) {
        MovieTrailersFragment fragment = new MovieTrailersFragment();
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
        View view = inflater.inflate(R.layout.fragment_trailers, container, false);

        mUnbinder = ButterKnife.bind(this, view);

        setupAdapter();
        setupUI();
        getTrailers();

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
                    mMovie = MoviesRepository.getInstance(getActivity()).getMovie(movieId);
                }
            }
        }
    }

    private void setupUI() {
        trailersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        trailersRecyclerView.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        trailersRecyclerView.addItemDecoration(itemDecoration);
    }

    private void setupAdapter() {
        mAdapter = new TrailersAdapter();

        mAdapter.setOnItemClickListener(new TrailersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Trailer trailer = mAdapter.getTrailer(position);

                if (trailer != null) {
                    showTrailer(trailer);
                }
            }

            @Override
            public void onShareItemButtonClick(int position) {
                Trailer trailer = mAdapter.getTrailer(position);

                if (trailer != null) {
                    shareTrailerUrl(Uri.parse(YOUTUBE_BASE_URL + trailer.getKey()));
                }
            }
        });

        trailersRecyclerView.setAdapter(mAdapter);
    }

    private void updateUI() {
        if (trailersRecyclerView != null) {
            if (mAdapter.getItemCount() > 0) {
                if (trailersRecyclerView.getVisibility() == View.GONE) {
                    trailersRecyclerView.setVisibility(View.VISIBLE);
                }

                if (emptyTextView.getVisibility() == View.VISIBLE) {
                    emptyTextView.setVisibility(View.GONE);
                }
            } else {
                if (trailersRecyclerView.getVisibility() == View.VISIBLE) {
                    trailersRecyclerView.setVisibility(View.GONE);
                }

                if (emptyTextView.getVisibility() == View.GONE) {
                    emptyTextView.setVisibility(View.VISIBLE);
                }
            }

            progressBar.setVisibility(View.GONE);
        }
    }

    private void getTrailers() {
        if (mMovie != null) {
            if (mMovie.getTrailers() == null || mMovie.getTrailers().size() == 0) {
                if (NetworkUtils.isNetworkAvailable(getActivity())) {
                    MoviesApiClient.getInstance().getTrailers(mMovie.getId(), Settings.getInstance().getCurrentLanguage())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new SingleObserver<Trailers>() {
                                @Override
                                public void onSubscribe(@NonNull Disposable d) {

                                }

                                @Override
                                public void onSuccess(@NonNull Trailers trailers) {
                                    mAdapter.addTrailers(trailers.getTrailers());
                                    mMovie.setTrailers(trailers.getTrailers());
                                    updateUI();
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    showErrorMessage(e.getLocalizedMessage());
                                }
                            });
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            } else {
                mAdapter.addTrailers(mMovie.getTrailers());
                updateUI();
            }
        }
    }

    private void showTrailer(Trailer trailer) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailer.getKey()));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_BASE_URL + trailer.getKey()));

        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    private void showErrorMessage(String errorMessage) {
        Snacky.builder()
                .setActivty(getActivity())
                .setText(errorMessage)
                .error()
                .show();
    }

    private void shareTrailerUrl(Uri uri) {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_trailer_title));
        share.putExtra(Intent.EXTRA_TEXT, uri.toString());

        startActivity(Intent.createChooser(share, getString(R.string.share_trailer)));
    }
    // endregion
}
