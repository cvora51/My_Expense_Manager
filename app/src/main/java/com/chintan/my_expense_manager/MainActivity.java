package com.chintan.my_expense_manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText mEmail,mPass;
    private Button btn_login;
    private TextView txt_forgot_pass, txt_signup;
    private ProgressDialog dialog;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                if(TextUtils.isEmpty(pass)){
                    mPass.setError("Password Required!");
                    return;
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
        moveTaskToBack(true);
        finish();
    }
}