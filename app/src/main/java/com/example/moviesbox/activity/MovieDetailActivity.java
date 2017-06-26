package com.example.moviesbox.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.moviesbox.R;
import com.example.moviesbox.fragment.MovieDetailFragment;
import com.example.moviesbox.model.Movie;

public class MovieDetailActivity extends AppCompatActivity {
    // region CONSTANTS
    public static final String EXTRA_MOVIE = "movie";
    // endregion

    // region VARIABLES
    private Movie mMovie;
    // endregion

    // region LIFE CYCLE METHODS
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_detail);

        setupActionBar();
        initArguments();

        if (savedInstanceState == null) {
            MovieDetailFragment fragment = MovieDetailFragment.newInstance(mMovie);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
        }
    }
    // endregion

    // region MENU METHODS
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    supportFinishAfterTransition();
                } else {
                    NavUtils.navigateUpFromSameTask(this);
                }

                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    // endregion

    // region PRIVATE METHODS
    private void initArguments() {
        Bundle extras = getIntent().getExtras();

        if (null != extras) {
            if (extras.containsKey(EXTRA_MOVIE)) {
                mMovie = extras.getParcelable(EXTRA_MOVIE);
            }
        }
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();

        if (null != actionBar) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    // endregion
}
