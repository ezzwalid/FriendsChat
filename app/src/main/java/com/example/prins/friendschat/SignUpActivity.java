package com.example.prins.friendschat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.prins.friendschat.Dtos.User;
import com.example.prins.friendschat.async.AsyncAction;
import com.example.prins.friendschat.async.OnRequestCompletedListener;
import com.example.prins.friendschat.async.UiException;
import com.example.prins.friendschat.helpers.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.user_image)
    ImageView userImage;
    @BindView(R.id.user_name_ed)
    EditText userNameEd;
    @BindView(R.id.email_ed)
    EditText emailEd;
    @BindView(R.id.password_ed)
    EditText passwordEd;
    @BindView(R.id.re_password_ed)
    EditText rePasswordEd;
    @BindView(R.id.register_btn)
    Button registerBtn;
    @BindView(R.id.progressBar2)
    ProgressBar progressBar;

    FirebaseAuth auth;
    StorageReference storageReference;
    private final int PICK_IMAGE = 6;
    String imagePath;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        databaseReference = FirebaseDatabase.getInstance().getReference(getString(R.string.data_base_users_reference));
        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(getString(R.string.storage_reference));
        setupAuth();
    }

    private void setupAuth(){
        auth = FirebaseAuth.getInstance();
    }

    @OnClick({R.id.register_btn, R.id.user_image})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.register_btn:
                if (validate())
                    createUser();
                break;
            case R.id.user_image:
                requestImage();
                break;
        }
    }

    private boolean validate(){
        if (!Utils.isEmailValid(emailEd.getText().toString()) || userNameEd.getText().toString().isEmpty() || passwordEd.getText().toString().isEmpty() || !rePasswordEd.getText().toString().equals(passwordEd.getText().toString()) ){
            Toast.makeText(this, "Enter all of your information please.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void createUser(){
        progressBar.setVisibility(View.VISIBLE);
        auth.createUserWithEmailAndPassword(emailEd.getText().toString().trim(), passwordEd.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()){
                            addUserToDB(task.getResult().getUser().getUid());
                        }
                        else {
                            Toast.makeText(getBaseContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void addUserToDB(String userId){
        progressBar.setVisibility(View.VISIBLE);
        User user = new User();
        user.setUserId(userId);
        user.setE_mail(emailEd.getText().toString());
        user.setName(userNameEd.getText().toString());
        user.setImage_url(imagePath);

        databaseReference.child(user.getUserId()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()){
                    Toast.makeText(getBaseContext(), "User register successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    Toast.makeText(getBaseContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void saveImage(Uri path){
        progressBar.setVisibility(View.VISIBLE);
        String file_name = path.getPath().substring(path.getPath().lastIndexOf("/") + 1);
        StorageReference childReference = storageReference.child(file_name);
        childReference.putFile(path).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()){
                    imagePath = task.getResult().getDownloadUrl().toString();
                }
                else {
                    Log.e("SignUp", task.getException().getLocalizedMessage());
                }
            }
        });
    }

    private void requestImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK){

            Uri filePath = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                userImage.setImageBitmap(bitmap);
                saveImage(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


}
