package com.waracle.androidtest;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Riad on 20/05/2015.
 */
public class StreamUtils {

    private static final String TAG = StreamUtils.class.getSimpleName();

    private StreamUtils() {
    }

    // Can you see what's wrong with this???

    // Data can be buffered while reading an InputStream
    // No point adding bytes to List and then converting List to array
    public static byte[] readUnknownFully(final InputStream stream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int count;
        byte[] tmpData = new byte[4096];

        // Read in stream of bytes
        while ((count = stream.read(tmpData, 0, tmpData.length)) != -1) {
            byteArrayOutputStream.write(tmpData, 0, count);
        }

        // Return the raw byte array.
        return byteArrayOutputStream.toByteArray();
    }

    public static void close(final Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (final IOException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }
}
