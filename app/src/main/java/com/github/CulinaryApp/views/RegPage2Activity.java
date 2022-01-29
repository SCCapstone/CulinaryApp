package com.github.CulinaryApp.views;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.github.CulinaryApp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegPage2Activity extends AppCompatActivity implements View.OnClickListener {

    private EditText FirstName;
    private EditText LastName;
    private Button rButton;
    private CheckBox AthleticL;
    private CheckBox VeganL;
    private CheckBox VegetL;
    private CheckBox MediL;
    private CheckBox KetoL;
    private CheckBox FlexiL;
    private Button PfpB;
    private Button BackgroundB;

    private Uri uri;
    private StorageTask storageTask;
    //private StorageReference storagePfp;



    private static final int RESULT_LOAD_PFP = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_page2);

        FirstName = (EditText)findViewById(R.id.fistName);
        LastName = (EditText)findViewById(R.id.lastName);

        AthleticL = (CheckBox)findViewById(R.id.athleticLifestyle);
        VeganL = (CheckBox)findViewById(R.id.veganLifestyle);
        VegetL = (CheckBox)findViewById(R.id.vegetarianLifestyle);
        MediL = (CheckBox)findViewById(R.id.mediDietLifestyle);
        KetoL = (CheckBox)findViewById(R.id.ketoDietLifestyle);
        FlexiL = (CheckBox)findViewById(R.id.flexDietLifestyle);

        PfpB = (Button)findViewById(R.id.pfpButton);
        BackgroundB = (Button)findViewById(R.id.backgroundPhotoButton);

        rButton = (Button)findViewById(R.id.userRegButton);

        //storagePfp = FirebaseStorage.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    }

    @Override
    public void onClick(View v) {

        String FName = FirstName.getText().toString().trim();
        String LName = LastName.getText().toString().trim();

        switch(v.getId()){
            //TODO pfp and background photo
            case R.id.userRegButton:
                if(FName.isEmpty()){
                    FirstName.setError("Field can't be empty!");
                    FirstName.requestFocus();
                    return;
                }
                else if(LName.isEmpty()){
                    LastName.setError("Field can't be empty!");
                    LastName.requestFocus();
                    return;
                }
                else{
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference("Users");
                    DatabaseReference hopperRef = ref.child(FirebaseAuth.getInstance().getUid());

                    //Create hashmap to store data in until upload
                    Map<String, Object> hopperUpdates = new HashMap<>();

                    //Store name info
                    hopperUpdates.put("First Name", FName);
                    hopperUpdates.put("Last Name", LName);

                    //Store diet/lifestyle info
                    ArrayList<String> lifestlyeList = new ArrayList<String>();
                    if(AthleticL.isChecked())
                        lifestlyeList.add("Athletic");
                    if(VeganL.isChecked())
                        lifestlyeList.add("Vegan");
                    if(VegetL.isChecked())
                        lifestlyeList.add("Vegetarian");
                    if(MediL.isChecked())
                        lifestlyeList.add("Mediterranean");
                    if(KetoL.isChecked())
                        lifestlyeList.add("Ketogenic");
                    if(FlexiL.isChecked())
                        lifestlyeList.add("Flexitarian");

                    if(!lifestlyeList.isEmpty())
                        hopperUpdates.put("Lifestyle",lifestlyeList);

                    hopperRef.updateChildren(hopperUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegPage2Activity.this, "User info successfully updated", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(RegPage2Activity.this, CategoriesActivity.class));
                            }
                            else
                                Toast.makeText(RegPage2Activity.this, "Failed to update user info", Toast.LENGTH_LONG).show();
                        }
                    });

                }
                break;

            case R.id.pfpButton:
                Intent pfpIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //TODO replace with non-deprecated method
                startActivityForResult(pfpIntent, RESULT_LOAD_PFP);
                break;

            case R.id.backgroundPhotoButton:

                break;
        }
    }

    //Function that's called when startActivityForResult is called to upload pfp and background image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_PFP && resultCode == RESULT_OK && data != null){
            //Uniform resource indicator
            Uri selectedImage = data.getData();

            final FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            //Creates a pfp reference under the users UID
            StorageReference pfpRef = storageRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            pfpRef.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful())
                        Toast.makeText(RegPage2Activity.this, "User image successfully uploaded", Toast.LENGTH_LONG).show();

                    else
                        Toast.makeText(RegPage2Activity.this, "Failed to upload user image", Toast.LENGTH_LONG).show();

                }
            });

        }

    }


}