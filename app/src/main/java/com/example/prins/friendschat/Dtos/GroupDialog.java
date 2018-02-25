package com.example.prins.friendschat.Dtos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by prins on 2/24/2018.
 */

public class GroupDialog implements Parcelable{
    public static final String KEY = "GroupDialog";
    public static final String USERS_KEY = "joinedUsers";
    public static final String MSG_KEY = "messages";

    private String id;
    private String name;
    private User joinedUsers;
    private Msg messages;

    public GroupDialog() {
    }

    protected GroupDialog(Parcel in) {
        id = in.readString();
        name = in.readString();
        joinedUsers = in.readParcelable(User.class.getClassLoader());
        messages = in.readParcelable(Msg.class.getClassLoader());
    }

    public static final Creator<GroupDialog> CREATOR = new Creator<GroupDialog>() {
        @Override
        public GroupDialog createFromParcel(Parcel in) {
            return new GroupDialog(in);
        }

        @Override
        public GroupDialog[] newArray(int size) {
            return new GroupDialog[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getJoinedUsers() {
        return joinedUsers;
    }

    public void setJoinedUsers(User joinedUsers) {
        this.joinedUsers = joinedUsers;
    }


    public Msg getMessages() {
        return messages;
    }

    public void setMessages(Msg messages) {
        this.messages = messages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeParcelable(joinedUsers, i);
        parcel.writeParcelable(messages, i);
    }
}
