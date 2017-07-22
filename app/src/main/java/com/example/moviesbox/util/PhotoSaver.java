package com.example.moviesbox.util;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;

public class PhotoSaver implements Target {
    // region VARIABLES
    private final String mImageName;
    // endregion

    // region CONSTRUCTORS
    public PhotoSaver(String imageName) {
        this.mImageName = imageName;
    }
    // endregion

    // region TARGET METHODS
    @Override
    public void onPrepareLoad(Drawable arg0) {

    }

    @Override
    public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom arg1) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + mImageName);

                try {
                    //noinspection ResultOfMethodCallIgnored
                    file.createNewFile();
                    FileOutputStream ostream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 75, ostream);
                    ostream.close();
                } catch (Exception ignored) {

                }
            }
        }).start();
    }

    @Override
    public void onBitmapFailed(Drawable arg0) {

    }
    // endregion
}
