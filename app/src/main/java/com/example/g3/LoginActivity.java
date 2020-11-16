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
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText txtStudent, txtPassword;
    Button btnForgot,btnLogin;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        pd = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (hasValidAuthToken()) {
            Intent intent = new Intent(LoginActivity.this,MainPageActivity.class);
            startActivity(intent);
        }

        txtPassword = findViewById(R.id.txtPassword);
        txtStudent = findViewById(R.id.txtStudent);
        btnForgot = findViewById(R.id.btnForgot);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                String student = txtStudent.getText().toString();
                String password = txtPassword.getText().toString();

                if(student.isEmpty() && password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "All fields are required",
                            Toast.LENGTH_SHORT).show();
                    txtPassword.setError("Field required");
                    txtStudent.setError("Field required");
                }
                else if(student.isEmpty()){
                    txtStudent.setError("Field required");
                }
                else if(password.isEmpty()){
                    txtPassword.setError("Field required");
                }
                else if(!(student.isEmpty() && password.isEmpty())){
                    String email = txtStudent.getText() + "@student.newinti.edu.my";

                    pd.setTitle("Please wait");
                    pd.setMessage("Login to the system...");
                    pd.show();

                    firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(
                            LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(LoginActivity.this,
                                                "Login successfully. Welcome, " + student,
                                                Toast.LENGTH_SHORT).show();
                                        pd.dismiss();

                                    }
                                    else{
                                        Toast.makeText(LoginActivity.this, "Login unsuccessfully" +
                                                        "\n" +
                                                "   Please try again", Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                    }
                                }
                            });
                }
                else{
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

    public static boolean hasValidAuthToken() {
        return FirebaseAuth.getInstance().getCurrentUser() != null ? true : false;
    }

    public void forgot(){
        Intent intent = new Intent(LoginActivity.this,ResetPasswordActivity.class);
        startActivity(intent);
    }

    public void login(){
        Intent intent = new Intent(LoginActivity.this, MainPageActivity.class);
        startActivity(intent);
    }
}