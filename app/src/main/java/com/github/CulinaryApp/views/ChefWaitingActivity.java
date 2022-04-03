package com.github.CulinaryApp.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.github.CulinaryApp.R;
import com.google.firebase.auth.FirebaseAuth;

public class ChefWaitingActivity extends AppCompatActivity{

    Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_account_waiting);

        logoutButton = findViewById(R.id.logoutButton);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                FirebaseAuth.getInstance().signOut();
                                goToLogin();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }

            };

                AlertDialog.Builder builder = new AlertDialog.Builder(ChefWaitingActivity.this);
                builder.setTitle("Are you sure you'd like to logout?");
                builder.setPositiveButton("Yes",dialogClickListener)
                        .setNegativeButton("No",dialogClickListener).show();

            }

            private void goToLogin() {
                Intent switchToLogin = new Intent(ChefWaitingActivity.this, LoginActivity.class);
                startActivity(switchToLogin);
            }
        });
    }

}
