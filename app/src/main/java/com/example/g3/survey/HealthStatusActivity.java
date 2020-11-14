package com.example.g3.survey;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.g3.R;

import androidx.appcompat.app.AppCompatActivity;

public class HealthStatusActivity  extends AppCompatActivity {
    CheckBox cbTemperature,cbContinuousCough,cbTaste;
    Button btnConfirm, btnDont;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_survey_activity_main);

        cbTemperature = findViewById(R.id.checkBoxTemperature);
        cbContinuousCough = findViewById(R.id.checkBoxContinuousCough);
        cbTaste = findViewById(R.id.checkBoxTaste);

        btnConfirm = findViewById(R.id.btnContinues);
        btnDont = findViewById(R.id.btnDontHave);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if(!(cbContinuousCough.isChecked()&&cbTaste.isChecked()&&cbTemperature.isChecked())){
                    Toast.makeText(HealthStatusActivity.this,"Please select the symptoms. Else " +
                            "click the button " +
                            "below" +
                            ".",Toast.LENGTH_SHORT).show();
                }
                else{
                    setContentView(R.layout.health_danger);
                }
            }
        });

        btnDont.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                
            }
        });


    }
}

