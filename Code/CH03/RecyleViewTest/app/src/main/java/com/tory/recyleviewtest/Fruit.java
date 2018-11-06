package com.tory.recyleviewtest;

public class Fruit {

    private String mName;

    private int mImageId;

    public Fruit(String name, int imageId) {
        this.mName = name;
        this.mImageId = imageId;
    }

    public String getName() {
        return mName;
    }

    public int getImageId() {
        return mImageId;
    }

}
