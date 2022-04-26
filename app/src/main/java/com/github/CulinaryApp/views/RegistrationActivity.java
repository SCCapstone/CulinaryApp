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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


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

        nButton = findViewById(R.id.reg1NButton);
        emailAddress = findViewById(R.id.EmailAddress);
        password = findViewById(R.id.Password);
        confPassword = findViewById(R.id.ConfPassword);
        progBar = findViewById(R.id.progressBar);
        progBar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reg1NButton:

                String email = emailAddress.getText().toString().trim();
                String pass = password.getText().toString().trim();

                //If email address doesn't match standard email format throw error
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailAddress.setError("Please enter a valid email address!");
                    emailAddress.requestFocus();
                    return;
                }

                //Firebase doesn't accept passwords less than 6 characters
                else if (pass.length() < 6) {
                    password.setError("Password must be at least 6 characters long!");
                    password.requestFocus();
                    return;
                }

                //If any field is empty throw error
                else if (email.isEmpty() || pass.isEmpty()
                        || confPassword.getText().toString().isEmpty()) {
                    Toast.makeText(RegistrationActivity.this, "One or more fields is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Check to make sure both password fields match
                else if (!pass.matches(confPassword.getText().toString())) {
                    Toast.makeText(RegistrationActivity.this, "Please make sure both passwords match", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    progBar.setVisibility(View.VISIBLE);

                    mAuth.createUserWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {

                                        User user = new User(email);

                                        FirebaseDatabase.getInstance().getReference("Users")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    //Toast.makeText(RegistrationActivity.this, "User successfully registered", Toast.LENGTH_LONG).show();
                                                    progBar.setVisibility(View.GONE);

                                                    //Set default user values
                                                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                    DatabaseReference ref = database.getReference("Users");
                                                    DatabaseReference hopperRef = ref.child(FirebaseAuth.getInstance().getUid());
                                                    Map<String, Object> hopperUpdates = new HashMap<>();
                                                    hopperUpdates.put("First Name", "None");
                                                    hopperUpdates.put("Last Name", "Entered");
                                                    hopperUpdates.put("isChef", false);
                                                    ArrayList<String> lifestyleList = new ArrayList<String>();
                                                    lifestyleList.add("None");
                                                    hopperUpdates.put("Lifestyle", lifestyleList);

                                                    //Upload default values and take to second page if successful
                                                    hopperRef.updateChildren(hopperUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                //Toast.makeText(RegistrationActivity.this, "User info successfully updated", Toast.LENGTH_LONG).show();
                                                                //startActivity(new Intent(RegistrationActivity.this, CategoriesActivity.class));
                                                                startActivity(new Intent(RegistrationActivity.this, RegPage2Activity.class));
                                                            } else
                                                                Toast.makeText(RegistrationActivity.this, "Failed to update user info", Toast.LENGTH_LONG).show();
                                                        }
                                                    });


                                                } else {
                                                    Toast.makeText(RegistrationActivity.this, "User failed to register", Toast.LENGTH_LONG).show();
                                                    progBar.setVisibility(View.GONE);
                                                }
                                            }
                                        });
                                        //Toast.makeText(RegistrationActivity.this, "User successfully registered", Toast.LENGTH_LONG).show();
                                        progBar.setVisibility(View.GONE);
                                    } else {
                                        Toast.makeText(RegistrationActivity.this, "User failed to register", Toast.LENGTH_LONG).show();
                                        progBar.setVisibility(View.GONE);
                                    }
                                }
                            });

                    progBar.setVisibility(View.GONE);

                }
                break;
            /*
            case R.id.chefRegClick:
                startActivity(new Intent(RegistrationActivity.this, ChefRegistration.class));
                break;
             */
        }
    }

    public static boolean emailValidator(String email) {
        boolean value = PatternsCompat.EMAIL_ADDRESS.matcher(email.trim()).matches();
        return value;
    }

    public static boolean passwordsMatch(String pass1, String pass2){
        return pass1.equals(pass2);
    }
}