package com.example.g3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;

    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                if (firebaseUser != null) {
                    Toast.makeText(MainActivity.this,
                            "Login Sucessfully.\nWelcome Back\n"
                                    + firebaseUser.getEmail()
                            ,
                            Toast.LENGTH_LONG).show();
                    DocumentReference df =
                            firestore.collection("User").document(firebaseUser.getUid());
                    df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists()){

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override public void onFailure(@NonNull Exception e) {

                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "Please Login.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, LoginOuterActivity.class);
                    startActivity(intent);
                }
            }
        };


    }
    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}