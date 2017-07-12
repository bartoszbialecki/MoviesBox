package com.example.moviesbox.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.content.res.AppCompatResources;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.moviesbox.R;
import com.example.moviesbox.fragment.MovieDetailFragment;
import com.example.moviesbox.fragment.MoviesFragment;
import com.example.moviesbox.model.Movie;

public class MainActivity extends AppCompatActivity implements MoviesFragment.OnMovieSelectedListener {
    // region CONSTANTS
    private static final String MOVIE_DETAIL_FRAGMENT_TAG = "MovieDetailFragment";
    // endregion

    // region VARIABLES
    private boolean mTwoPane = false;
    // endregion

    // region LIFE CYCLE METHODS
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, MovieDetailFragment.newInstance(), MOVIE_DETAIL_FRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
    }
    // endregion

    // region MENU METHODS
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem settingsMenuItem = menu.getItem(0);
        Drawable icon = AppCompatResources.getDrawable(this, R.drawable.ic_settings);

        if (icon != null) {
            icon = icon.mutate();
            icon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

            settingsMenuItem.setIcon(icon);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_settings_action) {
            startActivity(new Intent(this, SettingsActivity.class));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    // endregion

    // region MOVIE SELECTED LISTENER
    @Override
    public void onMovieSelected(Movie movie, ImageView sharedImageView) {
        if (mTwoPane) {
            MovieDetailFragment fragment = MovieDetailFragment.newInstance(movie.getId());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, MOVIE_DETAIL_FRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_ID, movie.getId());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(this, sharedImageView, getString(R.string.poster_transition_name));
                startActivity(intent, options.toBundle());
            } else {
                startActivity(intent);
            }
        }
    }
    // endregion
}
