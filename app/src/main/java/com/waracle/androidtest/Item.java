package com.waracle.androidtest;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single, immutable, top-level data item.
 */
public final class Item {

    private String title;
    private String description;
    private String image;

    public Item(final String title, final String description, final String image) {
        this.title = title;
        this.description = description;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public static List<Item> fromJsonArray(final JSONArray array) {
        List<Item> items = new ArrayList<>();
        if (array == null) {
            return items;
        }

        for (int i = 0; i < array.length(); ++i) {
            JSONObject object;
            try {
                object = array.getJSONObject(i);
                String title = object.getString("title");
                String desc = object.getString("desc");
                String image = object.getString("image");

                items.add(new Item(title, desc, image));
            } catch (JSONException e) {
                Log.d(Item.class.getSimpleName(), "Failed to read JSON: ", e);
            }
        }

        return items;
    }

}
