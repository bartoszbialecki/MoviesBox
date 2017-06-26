package com.example.moviesbox.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;

import com.example.moviesbox.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageViewerActivity extends AppCompatActivity {
    // region CONSTANTS
    public static final String EXTRA_IMAGE_PATH = "imagePath";
    // endregion

    // region VARIABLES
    @BindView(R.id.image_view)
    PhotoView photoView;

    private String mImagePath;
    // endregion

    // region LIFE CYCLE METHODS
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image_viewer);

        ButterKnife.bind(this);

        setupActionBar();
        initArguments();
        setupUI();
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
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();

        if (null != actionBar) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void initArguments() {
        Bundle extras = getIntent().getExtras();

        if (null != extras) {
            if (extras.containsKey(EXTRA_IMAGE_PATH)) {
                mImagePath = extras.getString(EXTRA_IMAGE_PATH);
            }
        }
    }

    private void setupUI() {
        if (!TextUtils.isEmpty(mImagePath)) {
            Picasso.with(this)
                    .load(mImagePath)
                    .placeholder(R.color.background)
                    .into(photoView);
        }
    }
    // endregion
}
