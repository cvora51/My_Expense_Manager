package com.chintan.my_expense_manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText mEmail,mPass;
    private Button btn_login;
    private TextView txt_forgot_pass, txt_signup, loca;
    private ProgressDialog dialog;
    private FirebaseAuth mAuth;
    FusedLocationProviderClient client;
    LocationRequest request;
    LocationCallback callback;
    Location location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        client = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        request = new LocationRequest();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(4000);
        request.setFastestInterval(2000);
        callback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                location = locationResult.getLocations().get(0);
                String Loc1 = "location:" + " , " + location.getLatitude() + " , " + location.getLongitude();
                loca = findViewById(R.id.location1);
                loca.setText(Loc1);
                convertToAddress(location);
                super.onLocationResult(locationResult);
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        client.requestLocationUpdates(request, callback, Looper.myLooper());

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        }

        dialog = new ProgressDialog(this);

        loginDetails();

    }

    private void loginDetails() {
        mEmail = findViewById(R.id.email_login);
        mPass = findViewById(R.id.password_login);
        btn_login = findViewById(R.id.btn_login);
        txt_forgot_pass = findViewById(R.id.forgot_password);
        txt_signup = findViewById(R.id.signup);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String pass = mPass.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email Required!");
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    mEmail.setError("Please enter a valid email");
                }
                if(TextUtils.isEmpty(pass)){
                    mPass.setError("Password Required!");
                    return;
                }
                if(pass.length()<8){
                    mPass.setError("Minimum 8 characters required");
                }

                dialog.setMessage("Processing..");
                dialog.show();

                mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            dialog.dismiss();
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                            Toast.makeText(getApplicationContext(), "Logged in Successfully!", Toast.LENGTH_SHORT).show();
                        }else{
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Oops!Login Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        txt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),RegistrationActivity.class));

            }
        });

        txt_forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ResetPasswordActivity.class));
            }
        });
    }

//    @Override
//    public void onBackPressed() {
//        MainActivity.super.onBackPressed();
//    }

        @Override
    public void onBackPressed() {

        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Warning")
                .setMessage("Do you Wish to exit?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("NO",null)
                .show();
//        moveTaskToBack(true);
//        finish();
    }

    private void convertToAddress(Location location) {
        Geocoder geocoder = new Geocoder(getApplicationContext());
        List<Address> lAds = null;
        try {
            lAds = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            Address ad = lAds.get(0);

            String Add_1 = "Connected From: " + ad.getSubLocality();
            loca.setText(Add_1);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}