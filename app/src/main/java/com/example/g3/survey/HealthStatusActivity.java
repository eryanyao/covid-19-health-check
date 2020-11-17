package com.example.g3.survey;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.g3.MainPageActivity;
import com.example.g3.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class HealthStatusActivity extends AppCompatActivity {
    CheckBox cbTemperature, cbContinuousCough, cbTaste;
    Button btnConfirm, btnDont;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_survey_activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        cbTemperature = findViewById(R.id.checkBoxTemperature);
        cbContinuousCough = findViewById(R.id.checkBoxContinuousCough);
        cbTaste = findViewById(R.id.checkBoxTaste);
        btnConfirm = findViewById(R.id.btnContinues);
        btnDont = findViewById(R.id.btnDontHave);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(cbContinuousCough.isChecked() || cbTaste.isChecked() || cbTemperature.isChecked())) {
                    Toast.makeText(HealthStatusActivity.this, "Please select symptom(s) " +
                            "if any. Otherwise, click the button at the bottom" +
                            ".", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(HealthStatusActivity.this);
                    builder.setTitle("Confirm the symptom(s) you have selected");
                    builder.setMessage("Any selected symptom(s) will be saved.");
                    builder.setPositiveButton(
                            "Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    builder.setNegativeButton(
                            "Confirm",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    final ProgressDialog pd =
                                            new ProgressDialog(HealthStatusActivity.this);
                                    pd.setTitle("Update Status");
                                    pd.setMessage("Please wait while saving your data");
                                    pd.show();
                                    firestore.collection("user").document(firebaseAuth.getCurrentUser().getUid())
                                            .update("userStatus", "Danger").addOnCompleteListener(
                                            new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(HealthStatusActivity.this, "Health " +
                                                            "status has updated " +
                                                            "successfully", Toast.LENGTH_SHORT).show();
                                                    pd.dismiss();
                                                    danger();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(HealthStatusActivity.this, "Health " +
                                                            "status update " +
                                                            "unsuccessfully.\nPlease try again",
                                                    Toast.LENGTH_SHORT).show();
                                            pd.dismiss();
                                        }
                                    });
                                }
                            });
                    builder.create().show();
                }
            }
        });

        btnDont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HealthStatusActivity.this);
                builder.setTitle("Confirm you do not have any symptoms");
                builder.setMessage("Any selected symptoms will be unselected.");
                builder.setPositiveButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                builder.setNegativeButton(
                        "Confirm",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                final ProgressDialog pd =
                                        new ProgressDialog(HealthStatusActivity.this);
                                pd.setTitle("Update Status");
                                pd.setMessage("Please wait while saving your data");
                                pd.show();

                                firestore.collection("user").document(firebaseAuth.getCurrentUser().getUid())
                                        .update("userStatus", "Safe").addOnCompleteListener(
                                        new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(HealthStatusActivity.this, "Health " +
                                                        "status update " +
                                                        "successfully.", Toast.LENGTH_SHORT).show();
                                                pd.dismiss();
                                                safe();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(HealthStatusActivity.this, "Health " +
                                                        "status update " +
                                                        "unsuccessfully.\nPlease try again",
                                                Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                    }
                                });
                            }
                        });
                builder.create().show();
            }
        });
    }


    public void danger() {
        Intent intent = new Intent(this, DangerActivity.class);
        startActivity(intent);
    }

    public void safe() {
        Intent intent = new Intent(this, SafeActivity.class);
        startActivity(intent);
    }
}

