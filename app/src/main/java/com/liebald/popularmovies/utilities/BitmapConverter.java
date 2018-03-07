package com.liebald.popularmovies.utilities;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * Helper class to convert bitmaps to byte arrays for storage as blobs in the database.
 */
// Based on https://stackoverflow.com/questions/11790104/how-to-storebitmap-image-and-retrieve-image-from-sqlite-database-in-android
public class BitmapConverter {


    /**
     * Converts the given bitmap to a byte array.
     *
     * @param bitmap The bitmap to convert.
     * @return The bitmap as byte array.
     */
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    /**
     * Converts the given byte array of an image to its bitmap representation.
     *
     * @param image The image as byte array.
     * @return The image as bitmap.
     */
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

}
