package com.example.prins.friendschat.Dtos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by prins on 2/24/2018.
 */

public class UserGroups implements Parcelable{
    public static final String KEY = "UserGroups";
    private String dialogId;
    private String name;

    protected UserGroups(Parcel in) {
        dialogId = in.readString();
        name = in.readString();
    }

    public UserGroups() {
    }

    public static final Creator<UserGroups> CREATOR = new Creator<UserGroups>() {
        @Override
        public UserGroups createFromParcel(Parcel in) {
            return new UserGroups(in);
        }

        @Override
        public UserGroups[] newArray(int size) {
            return new UserGroups[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDialogId() {
        return dialogId;
    }

    public void setDialogId(String dialogId) {
        this.dialogId = dialogId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(dialogId);
        parcel.writeString(name);
    }
}
