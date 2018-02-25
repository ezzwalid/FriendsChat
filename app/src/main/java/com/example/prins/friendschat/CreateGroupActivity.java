package com.example.prins.friendschat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bignerdranch.android.multiselector.MultiSelector;
import com.example.prins.friendschat.Dtos.GroupDialog;
import com.example.prins.friendschat.Dtos.User;
import com.example.prins.friendschat.Dtos.UserGroups;
import com.example.prins.friendschat.adapters.UsersMultiSelectAdapter;
import com.example.prins.friendschat.fragments.CreateGroupDialogFragment;
import com.example.prins.friendschat.helpers.PreferenceHandler;
import com.example.prins.friendschat.listeners.RecyclerItemClickListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateGroupActivity extends AppCompatActivity{

    UsersMultiSelectAdapter adapter;

    User me;

    ArrayList<User> users = new ArrayList<>();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.fab_donne)
    FloatingActionButton fabDonne;

    DatabaseReference userReference;

    MultiSelector multiSelector;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        me = new PreferenceHandler(this).getUser();
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        initRecycler();
        userReference = FirebaseDatabase.getInstance().getReference(User.KEY);
        userReference.addChildEventListener(userChildEventListener);
    }


    private void initRecycler() {
        multiSelector = new MultiSelector();
        adapter = new UsersMultiSelectAdapter(users, multiSelector);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @OnClick(R.id.fab_donne)
    public void onViewClicked() {
        if (multiSelector.getSelectedPositions().size() > 2) {
            progressBar.setVisibility(View.VISIBLE);
            ArrayList<User> selectedUsers = new ArrayList<>();
            for (int selectedPosition : multiSelector.getSelectedPositions()) {
                selectedUsers.add(users.get(selectedPosition));
            }
            CreateGroupDialogFragment.newInstance(selectedUsers).show(getSupportFragmentManager(), "");
        }
        else {
            Toast.makeText(this, "Please select at least three useres to join the group.", Toast.LENGTH_SHORT).show();
        }
    }


    ChildEventListener userChildEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            User user = dataSnapshot.getValue(User.class);
            if (!user.getUserId().equals(me.getUserId())){
                users.add(user);
                adapter.notifyDataSetChanged();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userReference.removeEventListener(userChildEventListener);
    }
}
