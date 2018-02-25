package com.example.prins.friendschat.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.prins.friendschat.CreateGroupActivity;
import com.example.prins.friendschat.Dtos.GroupDialog;
import com.example.prins.friendschat.Dtos.User;
import com.example.prins.friendschat.Dtos.UserGroups;
import com.example.prins.friendschat.GroupChatActivity;
import com.example.prins.friendschat.R;
import com.example.prins.friendschat.adapters.GroupsAdapter;
import com.example.prins.friendschat.helpers.PreferenceHandler;
import com.example.prins.friendschat.listeners.RecyclerItemClickListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupsFragment extends Fragment implements RecyclerItemClickListener<UserGroups>{

    User me;

    DatabaseReference userGroupsDatabaseReference;
    GroupsAdapter adapter;
    ArrayList<UserGroups> userGroups;

    @BindView(R.id.groups_recycler)
    RecyclerView groupsRecycler;
    @BindView(R.id.fab_add)
    FloatingActionButton fabAdd;
    Unbinder unbinder;

    public GroupsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_grops, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        me = new PreferenceHandler(getContext()).getUser();
        userGroupsDatabaseReference = FirebaseDatabase.getInstance().getReference(User.KEY).child(me.getUserId())
        .child(UserGroups.KEY);
        userGroups = new ArrayList<>();
        initRecycler();
        userGroupsDatabaseReference.addChildEventListener(groupChildEventListener);
    }

    private void initRecycler(){
        adapter = new GroupsAdapter(userGroups, this);
        groupsRecycler.setAdapter(adapter);
        groupsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    ChildEventListener groupChildEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            userGroups.add(dataSnapshot.getValue(UserGroups.class));
            adapter.notifyDataSetChanged();
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


    @OnClick(R.id.fab_add)
    public void onViewClicked() {
        Intent intent = new Intent(getContext(), CreateGroupActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(UserGroups o) {
        Intent intent = new Intent(getContext(), GroupChatActivity.class);
        intent.putExtra(UserGroups.KEY, o);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        userGroupsDatabaseReference.removeEventListener(groupChildEventListener);
        unbinder.unbind();
    }
}
