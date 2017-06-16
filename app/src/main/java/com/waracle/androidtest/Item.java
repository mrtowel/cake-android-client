package com.waracle.androidtest;

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

}
