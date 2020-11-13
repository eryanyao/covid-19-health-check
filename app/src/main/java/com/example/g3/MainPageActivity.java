package com.example.g3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.g3.covid.CovidActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainPageActivity extends AppCompatActivity {
    ImageView imgUser;
    Button btnBar,btnSurvey,btnCovid,btnSettings,btnLogout;
    TextView txtName,txtId,txtRole,txtEmail;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        txtEmail = findViewById(R.id.txtUserEmail);
        txtName = findViewById(R.id.txtUserName);
        txtId = findViewById(R.id.txtUserId);
        txtRole = findViewById(R.id.txtUserRoles);
        imgUser = findViewById(R.id.imgUsr);
        btnBar = findViewById(R.id.btnUserBar);
        btnLogout = findViewById(R.id.btnLogout);
        btnSettings = findViewById(R.id.btnSettings);
        btnCovid = findViewById(R.id.btnCovid);
        btnSurvey = findViewById(R.id.btnSurvey);

        Uri profile = firebaseUser.getPhotoUrl();
        imgUser.setImageURI(profile);
        imgUser.getCropToPadding();

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                settings();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                signOut();
            }
        });

        btnCovid.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                covid();
            }
        });

        btnSurvey.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {

            }
        });

        updateUser();

    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ddl_menu, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                settings();
                return true;
            case R.id.action_logout:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void signOut(){
        firebaseAuth.signOut();
        Toast.makeText(MainPageActivity.this, "Logout Successfully.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void settings(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void covid(){
        Intent intent = new Intent(this, CovidActivity.class);
        startActivity(intent);
    }

    public void updateUser(){
        DocumentReference df = firestore.collection("user").document(firebaseUser.getUid());
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String roles = documentSnapshot.getString("userAffiliation");
                    String firstName = documentSnapshot.getString("userFirstName");
                    String lastName = documentSnapshot.getString("userLastName");
                    String ic = documentSnapshot.getString("userBarcode");
                    String email = documentSnapshot.getString("userEmail");

                    txtName.setText(firstName + " " + lastName);
                    txtRole.setText(roles);
                    txtId.setText("i" + ic);
                    txtEmail.setText(email);

                }

            }
        });

    }
}