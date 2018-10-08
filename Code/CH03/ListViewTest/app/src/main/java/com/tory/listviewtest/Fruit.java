package com.tory.listviewtest;

public class Fruit {
    private String m_name;
    private int m_imageID;

    public Fruit(String name, int imageID) {
        m_name = name;
        m_imageID = imageID;
    }

    public String getName()
    {
        return m_name;
    }

    public int getImageID() {
        return m_imageID;
    }
}
