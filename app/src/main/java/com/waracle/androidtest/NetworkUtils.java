package com.waracle.androidtest;


import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


/**
 * Utils for manipulation of network response data.
 */
public class NetworkUtils {

    private static final String ENCODING_UTF_8 = "UTF-8";
    private static final String TAG = NetworkUtils.class.getSimpleName();

    static List<Item> parseResponseData(final NetworkResponse response) {
        List<Item> items = new ArrayList<>();
        byte[] body = response.getBody();
        if (body == null || body.length == 0) {
            return items;
        }

        String encoding = parseCharset(response.getContentType());
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(new String(body, Charset.forName(encoding)));
        } catch (final JSONException e) {
            Log.d(TAG, "Failed to parse JSON array", e);
        }

        return Item.fromJsonArray(jsonArray);
    }

    /**
     * Returns the charset specified in the Content-Type of this header,
     * or the HTTP most common (UTF-8) if none can be found.
     */
    private static String parseCharset(final String contentType) {
        if (TextUtils.isEmpty(contentType)) {
            return ENCODING_UTF_8;
        }

        String[] params = contentType.split(",");
        if (params.length < 1) {
            return ENCODING_UTF_8;
        }

        for (final String param : params) {
            String[] pair = param.trim().split("=");
            if (pair.length == 2 && "charset".equals(pair[0])) {
                return pair[1];
            }
        }

        return ENCODING_UTF_8;
    }

}
