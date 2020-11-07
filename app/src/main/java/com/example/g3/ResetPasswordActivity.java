package com.example.g3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ResetPasswordActivity extends AppCompatActivity {
    EditText txtStudent;
    Button btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        btnReset = findViewById(R.id.btnReset);
        txtStudent = findViewById(R.id.txtStudent);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {

            }
        });

    }
}