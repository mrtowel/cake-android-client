package com.waracle.androidtest.net;

import android.content.Context;
import android.util.Log;

import java.io.File;


/**
 * Enables response cache on non-secure connections for devices older than API level 14.
 *
 * @see <a href="https://developer.android.com/reference/android/net/http/HttpResponseCache.html">HttpResponseCache</a>
 */
public class HttpCache {

    private static final int CACHE_SIZE = 10 * 1024 * 1024;

    private HttpCache() {
    }

    public static void install(final Context context) {
        try {
            File httpCacheDir = new File(context.getCacheDir(), "http");
            Class.forName("android.net.http.HttpResponseCache")
                    .getMethod("install", File.class, long.class)
                    .invoke(null, httpCacheDir, CACHE_SIZE);
        } catch (final Exception e) {
            Log.d(HttpCache.class.getSimpleName(), "Error", e);
        }
    }

}
