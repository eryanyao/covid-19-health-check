package com.example.g3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainPageActivity extends AppCompatActivity {
    ImageView imgUser,success_imageview;
    Button btnBar,btnSurvey,btnCovid,btnSettings,btnLogout;
    TextView txtName,txtId,txtRole,txtEmail,success_text;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;

    private String roles;

    private String barcorde;
    private Bitmap myBitmap;
    private String time;

    private int size = 660;
    private int size_width = 660;
    private int size_height = 264;


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

        updateUser();

        if(firebaseUser.getPhotoUrl() != null){
            Glide.with(this)
                    .load(firebaseUser.getPhotoUrl())
                    .into(imgUser);
        }

        btnBar.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                generate();
            }
        });

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

    public void generate(){
        Bitmap bitmap =null;
        try
        {
            bitmap = CreateImage(firebaseAuth.getUid());
            myBitmap = bitmap;
        }
        catch (WriterException we)
        {
            we.printStackTrace();
        }
        if (bitmap != null)
        {
            saveBitmap(myBitmap, barcorde, ".jpg");
            LayoutInflater layoutInflater = LayoutInflater.from(MainPageActivity.this);
            View view = layoutInflater.inflate(R.layout.success_dialog, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(MainPageActivity.this);
            builder.setTitle("Barcode Details");
            builder.setCancelable(false);
            builder.setView(view);
            success_text = (TextView) view.findViewById(R.id.success_text);
            success_text.setText(roles + " ID: "+ barcorde + "\n" + time);
            success_imageview = (ImageView) view.findViewById(R.id.success_imageview);
            success_imageview.setImageBitmap(myBitmap);
            builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // do nothing
                }
            });
            builder.create();
            builder.show();

        }

    }
    public void saveBitmap (Bitmap bitmap, String message, String bitName)
    {
        String[] PERMISSIONS = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE" };
        int permission = ContextCompat.checkSelfPermission(this,
                "android.permission.WRITE_EXTERNAL_STORAGE");
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS,1);
        }

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int millisecond = calendar.get(Calendar.MILLISECOND);

        String fileName = message + "_at_" + String.valueOf(year) + "_" + String.valueOf(month) + "_" + String.valueOf(day) + "_" + String.valueOf(hour) + "_" + String.valueOf(minute) + "_" + String.valueOf(second) + "_"  + String.valueOf(millisecond);
        time = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day) + " " + String.valueOf(hour) + ":" + String.valueOf(minute) + ":" + String.valueOf(second);
        File file;

        String fileLocation;

        String folderLocation;

        if(Build.BRAND.equals("Xiaomi") ){
            fileLocation = Environment.getExternalStorageDirectory().getPath()+"/DCIM/Camera/AndroidBarcodeGenerator/" + fileName + bitName ;
            folderLocation = Environment.getExternalStorageDirectory().getPath()+"/DCIM/Camera/AndroidBarcodeGenerator/";
        }else{
            fileLocation = Environment.getExternalStorageDirectory().getPath()+"/DCIM/AndroidBarcodeGenerator/" + fileName + bitName ;
            folderLocation = Environment.getExternalStorageDirectory().getPath()+"/DCIM/AndroidBarcodeGenerator/";
        }

        Log.d("file_location", fileLocation);

        file = new File(fileLocation);

        File folder = new File(folderLocation);
        if (!folder.exists())
        {
            folder.mkdirs();
        }

        if (file.exists())
        {
            file.delete();
        }


        FileOutputStream out;

        try
        {
            out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out))
            {
                out.flush();
                out.close();
            }
        }
        catch (FileNotFoundException fnfe)
        {
            fnfe.printStackTrace();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }

        this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + fileName)));

    }

    public Bitmap CreateImage(String message) throws WriterException
    {

        BitMatrix bitMatrix =  new MultiFormatWriter().encode(message, BarcodeFormat.CODE_128, size_width, size_height);

        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        int [] pixels = new int [width * height];
        for (int i = 0 ; i < height ; i++)
        {
            for (int j = 0 ; j < width ; j++)
            {
                if (bitMatrix.get(j, i))
                {
                    pixels[i * width + j] = 0xff000000;
                }
                else
                {
                    pixels[i * width + j] = 0xffffffff;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
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

                    String firstName = documentSnapshot.getString("userFirstName");
                    String lastName = documentSnapshot.getString("userLastName");
                    String ic = documentSnapshot.getString("userBarcode");
                    String email = documentSnapshot.getString("userEmail");
                    roles = documentSnapshot.getString("userAffiliation");
                    barcorde = documentSnapshot.getString("userBarcode");

                    txtName.setText(firstName + " " + lastName);
                    txtRole.setText(roles);
                    txtId.setText(ic);
                    txtEmail.setText(email);
                }


            }
        });

    }
}