package com.example.prins.friendschat.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;


import com.example.prins.friendschat.Dtos.User;
import com.example.prins.friendschat.R;
import com.example.prins.friendschat.helpers.PreferenceHandler;
import com.example.prins.friendschat.helpers.Utils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by prins on 2/25/2018.
 */

public class WidgetAdapter implements RemoteViewsService.RemoteViewsFactory{

    boolean initListenerAgain = false;

    Context context;

    ArrayList<User> users;

    DatabaseReference usersDatabaseReference;

    PreferenceHandler preferenceHandler;

    User me;

    public WidgetAdapter(Context context) {
        this.context = context;
        preferenceHandler = new PreferenceHandler(context);
        usersDatabaseReference = FirebaseDatabase.getInstance().getReference(User.KEY);
        me = preferenceHandler.getUser();
    }

    @Override
    public void onCreate() {
        users = new ArrayList<>();
        if (me != null){
            initListenerAgain = false;
            usersDatabaseReference.addChildEventListener(usersChildEventListener);
        }
        else {
            initListenerAgain = true;
        }
    }

    @Override
    public void onDataSetChanged() {
        me = preferenceHandler.getUser();
        if (me == null){
            users.clear();
            usersDatabaseReference.removeEventListener(usersChildEventListener);
            initListenerAgain = true;
        }
        else {
            if (initListenerAgain){
                initListenerAgain = false;
                usersDatabaseReference.addChildEventListener(usersChildEventListener);
            }
        }
    }

    @Override
    public void onDestroy() {
        usersDatabaseReference.removeEventListener(usersChildEventListener);
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        final User user = users.get(i);
        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_item);
        remoteViews.setTextViewText(R.id.user_name, user.getName());
        if (user.getImage_url() != null){
            try {
                Bitmap bitmap = Picasso.with(context).load(user.getImage_url()).get();
                remoteViews.setImageViewBitmap(R.id.user_image, bitmap);
            } catch (IOException e) {
                remoteViews.setImageViewResource(R.id.user_image, R.mipmap.frinds_logo);
                e.printStackTrace();
            }
        }
        else {
            remoteViews.setImageViewResource(R.id.user_image, R.mipmap.frinds_logo);
        }
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable(User.KEY, user);
        intent.putExtras(bundle);
        remoteViews.setOnClickFillInIntent(R.id.container, intent);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }



    ChildEventListener usersChildEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            User user = dataSnapshot.getValue(User.class);
            if (me != null){
                if (!user.getUserId().equals(me.getUserId())){
                    users.add(user);
                    Utils.updateWidgets(context);
                }
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    private int[] getWidgetIds(){
        return AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, FriendsAppWidget.class));
    }

    public static class Service extends RemoteViewsService {
        @Override
        public RemoteViewsFactory onGetViewFactory(Intent intent) {
            return new WidgetAdapter(this);
        }
    }

}
