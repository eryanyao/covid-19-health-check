package com.example.g3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {
    EditText txtStudent;
    Button btnReset,btnBack;
    FirebaseAuth firebaseAuth;

    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        setTitle("Reset Password");
        pd = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();

        btnReset = findViewById(R.id.btnReset);
        btnBack = findViewById(R.id.btnBack);
        txtStudent = findViewById(R.id.txtResetStudent);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                String student = txtStudent.getText().toString().trim();
                if(student.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please enter your student ID.",
                            Toast.LENGTH_SHORT).show();
                    txtStudent.setError("Field Required.");

                }
                else{
                    String email = txtStudent.getText() + "@student.newinti.edu.my";

                    pd.setTitle("Sending reset password email");
                    pd.setMessage("wait a moment...");
                    pd.show();

                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(
                            new OnCompleteListener<Void>() {
                                @Override public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        pd.dismiss();
                                        Toast.makeText(ResetPasswordActivity.this, "The reset " +
                                                "password email already sent. Please check your " +
                                                "email inbox", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        pd.dismiss();
                                        Toast.makeText(ResetPasswordActivity.this,"Failed to send" +
                                                        " the reset password email. Please try  " +
                                                        "later.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    pd.dismiss();
                                }
                            });
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                login();
            }
        });

    }

    public void login(){
        Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}