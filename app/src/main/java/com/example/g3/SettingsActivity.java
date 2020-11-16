package com.example.g3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.g3.settings.ChangePassActivity;
import com.example.g3.settings.EditProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SettingsActivity extends AppCompatActivity {
    Button btnImg, btnPass;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private StorageReference mStorageRef;
    FirebaseStorage storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        firebaseUser = firebaseAuth.getCurrentUser();

        btnImg = findViewById(R.id.btnChangeImg);
        btnPass = findViewById(R.id.btnChangePassword);

        btnPass.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, ChangePassActivity.class);
                startActivity(intent);
            }
        });
        btnImg.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();
                StorageReference file =
                        mStorageRef.child("Images").child(imageUri.getLastPathSegment());
                file.putFile(imageUri).addOnSuccessListener(
                        new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setPhotoUri(downloadUrl)
                                        .build();
                                firebaseUser.updateProfile(profileUpdates).addOnSuccessListener(
                                        new OnSuccessListener<Void>() {
                                            @Override public void onSuccess(Void aVoid) {
                                                Toast.makeText(SettingsActivity.this,"User Profile " +
                                                        "Updated Successfully.",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SettingsActivity.this,"User Profile " +
                                                "Updated Unsuccessfully.",Toast.LENGTH_SHORT).show();
                                    }
                                });


                            }
                        });


            }
        }
    }
}