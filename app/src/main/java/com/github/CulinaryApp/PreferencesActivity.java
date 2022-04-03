package com.github.CulinaryApp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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
    protected void onCreate(Bundle savedInstanceState){
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
    protected void onStart(){
        super.onStart();

        //Remember user preferences and have those boxes already marked
        final ArrayList<String>[] lifeStyles = new ArrayList[]{new ArrayList<>()};

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Users");
        dbRef.child(FirebaseAuth.getInstance().getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("Data: ", snapshot.getKey() + ", " + snapshot.getValue());
                //Get lifestyle preferences
                if (snapshot.getKey().equals("Lifestyle")) {
                    lifeStyles[0] = (ArrayList<String>) snapshot.getValue();
                    for(String lifestyle : lifeStyles[0]){
                        switch(lifestyle){
                            case "Athletic":
                                athleticLifestyle.setChecked(true);
                                break;
                            case "Vegan":
                                veganLifestyle.setChecked(true);
                                break;
                            case "Vegetarian":
                                vegetLifestyle.setChecked(true);
                                break;
                            case "Mediterranean":
                                mediLifestyle.setChecked(true);
                                break;
                            case "Ketogenic":
                                ketoLifestyle.setChecked(true);
                                break;
                            case "Flexitarian":
                                flexiLifestyle.setChecked(true);
                                break;
                        }
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
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
                ArrayList<String> lifestyleList = new ArrayList<String>();
                if(athleticLifestyle.isChecked())
                    lifestyleList.add("Athletic");
                if(veganLifestyle.isChecked())
                    lifestyleList.add("Vegan");
                if(vegetLifestyle.isChecked())
                    lifestyleList.add("Vegetarian");
                if(mediLifestyle.isChecked())
                    lifestyleList.add("Mediterranean");
                if(ketoLifestyle.isChecked())
                    lifestyleList.add("Ketogenic");
                if(flexiLifestyle.isChecked())
                    lifestyleList.add("Flexitarian");


                if(lifestyleList.isEmpty()) {
                    lifestyleList.add("None");
                }
                hopperUpdates.put("Lifestyle", lifestyleList);


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

