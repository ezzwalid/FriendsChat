package com.example.prins.friendschat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.prins.friendschat.Dtos.GroupDialog;
import com.example.prins.friendschat.Dtos.Msg;
import com.example.prins.friendschat.Dtos.User;
import com.example.prins.friendschat.Dtos.UserGroups;
import com.example.prins.friendschat.adapters.ChatAdapter;
import com.example.prins.friendschat.helpers.PreferenceHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GroupChatActivity extends AppCompatActivity {

    User me;

    UserGroups userGroups;

    DateFormat df = new SimpleDateFormat("d MMM yyyy, HH:mm", Locale.US);

    ChatAdapter chatAdapter;

    ArrayList<Msg> msgs = new ArrayList<>();

    DatabaseReference groupMsgsReference;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.chat_recycler)
    RecyclerView chatRecycler;
    @BindView(R.id.msg_ed)
    EditText msgEd;
    @BindView(R.id.send_button)
    Button sendButton;
    @BindView(R.id.send_progress)
    ProgressBar sendProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        ButterKnife.bind(this);
        me = new PreferenceHandler(this).getUser();
        setSupportActionBar(toolbar);
        userGroups = getIntent().getParcelableExtra(UserGroups.KEY);
        groupMsgsReference = FirebaseDatabase.getInstance().getReference(GroupDialog.KEY)
                .child(userGroups.getDialogId()).child(GroupDialog.MSG_KEY);
        groupMsgsReference.addChildEventListener(msgChildEventListener);
        initRecycler();
    }

    private void initRecycler() {
        chatAdapter = new ChatAdapter(msgs);
        chatRecycler.setAdapter(chatAdapter);
        chatRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    ChildEventListener msgChildEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            msgs.add(dataSnapshot.getValue(Msg.class));
            chatAdapter.notifyDataSetChanged();
            chatRecycler.scrollToPosition(msgs.size() - 1);
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

    private void sendMsg(String messageText) {

        sendProgress.setVisibility(View.VISIBLE);
        sendButton.setVisibility(View.GONE);

        Msg msg = new Msg();
        msg.setMessage(messageText);
        msg.setUser(me);
        msg.setTime(df.format(Calendar.getInstance().getTime()));
        groupMsgsReference.child(groupMsgsReference.push().getKey()).setValue(msg).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                sendProgress.setVisibility(View.GONE);
                sendButton.setVisibility(View.VISIBLE);
                if (task.isSuccessful()){
                    msgEd.setText("");
                }
                else {
                    Toast.makeText(getBaseContext(), "Sorry some thing went wrong, please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        groupMsgsReference.removeEventListener(msgChildEventListener);
    }

    @OnClick(R.id.send_button)
    public void onViewClicked() {
        if (!msgEd.getText().toString().isEmpty()) {
            sendMsg(msgEd.getText().toString());
        } else {
            Toast.makeText(this, "Please type a message", Toast.LENGTH_SHORT).show();
        }
    }
}
