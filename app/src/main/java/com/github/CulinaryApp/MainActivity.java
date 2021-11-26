package com.github.CulinaryApp;

import android.os.Bundle;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView usersEmail = findViewById(R.id.usersEmail);
        TextView usersPassword = findViewById(R.id.usersPassword);

        // Configure sign-in to request user's ID & email address
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                // this is the backend server's  OAuth 2.0 client ID
                .requestIdToken("453772724489-hcig30pfqiddt4af1i8ucu2766kuiikh.apps.googleusercontent.com")
                .requestEmail()
                .build();

        // init GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // init Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    public void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        // check for existing google sign in account, if the user is already signed in,
        // the GoogleSignInAccount will be non-null
        // transition them to categories screen if non-null

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        //   startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            // ...
        }
    }
}