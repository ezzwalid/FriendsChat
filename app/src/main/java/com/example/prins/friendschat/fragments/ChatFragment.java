package com.example.prins.friendschat.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.prins.friendschat.ChatActivity;
import com.example.prins.friendschat.Dtos.User;
import com.example.prins.friendschat.R;
import com.example.prins.friendschat.adapters.UsersAdapter;
import com.example.prins.friendschat.helpers.PreferenceHandler;
import com.example.prins.friendschat.listeners.RecyclerItemClickListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment implements RecyclerItemClickListener<User>{

    User me;

    ArrayList<User> users;

    UsersAdapter adapter;
    @BindView(R.id.chat_recycler)
    RecyclerView chatRecycler;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    Unbinder unbinder;

    DatabaseReference databaseReference;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(getString(R.string.data_base_users_reference));
        users = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        me = new PreferenceHandler(getContext()).getUser();
        adapter = new UsersAdapter(users, this);
        initRecycler();
        databaseReference.addChildEventListener(usersChildEventListener);
    }

    private void initRecycler(){
        chatRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        chatRecycler.setAdapter(adapter);
    }


    @Override
    public void onClick(User o) {
        Intent intent = new Intent(getContext(), ChatActivity.class);
        intent.putExtra(User.KEY, o);
        startActivity(intent);
    }



    ValueEventListener singleUsersEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            progressBar.setVisibility(View.GONE);
            for (DataSnapshot dataSnapshots : dataSnapshot.getChildren()){
                User user = dataSnapshots.getValue(User.class);
                if (!user.getUserId().equals(me.getUserId())){
                    users.add(user);
                }
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    ChildEventListener usersChildEventListener = new ChildEventListener() {
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        databaseReference.removeEventListener(usersChildEventListener);
    }


}
