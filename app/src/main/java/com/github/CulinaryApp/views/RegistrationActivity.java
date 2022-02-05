// Used this tutorial to attempt to get the firebase authentication working: https://www.youtube.com/watch?v=Z-RE1QuUWPg&t=347s

package com.github.CulinaryApp.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.PatternsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.CulinaryApp.R;
import com.github.CulinaryApp.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private Button nButton;
    private EditText emailAddress;
    private EditText password;
    private EditText confPassword;
    private ProgressBar progBar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        nButton = (Button)findViewById(R.id.reg1NButton);
        emailAddress = (EditText)findViewById(R.id.EmailAddress);
        password = (EditText)findViewById(R.id.Password);
        confPassword = (EditText)findViewById(R.id.ConfPassword);
        progBar = (ProgressBar)findViewById(R.id.progressBar);
        progBar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reg1NButton:

                String email = emailAddress.getText().toString().trim();
                String pass = password.getText().toString().trim();

                //If email address doesn't match standard email format throw error
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emailAddress.setError("Please enter a valid email address!");
                    emailAddress.requestFocus();
                    return;
                }

                //Firebase doesn't accept passwords less than 6 characters
                else if(pass.length() < 6){
                    password.setError("Password must be at least 6 characters long!");
                    password.requestFocus();
                    return;
                }

                //If any field is empty throw error
                else if(email.isEmpty()|| pass.isEmpty()
                        || confPassword.getText().toString().isEmpty())
                {
                    Toast.makeText(RegistrationActivity.this, "One or more fields is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Check to make sure both password fields match
                else if(!pass.matches(confPassword.getText().toString())){
                    Toast.makeText(RegistrationActivity.this, "Please make sure both passwords match", Toast.LENGTH_SHORT).show();
                    return;
                }

                //TODO Check to make sure email is unique

                //TODO Currently adds user to firebase right away to testing, need to change it to either
                //TODO only once second page completed or to update once second page completed
                else{
                    progBar.setVisibility(View.VISIBLE);

                    mAuth.createUserWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){

                                        User user = new User(email);

                                        FirebaseDatabase.getInstance().getReference("Users")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                               if(task.isSuccessful()){
                                                    Toast.makeText(RegistrationActivity.this, "User successfully registered", Toast.LENGTH_LONG).show();
                                                    progBar.setVisibility(View.GONE);

                                                    //TODO Send user to profile page or categories page (or reg page 2 but that's given)
                                               }
                                               else{
                                                   Toast.makeText(RegistrationActivity.this, "User failed to register", Toast.LENGTH_LONG).show();
                                                   progBar.setVisibility(View.GONE);
                                               }
                                            }
                                        });
                                        Toast.makeText(RegistrationActivity.this, "User successfully registered", Toast.LENGTH_LONG).show();
                                        progBar.setVisibility(View.GONE);
                                    }
                                    else{
                                        Toast.makeText(RegistrationActivity.this, "User failed to register", Toast.LENGTH_LONG).show();
                                        progBar.setVisibility(View.GONE);
                                    }
                                }
                            });

                    progBar.setVisibility(View.GONE);
                    startActivity(new Intent(RegistrationActivity.this, RegPage2Activity.class));
                    //setContentView(R.layout.activity_reg_page2);
                }
                break;

            case R.id.chefRegClick:
                startActivity(new Intent(RegistrationActivity.this, ChefRegistration.class));
                break;
        }
    }

    public static boolean emailValidator(String email){
        boolean value = PatternsCompat.EMAIL_ADDRESS.matcher(email.trim()).matches();
        return value;
    }
}