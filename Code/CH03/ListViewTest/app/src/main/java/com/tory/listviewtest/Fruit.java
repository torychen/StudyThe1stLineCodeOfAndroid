package com.tory.listviewtest;

public class Fruit {
    private String mName;
    private int mImageID;

    public Fruit(String name, int imageID) {
        mName = name;
        mImageID = imageID;
    }

    public String getName()
    {
        return mName;
    }

    public int getImageID() {
        return mImageID;
    }
}
