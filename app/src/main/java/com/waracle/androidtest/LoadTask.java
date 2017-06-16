package com.waracle.androidtest;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by przemek on 16/06/2017.
 */
public class LoadTask extends AsyncTask<String, Void, byte[]> {

    private static final String TAG = LoadTask.class.getSimpleName();

    private Callback callback;

    public LoadTask(@NonNull final Callback callback) {
        this.callback = callback;
    }

    @Override
    protected byte[] doInBackground(final String... params) {
        URL url;
        byte[] readBytes = new byte[0];
        try {
            url = new URL(params[0]);
        } catch (final MalformedURLException e) {
            Log.d(TAG, "Failed to create URL", e);
            return readBytes;
        }

        HttpURLConnection urlConnection;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                readBytes = StreamUtils.read(urlConnection.getInputStream());
            }
        } catch (final IOException e) {
            Log.d(TAG, "Failed to open connection", e);
            return readBytes;
        }

        return readBytes;
    }

    @Override
    protected void onPostExecute(final byte[] bytes) {
        callback.onLoaded();
    }

    interface Callback {

        void onLoaded();

    }

}
