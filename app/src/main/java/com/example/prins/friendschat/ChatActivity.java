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

import com.example.prins.friendschat.Dtos.Dialog;
import com.example.prins.friendschat.Dtos.Msg;
import com.example.prins.friendschat.Dtos.User;
import com.example.prins.friendschat.adapters.ChatAdapter;
import com.example.prins.friendschat.helpers.PreferenceHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends AppCompatActivity {

    ArrayList<Msg> msgs = new ArrayList<>();

    DateFormat df = new SimpleDateFormat("d MMM yyyy, HH:mm", Locale.US);

    User me;

    User receiverUser;

    Dialog dialog;

    DatabaseReference dialogsDatabaseReference;

    ChatAdapter adapter;

    DatabaseReference msgDatabaseReference;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.msg_recycler_view)
    RecyclerView msgRecyclerView;
    @BindView(R.id.msg_ed)
    EditText msgEd;
    @BindView(R.id.send_button)
    Button sendButton;
    @BindView(R.id.send_progress)
    ProgressBar sendProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        receiverUser = getIntent().getParcelableExtra(User.KEY);
        initRecycler();
        dialogsDatabaseReference = FirebaseDatabase.getInstance().getReference(Dialog.KEY);
        me = new PreferenceHandler(this).getUser();
        setDialogFromFirebase();
    }

    private void initRecycler() {
        adapter = new ChatAdapter(new ArrayList<Msg>());
        msgRecyclerView.setAdapter(adapter);
        msgRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setDialogFromFirebase() {
        dialogsDatabaseReference.orderByChild(Dialog.sender_receiver_ids_key).equalTo(me.getUserId() + "," + receiverUser.getUserId())
                .addListenerForSingleValueEvent(sender_receiver_dialogListener);
    }

    ValueEventListener sender_receiver_dialogListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    setDialog(dataSnapshot1.getValue(Dialog.class));
                    msgDatabaseReference = dialogsDatabaseReference.child(dialog.getId()).child(Msg.KEY);
                    msgDatabaseReference.addChildEventListener(msgEventListener);
                }
            } else {
                dialogsDatabaseReference.orderByChild(Dialog.receiver_sender_ids_key).equalTo(me.getUserId() + "," + receiverUser.getUserId())
                        .addListenerForSingleValueEvent(receiver_sender_dialogListener);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    ValueEventListener receiver_sender_dialogListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    setDialog(dataSnapshot1.getValue(Dialog.class));
                    msgDatabaseReference = dialogsDatabaseReference.child(dialog.getId()).child(Msg.KEY);
                    msgDatabaseReference.addChildEventListener(msgEventListener);
                }
            } else {
                final Dialog dialog = new Dialog();
                dialog.setId(dialogsDatabaseReference.push().getKey());
                dialog.setReceiver_sender_ids(receiverUser.getUserId() + "," + me.getUserId());
                dialog.setSender_receiver_ids(me.getUserId() + "," + receiverUser.getUserId());
                dialogsDatabaseReference.child(dialog.getId()).setValue(dialog).addOnCompleteListener(ChatActivity.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {
                            setDialog(dialog);
                            msgDatabaseReference = dialogsDatabaseReference.child(dialog.getId()).child(Msg.KEY);
                            msgDatabaseReference.addChildEventListener(msgEventListener);
                        } else {
                            Toast.makeText(getBaseContext(), "Some thing went wrong, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    ChildEventListener msgEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            adapter.addMsg(dataSnapshot.getValue(Msg.class));
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

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    private void sendMsg(String msgText) {
        Msg msg = new Msg();
        msg.setMessage(msgText);
        msg.setUser(me);
        msg.setTime(df.format(Calendar.getInstance().getTime()));
        msgDatabaseReference.child(msgDatabaseReference.push().getKey()).setValue(msg)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        sendButton.setVisibility(View.VISIBLE);
                        sendProgress.setVisibility(View.GONE);
                        msgEd.setText("");
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dialogsDatabaseReference.removeEventListener(sender_receiver_dialogListener);
        dialogsDatabaseReference.removeEventListener(receiver_sender_dialogListener);
        dialogsDatabaseReference.removeEventListener(msgEventListener);
    }

    @OnClick(R.id.send_button)
    public void onViewClicked() {
        if (!msgEd.getText().toString().isEmpty()) {
            sendButton.setVisibility(View.GONE);
            sendProgress.setVisibility(View.VISIBLE);
            sendMsg(msgEd.getText().toString());
        } else {
            Toast.makeText(this, "Please type a message", Toast.LENGTH_SHORT).show();
        }
    }
}
