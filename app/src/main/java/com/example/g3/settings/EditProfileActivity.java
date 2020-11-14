package com.example.g3.settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.g3.MainPageActivity;
import com.example.g3.R;
import com.example.g3.SettingsActivity;
import com.example.g3.covid.AffectedCountries;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    CircleImageView profileImg;
    Button btnSave,btnClose;
    TextView profileChangeBtn;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;

    Uri imgUri;
    String myUri= "";
    Task<Uri> uploadTask;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        getSupportActionBar().setTitle("Edit Profile Picture");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        profileImg = findViewById(R.id.profileImg);
        btnSave = findViewById(R.id.btnSave);
        btnClose = findViewById(R.id.btnClose);
        profileChangeBtn = findViewById(R.id.change_profile_btn);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                startActivity(new Intent(EditProfileActivity.this, MainPageActivity.class));
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                uploadProfileImage();
            }
        });

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                CropImage.activity().setAspectRatio(1,1).start(EditProfileActivity.this);
            }
        });

        profileChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                CropImage.activity().setAspectRatio(1,1).start(EditProfileActivity.this);
            }
        });
        
        getUsrInfo();
    }

    private void getUsrInfo() {
        DocumentReference df = firestore.collection("user").document(firebaseAuth.getCurrentUser().getUid());
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String img = documentSnapshot.getString("userProfileUri");
                    if(!img.isEmpty()){
                        Picasso.get().load(img).into(profileImg);
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imgUri = result.getUri();
            profileImg.setImageURI(imgUri);
        }
        else{
            Toast.makeText(this,"Image upload fail, please try again",Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadProfileImage(){
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Set your profile picture");
        pd.setMessage("Please wait, while we are setting your data");
        pd.show();

        if(imgUri !=null){
            final StorageReference fileRef =
                    storageReference.child("Images").child(firebaseAuth.getCurrentUser().getUid()+
                    ".jpg");
            uploadTask = fileRef.putFile(imgUri).continueWithTask(
                    new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task)
                                throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            // Continue with the task to get the download URL
                            return fileRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override public void onComplete(@NonNull Task<Uri> task) {
                    Uri downloadUri = task.getResult();

                    myUri = downloadUri.toString();

                    firestore.collection("user").document(firebaseAuth.getUid()).update(
                            "userProfileUri",myUri);
                    Toast.makeText(EditProfileActivity.this,"User Profile " +
                                    "Updated Successfully.",
                            Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                        @Override public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(EditProfileActivity.this,"Updated Unsuccessfully.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    });
            }
    }
}