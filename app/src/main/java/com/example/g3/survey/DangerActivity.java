package com.example.g3.survey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.g3.MainPageActivity;
import com.example.g3.R;

public class DangerActivity extends AppCompatActivity {
    Button btnHome,btnHelp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_danger);

        btnHome = findViewById(R.id.btnDangerHome);
        btnHelp = findViewById(R.id.btnDangerHelp);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                home();
            }
        });

        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                String phno="+60388810200";
                Intent callIntent = new Intent(Intent.ACTION_DIAL,Uri.parse(phno));

                startActivity(callIntent);
            }
        });
    }

    public void home(){
        Intent intent = new Intent(this, MainPageActivity.class);
        startActivity(intent);
    }
}