package com.example.prins.friendschat.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.example.prins.friendschat.Dtos.GroupDialog;
import com.example.prins.friendschat.Dtos.User;
import com.example.prins.friendschat.Dtos.UserGroups;
import com.example.prins.friendschat.R;
import com.example.prins.friendschat.helpers.PreferenceHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
public class CreateGroupDialogFragment extends DialogFragment{
    //===================================================================
    User me;
    //===================================================================
    ArrayList<User> selectedUsers;
    //===================================================================
    DatabaseReference groupDialogReference;
    DatabaseReference userReference;
    //===================================================================
    int CREATE_PROJECT_REQUEST_CODE = 2;
    //===================================================================
    @BindView(R.id.create_project_name_ed)
    EditText nameEd;
    @BindView(R.id.create_project_btn)
    View projectBtn;
    @BindView(R.id.create_project_btn_progress)
    ProgressBar btnProgress;
    Unbinder unbinder;
    @BindView(R.id.create_project_image)
    ImageView projectImage;
    //===================================================================
    public CreateGroupDialogFragment() {
        // Required empty public constructor
    }

    //===================================================================
    public static DialogFragment newInstance(ArrayList<User> selectedUsers){
        CreateGroupDialogFragment fragment = new CreateGroupDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(User.KEY, selectedUsers);
        fragment.setArguments(bundle);
        return fragment;
    }
    //===================================================================
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_group_dailog, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    //===================================================================
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        me = new PreferenceHandler(getContext()).getUser();
        selectedUsers = getArguments().getParcelableArrayList(User.KEY);
        userReference = FirebaseDatabase.getInstance().getReference(User.KEY);
        groupDialogReference = FirebaseDatabase.getInstance().getReference(GroupDialog.KEY);
    }

    //===================================================================
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //===================================================================
    @OnClick(R.id.create_project_btn)
    public void onViewClicked() {
        if (nameEd.getText().toString().length() > 0) {
            btnProgress.setVisibility(View.VISIBLE);
            projectImage.setVisibility(View.GONE);
            createDialog(selectedUsers, nameEd.getText().toString());
        } else {
            nameEd.setError(getString(R.string.empty_filed));
        }
    }
    //===================================================================
    private void createDialog(final ArrayList<User> users, final String name) {
        final GroupDialog groupDialog = new GroupDialog();
        groupDialog.setId(groupDialogReference.push().getKey());
        groupDialog.setName(name);
        groupDialogReference.child(groupDialog.getId()).setValue(groupDialog)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            DatabaseReference newGroupDialogReference = groupDialogReference.child(groupDialog.getId());
                            DatabaseReference joinedUsersReference = newGroupDialogReference.child(GroupDialog.USERS_KEY);
                            joinedUsersReference.child(me.getUserId()).setValue(me);
                            UserGroups userGroups = new UserGroups();
                            userGroups.setDialogId(groupDialog.getId());
                            userGroups.setName(name);
                            userReference.child(me.getUserId()).child(UserGroups.KEY).child(userGroups.getDialogId())
                                    .setValue(userGroups);
                            for (User user : users) {
                                joinedUsersReference.child(user.getUserId()).setValue(user);
                                userReference.child(user.getUserId()).child(UserGroups.KEY).child(userGroups.getDialogId())
                                        .setValue(userGroups);
                            }
                            Toast.makeText(getContext(), R.string.group_create_success, Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        } else {
                            Toast.makeText(getContext(), getString(R.string.went_wrong), Toast.LENGTH_SHORT).show();
                            dismiss();
                        }

                    }
                });
    }
    //===================================================================
}
