package com.example.ordernow.Models;

public class FavoriteItem {
    private String name;
    private int imageResId;

    public FavoriteItem(String name, int imageResId) {
        this.name = name;
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public int getImageResId() {
        return imageResId;
    }
}
