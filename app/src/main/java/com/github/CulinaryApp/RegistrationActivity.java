package com.github.CulinaryApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private Button nButton;
    private EditText emailAddress;
    private EditText password;
    private EditText confPassword;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        nButton = (Button)findViewById(R.id.button);
        emailAddress = (EditText)findViewById(R.id.EmailAddress);
        password = (EditText)findViewById(R.id.Password);
        confPassword = (EditText)findViewById(R.id.ConfPassword);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:

                //If email address doesn't match standard email format throw error
                if(Patterns.EMAIL_ADDRESS.matcher(emailAddress.getText().toString().trim()).matches()){
                    emailAddress.setError("Please enter a valid email address!");
                    emailAddress.requestFocus();
                    return;
                }

                //Firebase doesn't accept passwords less than 6 characters
                if(password.getText().toString().trim().length() < 6){
                    password.setError("Password must be at least 6 characters long!");
                    password.requestFocus();
                    return;
                }

                if(emailAddress.getText().toString().isEmpty()||
                        password.getText().toString().isEmpty() ||
                        confPassword.getText().toString().isEmpty())
                {
                    Toast.makeText(RegistrationActivity.this, "One or more fields is empty", Toast.LENGTH_SHORT).show();
                }

                //Check to make sure both password fields match
                else if(!password.getText().toString().matches(confPassword.getText().toString())){
                    Toast.makeText(RegistrationActivity.this, "Please make sure both passwords match", Toast.LENGTH_SHORT).show();
                }

                //TODO Check to make sure email is unique
                else{
                    startActivity(new Intent(RegistrationActivity.this, RegPage2Activity.class));
                    //setContentView(R.layout.activity_reg_page2);
                }
                break;
        }
    }
}