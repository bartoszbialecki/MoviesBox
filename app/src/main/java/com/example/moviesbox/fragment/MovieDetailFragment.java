package com.example.moviesbox.fragment;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moviesbox.R;
import com.example.moviesbox.activity.ImageViewerActivity;
import com.example.moviesbox.model.Movie;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment {
    // region CONSTANTS
    private static final String EXTRA_MOVIE = "movie";
    // endregion

    // region VARIABLES
    @BindView(R.id.poster_image_view)
    ImageView posterImageView;
    @BindView(R.id.title_text_view)
    TextView titleTextView;
    @BindView(R.id.release_date_text_view)
    TextView releaseDateTextView;
    @BindView(R.id.vote_average_text_view)
    TextView voteAverageTextView;
    @BindView(R.id.overview_text_view)
    TextView overviewTextView;
    @BindView(R.id.container)
    ViewGroup container;

    private Movie mMovie;
    private Unbinder mUnbinder;
    private DateFormat mDateFormat;
    // endregion

    // region CONSTRUCTORS
    public MovieDetailFragment() {
        // Required empty public constructor
    }
    // endregion

    // region PUBLIC METHODS
    public static MovieDetailFragment newInstance(Movie movie) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();

        if (movie != null) {
            args.putParcelable(EXTRA_MOVIE, movie);
        }

        fragment.setArguments(args);

        return fragment;
    }
    // endregion

    // region LIFE CYCLE METHODS
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);

        initArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        mUnbinder = ButterKnife.bind(this, view);

        setupUI();

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
            if (bundle.containsKey(EXTRA_MOVIE)) {
                mMovie = bundle.getParcelable(EXTRA_MOVIE);
            }
        }
    }

    private void setupUI() {
        if (mMovie != null) {
            getActivity().setTitle(mMovie.getTitle());

            Picasso.with(getActivity())
                    .load(mMovie.getPosterPath(false))
                    .placeholder(R.color.background)
                    .into(posterImageView);

            posterImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), ImageViewerActivity.class);
                    intent.putExtra(ImageViewerActivity.EXTRA_IMAGE_PATH, mMovie.getPosterPath(true));

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptionsCompat options = ActivityOptionsCompat.
                                makeSceneTransitionAnimation(getActivity(), posterImageView, getString(R.string.poster_transition_name));
                        startActivity(intent, options.toBundle());
                    } else {
                        startActivity(intent);
                    }
                }
            });

            if (mMovie.getOriginalTitle() != null) {
                titleTextView.setText(mMovie.getOriginalTitle());
            }

            if (mMovie.getReleaseDate() != null) {
                releaseDateTextView.setText(mDateFormat.format(mMovie.getReleaseDate()));
            }

            voteAverageTextView.setText(String.format(Locale.getDefault(), "%.1f \u2605", mMovie.getVoteAverage()));

            if (mMovie.getOverview() != null) {
                overviewTextView.setText(mMovie.getOverview());
            }
        }
    }
    // endregion
}
