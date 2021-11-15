package com.github.CulinaryApp;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView numInputField = findViewById(R.id.numInput);
        TextView passInputField = findViewById(R.id.passInput);




        Button login = findViewById(R.id.loginButton);
        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                // this is the backend server's  OAuth 2.0 client ID
                .requestIdToken("453772724489-hcig30pfqiddt4af1i8ucu2766kuiikh.apps.googleusercontent.com")
                .requestEmail()
                .build();
        /*       login.setOnClickListener(view -> {

           boolean numRight = numInputField.getText().toString().equals(correctPhone);
           boolean passRight = passInputField.getText().toString().equals(correctPass);

            boolean numRight = true;
            boolean passRight = true;

            if (numRight && passRight)
                output("Login Success", "Welcome, Chef Cocky!");
            else
                output("Login Failed", "Check Phone Number or Password. (Phone: 8438675309, Pass: capstone)");

        });

 */
       
        /*
            if (userExists(numInputField,passInputField))
                navigateToCategoriesPage();
         */
    }
    /*
    private boolean userExists(TextView number, TextView password) {
        
        // if (user account is authenticated by firebase instance
        return true;
       
    }

    public void navigateToCategoriesPage() {
        Intent intentToStartCategoriesPage = new Intent(this, CategoriesActivity.class);
        startActivity(intentToStartCategoriesPage);
    }
    private void output(String title, String msg){
        AlertDialog.Builder alertBox = new AlertDialog.Builder(this);
        alertBox.setTitle(title);
        alertBox.setMessage(msg);

        alertBox.setPositiveButton("Ok", null);

        alertBox.show();
    }
    */

}
