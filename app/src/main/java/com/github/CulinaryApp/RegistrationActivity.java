package com.github.CulinaryApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity {

    Button nButton;
    EditText emailAddress;
    EditText password;
    EditText confPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        nButton = (Button)findViewById(R.id.button);
        emailAddress = (EditText)findViewById(R.id.EmailAddress);
        password = (EditText)findViewById(R.id.Password);
        confPassword = (EditText)findViewById(R.id.ConfPassword);

        nButton.setOnClickListener(
            new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    //Check to see if any field is blank, display error message if so
                    if(emailAddress.getText().toString().matches("") ||
                    password.getText().toString().matches("") ||
                    confPassword.getText().toString().matches(""))
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
                        setContentView(R.layout.activity_reg_page2);
                    }

                }
            });
    }
}