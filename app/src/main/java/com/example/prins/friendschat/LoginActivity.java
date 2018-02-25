package com.example.prins.friendschat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prins.friendschat.Dtos.User;
import com.example.prins.friendschat.helpers.PreferenceHandler;
import com.example.prins.friendschat.helpers.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.email_ed)
    EditText emailEd;
    @BindView(R.id.password_ed)
    EditText passwordEd;
    @BindView(R.id.login_btn)
    Button loginBtn;
    @BindView(R.id.regester_btn)
    TextView regesterBtn;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    ValueEventListener userCallback;
    PreferenceHandler preferenceHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        preferenceHandler = new PreferenceHandler(this);
        if (preferenceHandler.getUser() != null){
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference((getString(R.string.data_base_users_reference)));
    }

    @OnClick({R.id.login_btn, R.id.regester_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                if (validate()){
                    login();
                }
                break;
            case R.id.regester_btn:
                goToSignUp();
                break;
        }
    }

    private void goToSignUp(){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    private boolean validate(){
        if (Utils.isEmailValid(emailEd.getText().toString()) && !passwordEd.getText().toString().isEmpty()){
            return true;
        }
        return false;
    }

    private void login(){
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(emailEd.getText().toString(), passwordEd.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()){
                    getUserFromDb(task.getResult().getUser().getUid());
                }
                else {
                    Log.e("Login", task.getException().getLocalizedMessage());
                }
            }
        });
    }

    private void getUserFromDb(String userId){
        progressBar.setVisibility(View.VISIBLE);
        userCallback = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                preferenceHandler.addUser(dataSnapshot.getValue(User.class));
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getBaseContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        databaseReference.child(userId).addListenerForSingleValueEvent(userCallback);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (userCallback != null){
            databaseReference.removeEventListener(userCallback);
        }
    }
}
