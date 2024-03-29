package com.github.CulinaryApp.views;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import javax.net.ssl.HttpsURLConnection;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.io.IOException;

import static com.github.CulinaryApp.R.id.btn_signup;
import static com.github.CulinaryApp.R.id.login_button;
import static com.github.CulinaryApp.R.id.reset_button;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
// import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
// import android.widget.ProgressBar;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.github.CulinaryApp.R;
import com.github.CulinaryApp.views.RegistrationActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.FirebaseUser;
import android.content.Intent;
import android.view.View;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;


public class LoginActivity extends AppCompatActivity {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String FILENAME_ENCRYPTED_SHARED_PREFS = "secret_shared_prefs";
    private static final String VALUE_DEFAULT_NONE_FOUND = "{}";

    private FirebaseAuth mAuth;
    private static final String TAG = "Login Activity";

    public void navigateToCategoriesPage() {
            Intent intentToStartCategoriesPage = new Intent(this, CategoriesActivity.class);
            startActivity(intentToStartCategoriesPage);
    }

    public void navigateToRegistrationPage() {
        Intent intentToStartRegistrationPage = new Intent(this, RegistrationActivity.class);
        startActivity(intentToStartRegistrationPage);
    }
    @Override
    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            navigateToCategoriesPage();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.github.CulinaryApp.R.layout.activity_login);

        Log.d(TAG, "LoginActivity: CREATED");
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        EditText email = (findViewById(R.id.email));
        EditText password = (findViewById(R.id.password));
        Button loginButton = (findViewById(login_button));
        Button registerButton = (findViewById(btn_signup));
        Button resetButton = (findViewById(reset_button));


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToRegistrationPage();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String useremail = email.getText().toString();
                String userpassword = password.getText().toString();

                if (TextUtils.isEmpty(useremail)) {
                    Toast.makeText(LoginActivity.this.getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(userpassword)) {
                    Toast.makeText(LoginActivity.this.getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //progressBar.setVisibility(View.VISIBLE);

                //login user
                mAuth.signInWithEmailAndPassword(useremail,userpassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //progressBar.setVisibility(View.GONE);

                                if (!task.isSuccessful()) {

                                    if (userpassword.length() < 6) {
                                        password.setError(LoginActivity.this.getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(LoginActivity.this, LoginActivity.this.getString(R.string.auth_failed), Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Log.d(TAG, "LOGIN SUCCEEDED: REDIRECTED TO CATEGORIES NOW");
                                    //Toast.makeText(LoginActivity.this, "Authentication SUCCESS.", Toast.LENGTH_SHORT).show();
                                  //  LoginActivity.this.startActivity(new Intent(LoginActivity.this, CategoriesActivity.class));
                                   // LoginActivity.this.finish();
                                    navigateToCategoriesPage();
                                }
                            }
                        });

            }
        });

        //Forgot password button
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Please enter your email address");

                final EditText input = new EditText(LoginActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                builder.setView(input);

                //Builds dialog interface
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //"Send email" clicked
                                String emailAddress = input.getText().toString();
                                mAuth.sendPasswordResetEmail(emailAddress)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d(TAG, "Email sent.");
                                                }
                                            }
                                        });
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //"Cancel" clicked
                                break;
                        }

                    }
                };

                builder.setPositiveButton("Send Email", dialogClickListener);
                builder.setNegativeButton("Cancel", dialogClickListener);
                builder.show();

            }
        });


    }



}


