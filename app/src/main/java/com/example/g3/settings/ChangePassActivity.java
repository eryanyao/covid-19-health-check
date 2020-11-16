package com.example.g3.settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.g3.MainPageActivity;
import com.example.g3.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassActivity extends AppCompatActivity {
    Button btnChange;
    EditText txtCurrPass,txtNewPass,txtConfirmPass;
    FirebaseAuth auth;
    FirebaseUser user;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        getSupportActionBar().setTitle("Change Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        pd = new ProgressDialog(this);

        btnChange = findViewById(R.id.btnChangePass);

        txtCurrPass = findViewById(R.id.txtCurrentPassword);
        txtNewPass = findViewById(R.id.txtPassword);
        txtConfirmPass = findViewById(R.id.txtConfirmPassword);

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                final String txtNew = txtNewPass.getText().toString();
                String txtConfirm = txtConfirmPass.getText().toString();
                final String txtCurrent = txtCurrPass.getText().toString();

                if(txtNew.isEmpty() || txtConfirm.isEmpty() || txtCurrent.isEmpty()) {
                    Toast.makeText(ChangePassActivity.this,
                            "Please enter all field.",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    if (!txtNew.equals(txtConfirm)) {
                        txtConfirmPass.setError("Password must be same.");
                        txtNewPass.setError("Password must be same.");
                    } else if (txtConfirmPass.getText().length() < 8) {
                        txtConfirmPass.setError("The password lengths must be more than 8.");
                        txtNewPass.setError("The password lengths must be more than 8.");
                    } else if (txtNewPass.getText().length() < 8) {
                        txtConfirmPass.setError("The password lengths must be more than 8.");
                        txtNewPass.setError("The password lengths must be more than 8.");
                    } else {
                        if (user != null || user.getEmail() != null) {
                            AuthCredential credential = EmailAuthProvider
                                    .getCredential(user.getEmail(), txtCurrent);
                            pd.setTitle("Changing Password");
                            pd.setMessage("wait a moment...");
                            pd.show();
                            Task<Void> voidTask = user.reauthenticate(credential)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(ChangePassActivity.this,
                                                        "Re-Authentication " +
                                                                "Successfully.",
                                                        Toast.LENGTH_SHORT).show();
                                                user.updatePassword(txtNew).addOnCompleteListener(
                                                        new OnCompleteListener<Void>() {
                                                            @Override public void onComplete(
                                                                    @NonNull Task<Void> task) {
                                                                Toast.makeText(
                                                                        ChangePassActivity.this,
                                                                        "Password Update " +
                                                                                "Successfully.",
                                                                        Toast.LENGTH_SHORT).show();
                                                                pd.dismiss();
                                                                back();
                                                            }
                                                        }).addOnFailureListener(
                                                        new OnFailureListener() {
                                                            @Override public void onFailure(
                                                                    @NonNull Exception e) {
                                                                Toast.makeText(
                                                                        ChangePassActivity.this,
                                                                        "Password Update " +
                                                                                "Unsuccessfully. " +
                                                                                "Please try again.",
                                                                        Toast.LENGTH_SHORT).show();
                                                                pd.dismiss();
                                                            }
                                                        });
                                            } else {
                                                Toast.makeText(ChangePassActivity.this,
                                                        "Re-Authentication " +
                                                                "Unsuccessfully. Current Password" +
                                                                " error.",
                                                        Toast.LENGTH_SHORT).show();
                                                pd.dismiss();
                                                txtCurrPass.setError("Password Wrong.");
                                            }
                                        }
                                    });

                        }

                    }
                }
            }
        });
    }

    public void back(){
        Intent intent = new Intent(ChangePassActivity.this, MainPageActivity.class);
        startActivity(intent);
    }
}