package com.example.prins.friendschat.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;


import com.example.prins.friendschat.Dtos.User;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by EzzWalid on 8/29/2017.
 */

public class PreferenceHandler {
    Context context;
    //===================================================================
    private final String USER_ID_KEY = "IDUser";
    private final String USER_EMAIL_KEY = "EMAILUser";
    private final String USER_IMAGE_KEY = "ImageEUser";
    private final String USER_NAME_KEY = "NAMEUser";
    //===================================================================
    private final String PREFERENCE_KEY = "FRIENDS_PREFERENCE";
    //===================================================================
    private SharedPreferences sharedPreferences;
    //===================================================================
    public PreferenceHandler(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREFERENCE_KEY, MODE_PRIVATE);
    }
    //===================================================================
    public void addUser(User user){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_ID_KEY, user.getUserId());
        editor.putString(USER_EMAIL_KEY, user.getE_mail());
        editor.putString(USER_NAME_KEY, user.getName());
        editor.putString(USER_IMAGE_KEY, user.getImage_url());
        editor.commit();
        Utils.updateWidgets(context);
    }
    //===================================================================
    public User getUser(){
        User user = new User();
        if (sharedPreferences.getString(USER_ID_KEY, null) == null){
            return null;
        }
        user.setUserId(sharedPreferences.getString(USER_ID_KEY, null));
        user.setE_mail(sharedPreferences.getString(USER_EMAIL_KEY, null));
        user.setName(sharedPreferences.getString(USER_NAME_KEY, null));
        user.setImage_url(sharedPreferences.getString(USER_IMAGE_KEY, null));
        return user;
    }
    //===================================================================
    public void clearUserData(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(USER_ID_KEY);
        editor.remove(USER_EMAIL_KEY);
        editor.remove(USER_NAME_KEY);
        editor.remove(USER_IMAGE_KEY);
        editor.commit();
        Utils.updateWidgets(context);
    }
}
