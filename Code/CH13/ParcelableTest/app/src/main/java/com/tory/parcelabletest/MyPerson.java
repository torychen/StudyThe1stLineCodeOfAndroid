package com.tory.parcelabletest;

import android.os.Parcel;
import android.os.Parcelable;

public class MyPerson implements Parcelable{

    private int age;
    private String name;

    MyPerson(){

    }

    private MyPerson(Parcel in) {
        this.age = in.readInt();
        this.name = in.readString();
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(age);
        parcel.writeString(name);
    }

    public static final Parcelable.Creator<MyPerson> CREATOR = new Parcelable.Creator<MyPerson>() {
        @Override
        public MyPerson createFromParcel(Parcel parcel) {
            return new MyPerson(parcel);
        }

        @Override
        public MyPerson[] newArray(int i) {

            return new MyPerson[i];
        }
    };


}
