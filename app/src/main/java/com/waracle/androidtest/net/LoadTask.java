package com.waracle.androidtest.net;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.waracle.androidtest.NetworkResponse;
import com.waracle.androidtest.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Executes network call in background. Non-null {@link Callback} must be provided.
 *
 * @see Callback
 * @see NetworkResponse
 */
public final class LoadTask extends AsyncTask<String, Void, NetworkResponse> {

    private static final String TAG = LoadTask.class.getSimpleName();
    private static final int CONNECTION_TIMEOUT = 10 * 1000;
    private static final String HEADER_KEY_LOCATION = "Location";

    private Callback callback;

    public LoadTask(@NonNull final Callback callback) {
        this.callback = callback;
    }

    @Override
    protected NetworkResponse doInBackground(final String... params) {
        URL url;
        byte[] readBytes;
        NetworkResponse response = null;
        try {
            url = new URL(params[0]);
        } catch (final MalformedURLException e) {
            Log.d(TAG, "Failed to create URL", e);
            return null;
        }

        HttpURLConnection urlConnection = null;
        InputStream stream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();

            // Set up connection
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            urlConnection.setReadTimeout(CONNECTION_TIMEOUT);
            urlConnection.setUseCaches(true);
            urlConnection.setInstanceFollowRedirects(true);

            // Follow redirects just once
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_MOVED_TEMP
                    || responseCode == HttpURLConnection.HTTP_MOVED_PERM
                    || responseCode == HttpURLConnection.HTTP_SEE_OTHER) {
                // Get redirect url from "location" header field
                String newUrl = urlConnection.getHeaderField(HEADER_KEY_LOCATION);

                // Open the new connection again
                urlConnection = (HttpURLConnection) new URL(newUrl).openConnection();
                responseCode = urlConnection.getResponseCode();
            }

            // Parse response body if status 200
            if (responseCode == HttpURLConnection.HTTP_OK) {
                stream = urlConnection.getInputStream();
                readBytes = StreamUtils.read(stream);
                response = new NetworkResponse(
                        responseCode, readBytes, urlConnection.getContentType());
            }
        } catch (final IOException e) {
            Log.d(TAG, "Failed to open connection", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            StreamUtils.close(stream);
        }

        return response;
    }

    @Override
    protected void onPostExecute(final NetworkResponse response) {
        callback.onLoaded(response);
    }

    /**
     * Executed on network response.
     */
    public interface Callback {

        void onLoaded(NetworkResponse response);

    }

}
