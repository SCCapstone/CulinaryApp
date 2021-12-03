package com.github.CulinaryApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class ChefRegistration extends AppCompatActivity implements View.OnClickListener{

    private EditText chefEmailT;
    private EditText passT;
    private EditText chefConfPassT;
    private EditText chefFirstT;
    private EditText chefLastT;
    private EditText chefAffiliationT;
    private Button chefRegB;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_registration);

        mAuth = FirebaseAuth.getInstance();

        chefEmailT = (EditText)findViewById(R.id.chefEmail);
        chefConfPassT = (EditText)findViewById(R.id.chefConfPass);
        chefFirstT = (EditText)findViewById(R.id.chefFirstName);
        chefLastT = (EditText)findViewById(R.id.chefLastName);
        chefAffiliationT = (EditText)findViewById(R.id.chefAffiliation);
        passT = (EditText)findViewById(R.id.chefPass);
        chefConfPassT = (EditText)findViewById(R.id.chefConfPass);
        chefRegB = (Button)findViewById(R.id.chefReg);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.chefReg:

                String email = chefEmailT.getText().toString().trim();
                String firstN = chefFirstT.getText().toString().trim();
                String lastN = chefLastT.getText().toString().trim();
                String affiliation = chefAffiliationT.getText().toString().trim();
                String pass = passT.getText().toString().trim();
                String confPass = chefConfPassT.getText().toString().trim();

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    chefEmailT.setError("Please enter a valid email address!");
                    chefEmailT.requestFocus();
                    return;
                }
                else if(email.isEmpty()){
                    chefEmailT.setError("Field cannot be empty!");
                    chefEmailT.requestFocus();
                    return;
                }
                else if(firstN.isEmpty()){
                    chefFirstT.setError("Field cannot be empty!");
                    chefFirstT.requestFocus();
                    return;
                }
                else if(lastN.isEmpty()){
                    chefLastT.setError("Field cannot be empty!");
                    chefLastT.requestFocus();
                    return;
                }
                else if(affiliation.isEmpty()) {
                    chefAffiliationT.setError("Field cannot be empty!");
                    chefAffiliationT.requestFocus();
                    return;
                }
                else if(pass.isEmpty()) {
                    passT.setError("Field cannot be empty!");
                    passT.requestFocus();
                    return;
                }
                else if(pass.length() < 6) {
                    passT.setError("Password must be at least 6 characters!");
                    passT.requestFocus();
                    return;
                }
                else if(!pass.matches(confPass)) {
                    chefConfPassT.setError("Passwords must match!");
                    chefConfPassT.requestFocus();
                    return;
                }
                else{
                    mAuth.createUserWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){

                                        Chef chef = new Chef(email, firstN, lastN, affiliation);

                                        FirebaseDatabase.getInstance().getReference("Chefs")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(chef).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(ChefRegistration.this, "Chef info uploaded", Toast.LENGTH_LONG).show();

                                                    //TODO Send chef to profile page or categories page (or reg page 2 but that's given)
                                                }
                                                else{
                                                    Toast.makeText(ChefRegistration.this, "Chef info failed to upload", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                        Toast.makeText(ChefRegistration.this, "Chef successfully registered", Toast.LENGTH_LONG).show();

                                    }
                                    else{
                                        Toast.makeText(ChefRegistration.this, "Chef failed to register", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }

                break;
        }
    }
}