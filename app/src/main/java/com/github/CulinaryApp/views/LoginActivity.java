package com.github.CulinaryApp.views;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import javax.net.ssl.HttpsURLConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.io.IOException;

import static com.github.CulinaryApp.R.id.btn_signup;
import static com.github.CulinaryApp.R.id.login_button;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
// import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
// import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.github.CulinaryApp.R;
import com.github.CulinaryApp.views.RegistrationActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
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


        /** RJ TEST CODE **/

        new Thread(this::apiCall).start();

        /****************************************/


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
                                    Toast.makeText(LoginActivity.this, "Authentication SUCCESS.", Toast.LENGTH_SHORT).show();
                                  //  LoginActivity.this.startActivity(new Intent(LoginActivity.this, CategoriesActivity.class));
                                   // LoginActivity.this.finish();
                                    navigateToCategoriesPage();
                                }
                            }
                        });

            }
        });


    }

    /** More test code **/

    private void apiCall(){
        //https://www.themealdb.com/api/json/v1/1/lookup.php?i=52772
        final String URL_FIND_BY_ID = "https://www.themealdb.com/api/json/v1/1/search.php?s=Arr";

        try {
            HttpsURLConnection connect = (HttpsURLConnection) new URL(URL_FIND_BY_ID).openConnection();
            InputStream response = connect.getInputStream();

            String stream = streamToString(response);

            Log.d("TEST VALUES",stream);

            Log.d("RECIPE NAMES",JSONToArray(stream).toString());


        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }

    private String streamToString(InputStream response) {
        String JSON = "{}";
        try (Scanner scanner = new Scanner(response, StandardCharsets.UTF_8.name())) {
            JSON = scanner.useDelimiter("\\A").next();
        }

        return JSON;
    }

    private List<String> JSONToArray(String JSON) throws JSONException {
        JSONObject obj = new JSONObject(JSON);

        List<String> list = new ArrayList<String>();
        JSONArray array = obj.getJSONArray("meals");
        for(int i = 0 ; i < array.length() ; i++){
            list.add(array.getJSONObject(i).getString("strMeal"));
        }

        return list;
    }

    /** END OF TEST CODE **/
}


