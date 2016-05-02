package com.norigin.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Helper class for working with images
 *
 * Created by ibohdan on 4/29/2016.
 */
public class ImageHelper {

    private static final String TAG = ImageHelper.class.getSimpleName();

    /**
     * Calculate sample size for decoding image
     *
     * @param options   instance of BitmapFactory.Options
     * @param reqWidth  width of target image
     * @param reqHeight height of target image
     * @return calculated sample size
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * Returns scaled bitmap to selected size
     *
     * @param res       resource instance
     * @param path      path to image in assets
     * @param reqWidth  width of target image
     * @param reqHeight height of target image
     * @return resized bitmap
     */
    @Nullable
    public static Bitmap decodeSampledBitmapFromResource(Resources res, String path, int reqWidth, int reqHeight) {
        InputStream stream;
        try {
            stream = res.getAssets().open(path);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(stream, null, options);

        try {
            stream.close();
        } catch (IOException e) {
            Log.e(TAG, "decodeSampledBitmapFromResource", e);
        }

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        try {
            stream = res.getAssets().open(path);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        // Decode bitmap with new options
        Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);

        try {
            stream.close();
        } catch (IOException e) {
            Log.e(TAG, "decodeSampledBitmapFromResource", e);
        }
        return bitmap;
    }
}
