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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText txtStudent, txtPassword;
    Button btnForgot, btnLogin;

    FirebaseAuth firebaseAuth;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        pd = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        txtPassword = findViewById(R.id.txtCurrentPassword);
        txtStudent = findViewById(R.id.txtStudent);
        btnForgot = findViewById(R.id.btnForgot);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                String student = txtStudent.getText().toString();
                String password = txtPassword.getText().toString();

                if (student.isEmpty() && password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "All field must be key in.",
                            Toast.LENGTH_SHORT).show();
                    txtPassword.setError("Field Required.");
                    txtStudent.setError("Field Required.");
                } else if (student.isEmpty()) {
                    txtStudent.setError("Field Required.");
                } else if (password.isEmpty()) {
                    txtPassword.setError("Field Required.");
                } else if (!(student.isEmpty() && password.isEmpty())) {
                    String email = txtStudent.getText() + "@student.newinti.edu.my";

                    pd.setTitle("Login to the system");
                    pd.setMessage("wait a moment...");
                    pd.show();

                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                            LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this,
                                                "Login Successfully.\nWelcome\n" + student,
                                                Toast.LENGTH_SHORT).show();
                                        pd.dismiss();

                                    } else {
                                        Toast.makeText(LoginActivity.this, "Login Unsuccessfully" +
                                                ".\n" +
                                                " Please try again later.", Toast.LENGTH_SHORT)
                                                .show();
                                        pd.dismiss();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(LoginActivity.this, "ERROR OCCURED.", Toast.LENGTH_SHORT).show();
                }
            }

        });

        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                forgot();
            }
        });
    }

    public void forgot() {
        Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
        startActivity(intent);
    }

    public void login() {
        Intent intent = new Intent(LoginActivity.this, MainPageActivity.class);
        startActivity(intent);
    }
}