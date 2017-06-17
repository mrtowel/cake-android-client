package com.waracle.androidtest.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.waracle.androidtest.net.LoadTask;
import com.waracle.androidtest.net.NetworkResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Loads images from internet or cache if available.
 *
 * @see LruCache
 * @see LoadTask
 */
final class ImageLoader {

    private LruCache<String, Bitmap> mMemoryCache;
    private Map<String, LoadTask> mLoadTasks1;

    ImageLoader() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(final String key, final Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };

        mLoadTasks1 = new HashMap<>();
    }

    private static Bitmap convertToBitmap(final byte[] data) {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    void loadImage(final String url, final ImageView imageView,
                          final ItemListFragment.ItemAdapter.ViewHolder holder,
                          final int position) {
        // Load from cache
        Bitmap bitmapFromMemCache = getBitmapFromMemCache(url);
        if (bitmapFromMemCache != null) {
            imageView.setImageBitmap(bitmapFromMemCache);
            return;
        }

        // Cancel load request if view was recycled and remove from loading list
        if (holder.position != -1 && holder.position != position) {
            LoadTask loadTask1 = mLoadTasks1.get(url);
            if (loadTask1 != null && !loadTask1.isCancelled()) {
                loadTask1.cancel(true);
                mLoadTasks1.remove(url);
                return;
            }
        }

        // Load from internet and add to loading list
        LoadTask loadTask = new LoadTask(new LoadTask.Callback() {
            @Override
            public void onLoaded(final NetworkResponse response) {
                Bitmap bitmap = convertToBitmap(response.getBody());
                addBitmapToMemoryCache(url, bitmap);

                if (holder.position == position) {
                    imageView.setImageBitmap(bitmap);
                }
                mLoadTasks1.remove(url);
            }
        });
        mLoadTasks1.put(url, loadTask);
        loadTask.execute(url);
    }

    private void addBitmapToMemoryCache(final String key, final Bitmap bitmap) {
        if (key == null || bitmap == null) {
            return;
        }

        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    private Bitmap getBitmapFromMemCache(final String key) {
        return mMemoryCache.get(key);
    }

}
