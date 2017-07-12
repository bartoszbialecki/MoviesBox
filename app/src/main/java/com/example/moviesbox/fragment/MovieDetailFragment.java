package com.example.moviesbox.fragment;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moviesbox.R;
import com.example.moviesbox.activity.ImageViewerActivity;
import com.example.moviesbox.data.MoviesRepository;
import com.example.moviesbox.model.Movie;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment {
    // region CONSTANTS
    private static final String EXTRA_MOVIE_ID = "movie_id";
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
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

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
    public static MovieDetailFragment newInstance() {
        return newInstance(0);
    }

    public static MovieDetailFragment newInstance(int movieId) {
        MovieDetailFragment fragment = new MovieDetailFragment();
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

        setHasOptionsMenu(true);

        mDateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);

        initArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        mUnbinder = ButterKnife.bind(this, view);

        setupTabs();
        setupUI();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mUnbinder.unbind();
    }
    // endregion

    // region MENU METHODS
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movie_detail, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_favorite_action) {

            return true;
        }

        return super.onOptionsItemSelected(item);
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

    private void setupTabs() {
        PagerAdapter adapter = new PagerAdapter(getChildFragmentManager());
        adapter.addFragment(MovieTrailersFragment.newInstance(mMovie.getId()), getString(R.string.trailers));
        adapter.addFragment(MovieReviewsFragment.newInstance(mMovie.getId()), getString(R.string.reviews));

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
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

    // region INNER CLASSES
    private static class PagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private PagerAdapter(FragmentManager manager) {
            super(manager);
        }

        private void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    // endregion
}
