package com.example.g3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginOuterActivity extends AppCompatActivity {
    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_outer);

        btnLogin = findViewById(R.id.btnThroughLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                login();
            }
        });

    }

    public void login(){
        Intent intent = new Intent(LoginOuterActivity.this,LoginActivity.class);
        startActivity(intent);
    }
}