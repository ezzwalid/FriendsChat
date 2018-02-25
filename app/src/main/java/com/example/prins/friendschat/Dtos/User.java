package com.example.prins.friendschat.Dtos;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

/**
 * Created by ali on 7/11/2017.
 */

public class User implements Parcelable{

    public static final String KEY = "users";

    private String name;
    private String e_mail;
    private String userId;
    private String image_url;

    public User() {
    }

    protected User(Parcel in) {
        name = in.readString();
        e_mail = in.readString();
        userId = in.readString();
        image_url = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getE_mail() {
        return e_mail;
    }

    public void setE_mail(String e_mail) {
        this.e_mail = e_mail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(e_mail);
        parcel.writeString(userId);
        parcel.writeString(image_url);
    }
}
