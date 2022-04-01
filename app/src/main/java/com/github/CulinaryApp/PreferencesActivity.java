package com.github.CulinaryApp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PreferencesActivity extends AppCompatActivity implements View.OnClickListener {
    private CheckBox athleticLifestyle;
    private CheckBox veganLifestyle;
    private CheckBox vegetLifestyle;
    private CheckBox mediLifestyle;
    private CheckBox ketoLifestyle;
    private CheckBox flexiLifestyle;
    private Button confirmButton;
    private Button cancelButton;

    @Override
    protected void OnCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        athleticLifestyle = (CheckBox)findViewById(R.id.athleticLifestyle);
        veganLifestyle = (CheckBox)findViewById(R.id.veganLifestyle);
        vegetLifestyle = (CheckBox)findViewById(R.id.vegetarianLifestyle);
        mediLifestyle = (CheckBox)findViewById(R.id.mediDietLifestyle);
        ketoLifestyle = (CheckBox)findViewById(R.id.ketoDietLifestyle);
        flexiLifestyle = (CheckBox)findViewById(R.id.flexDietLifestyle);

        confirmButton = (Button)findViewById(R.id.confirm_button);
        cancelButton = (Button)findViewById(R.id.cancel_button);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.confirm_button:
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference("Users");
                DatabaseReference hopperRef = ref.child(FirebaseAuth.getInstance().getUid());

                //Create hashmap to store data in until upload
                Map<String, Object> hopperUpdates = new HashMap<>();
                //Store diet/lifestyle info
                ArrayList<String> lifestlyeList = new ArrayList<String>();
                if(athleticLifestyle.isChecked())
                    lifestlyeList.add("Athletic");
                if(veganLifestyle.isChecked())
                    lifestlyeList.add("Vegan");
                if(vegetLifestyle.isChecked())
                    lifestlyeList.add("Vegetarian");
                if(mediLifestyle.isChecked())
                    lifestlyeList.add("Mediterranean");
                if(ketoLifestyle.isChecked())
                    lifestlyeList.add("Ketogenic");
                if(flexiLifestyle.isChecked())
                    lifestlyeList.add("Flexitarian");

                if(!lifestlyeList.isEmpty())
                    hopperUpdates.put("Lifestyle",lifestlyeList);

                hopperRef.updateChildren(hopperUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(PreferencesActivity.this, "Preferences successfully updated", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(PreferencesActivity.this, ProfileActivity.class));
                        }
                        else
                            Toast.makeText(PreferencesActivity.this, "Failed to update user info", Toast.LENGTH_LONG).show();
                    }
                });
                break;

            case R.id.cancel_button:
                startActivity(new Intent(PreferencesActivity.this, ProfileActivity.class));
                break;
        }

    }
}

