package com.waracle.androidtest;

import android.content.Context;
import android.util.Log;

import java.io.File;


public class HttpCache {

    public static void install(final Context context) {
        try {
            File httpCacheDir = new File(context.getCacheDir(), "http");
            long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
            Class.forName("android.net.http.HttpResponseCache")
                    .getMethod("install", File.class, long.class)
                    .invoke(null, httpCacheDir, httpCacheSize);
        } catch (Exception e) {
            Log.d(HttpCache.class.getSimpleName(), "Error", e);
        }
    }

}
