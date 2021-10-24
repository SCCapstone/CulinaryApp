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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView numInputField = findViewById(R.id.numInput);
        TextView passInputField = findViewById(R.id.passInput);

        String correctPhone = "8438675309";
        String correctPass = "capstone";

        Button login = findViewById(R.id.loginButton);
        login.setOnClickListener(view -> {
            boolean numRight = numInputField.getText().toString().equals(correctPhone);
            boolean passRight = passInputField.getText().toString().equals(correctPass);

            if (numRight && passRight)
                output("Login Success", "Welcome, Chef Cocky!");
            else
                output("Login Failed", "Check Phone Number or Password. (Phone: 8438675309, Pass: capstone)");

        });
    }

    private void output(String title, String msg){
        AlertDialog.Builder alertBox = new AlertDialog.Builder(this);
        alertBox.setTitle(title);
        alertBox.setMessage(msg);

        alertBox.setPositiveButton("Ok", null);

        alertBox.show();
    }
}