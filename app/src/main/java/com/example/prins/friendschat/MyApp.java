package com.example.prins.friendschat;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by prins on 2/24/2018.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
