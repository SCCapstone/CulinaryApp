package com.github.CulinaryApp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.github.CulinaryApp.views.RegPage2Activity;
import com.google.android.gms.dynamite.DynamiteModule;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.storage.FirebaseStorage;

import com.github.dhaval2404.imagepicker.ImagePicker;

import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.UploadTask;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private CircleImageView prof;
    private ImageView bgImg;
//    private FragmentContainerView clipboardContainer, trendingContainer;
    boolean changingProfPic, changingBgImg;
    private String bio, displayName;
    private Uri pfpURI, bgURI;
    private StorageReference pfpRef, bgpRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        changingBgImg = false;
        changingProfPic = false;
        bio = "No Bio yet...";
        displayName = "New User...";
        pfpURI = null;
        bgURI = null;
        pfpRef = null;
        bgpRef = null;

    }

    @Override
    protected void onStart() {
        super.onStart();

        //global values, need these for other functions' uses
        //non button related
        bgImg = findViewById(R.id.profBackground);
        prof = findViewById(R.id.profPic);

        //buttons within settings
        Button profChangeButton = findViewById(R.id.editAvatar);
        Button bgChangeButton = findViewById(R.id.editBGImg);
        Button editBioButton = findViewById(R.id.editBio);
        Button editDisplayNameButton = findViewById(R.id.editDispName);
        Button profileDisplayButton = findViewById(R.id.howTheySee);

        //buttons within preferences
        Button healthSettingsButton = findViewById(R.id.setHealth);
        Button lifestyleSettingsButton = findViewById(R.id.setLifestyle);
        Button activitySettingsButton = findViewById(R.id.setActivity);
        Button privacySettingsButton = findViewById(R.id.setPrivacy);

        //settings onclicks
        profChangeButton.setOnClickListener(profImgChanger);
        bgChangeButton.setOnClickListener(bgImgChanger);
        editBioButton.setOnClickListener(bioEditor);
        editDisplayNameButton.setOnClickListener(displayNameEditor);
        profileDisplayButton.setOnClickListener(displayProfileFull);

        //preferences onclicks
        healthSettingsButton.setOnClickListener(healthSettingsChanger);
        lifestyleSettingsButton.setOnClickListener(lifestyleSettingsChanger);
        activitySettingsButton.setOnClickListener(activitySettingsChanger);
        privacySettingsButton.setOnClickListener(privacySettingsChanger);


        //Load Pfp and Bgp
        final FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        //Creates a pfp reference under the users UID
        StorageReference pfpRef = storageRef.child("Pfp").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        //Checks if user has uploaded a pfp and loads a default picture if not
        pfpRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                loadPfp(pfpRef);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                loadPfp(storageRef.child("DefaultPfp.jpg"));
            }
        });


        //Create a bgp reference under the users UID
        //No check if reference exist as we don't currently have a default background to use
        StorageReference bgpRef = storageRef.child("Bgp").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        loadBgp(bgpRef);
    }


    View.OnFocusChangeListener toolbarFocusListener = (container, hasFocus) -> {
        if(!hasFocus)
            container.setVisibility(View.GONE);
    };

    View.OnClickListener healthSettingsChanger = view -> {
        androidx.appcompat.widget.SwitchCompat glutenSwitch = new androidx.appcompat.widget.SwitchCompat(this);
        glutenSwitch.setText("Gluten-free mode");

        showGenericDialog("Health Settings", glutenSwitch);
    };

    View.OnClickListener lifestyleSettingsChanger = view -> {
        //track calories switch
        androidx.appcompat.widget.SwitchCompat caloriesSwitch = new androidx.appcompat.widget.SwitchCompat(this);
        caloriesSwitch.setText("Track my Caloric intake");

        showGenericDialog("Lifestyle Settings", caloriesSwitch);
    };

    View.OnClickListener activitySettingsChanger = view -> {
        //track recipe history switch
        androidx.appcompat.widget.SwitchCompat historySwitch = new androidx.appcompat.widget.SwitchCompat(this);
        historySwitch.setText("Track my history");

        showGenericDialog("Activity Settings", historySwitch);
    };

    View.OnClickListener privacySettingsChanger = view -> {
        //private acct switch
        androidx.appcompat.widget.SwitchCompat privateSwitch = new androidx.appcompat.widget.SwitchCompat(this);
        privateSwitch.setText("Private Account");

        showGenericDialog("Privacy Settings", privateSwitch);
    };

    View.OnClickListener displayProfileFull = view ->{
//        findViewById(R.id.clipboardFragmentHolder).setVisibility(View.VISIBLE);

        FragmentManager manager = getSupportFragmentManager();

        ViewCompat.setTransitionName(bgImg, "bgImg");
        ViewCompat.setTransitionName(prof, "prof");

        manager.beginTransaction()
                .add(R.id.profileDisplayContainer, ProfileDisplayFragment.class, null)
                .addSharedElement(bgImg, "bgImg")
                .addSharedElement(prof, "prof")
                .commit();
    };

    View.OnClickListener displayNameEditor = view -> textFromDialog("Edit Display Name", "New Display Name Here", (output) -> this.displayName = output);

    View.OnClickListener bioEditor = view -> textFromDialog("Edit Bio", "Your Bio Here", (output) -> this.bio = output);

    View.OnClickListener profImgChanger = view -> {
        changingProfPic = true;
        ImagePicker.with(ProfileActivity.this)
                .cropSquare()
                .compress(2048)
                .maxResultSize(1080, 1080)
                .start();
    };

    View.OnClickListener bgImgChanger = view -> {
        changingBgImg = true;

        float imgWidth = bgImg.getWidth();
        float imgHeight = bgImg.getHeight();

        ImagePicker.with(ProfileActivity.this)
                .crop(imgWidth, imgHeight)
                .compress(2048)
                .maxResultSize(1080, 1080)
                .start();
    };

    public String[] getProfileStrings(){
        return new String[] {displayName, bio};
    }

    public Uri[] getUris(){
        return new Uri[] {bgURI, pfpURI};
    }

    private interface setValue {
        void set(String value);
    }

    private void textFromDialog(String title, String hint, setValue setter){
        AlertDialog.Builder textInputDialog = new AlertDialog.Builder(this);
        textInputDialog.setTitle(title);

        EditText newTextInput = new EditText(ProfileActivity.this);
        newTextInput.setHint(hint);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        int padding = getPixelsFromDp(32);
        layout.setPadding(padding, padding, padding, padding);

        layout.addView(newTextInput);
        textInputDialog.setView(layout);

        textInputDialog.setPositiveButton("Done", (dialog, choice) -> setter.set(newTextInput.getText().toString()));
        textInputDialog.setNegativeButton("Cancel", (dialog, choice) -> dialog.cancel());
        textInputDialog.show();
    }

    private int getPixelsFromDp(int densityPoints){
        final float DP_CONSTANT = 0.5f;
        float scale = getResources().getDisplayMetrics().density;
        return (int)(densityPoints*scale + DP_CONSTANT);
    }

    private void showGenericDialog(String title, View toAdd){
        AlertDialog.Builder textInputDialog = new AlertDialog.Builder(this);
        textInputDialog.setTitle(title);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        int dpPaddingSides = 32;
        float scale = getResources().getDisplayMetrics().density;
        int pixelPaddingSides = (int) (dpPaddingSides*scale + 0.5f);
        layout.setPadding(pixelPaddingSides, pixelPaddingSides, pixelPaddingSides, pixelPaddingSides);
        layout.addView(toAdd);

        textInputDialog.setView(layout);

        textInputDialog.setPositiveButton("Done", (dialog, choice) -> {
            dialog.dismiss();
        });

        textInputDialog.setNegativeButton("Cancel", (dialog, choice) -> {
            dialog.cancel();
        });

        textInputDialog.show();
    }

    public void loadPfp(StorageReference pfpRef){
        Glide.with(this /* context */)
                .load(pfpRef)
                .into(prof);
    }

    public void loadBgp(StorageReference bgpRef){
        Glide.with(this /* context */)
                .load(bgpRef)
                .into(bgImg);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            final FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();

            if(changingProfPic) {
                changingProfPic = false;
                if (prof != null) {
                    //Image Uri will not be null for RESULT_OK
                    pfpURI = data.getData();


                    StorageReference pfpRef = storageRef.child("Pfp").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                    pfpRef.putFile(pfpURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ProfileActivity.this, "User image successfully uploaded", Toast.LENGTH_LONG).show();
                            } else
                                Toast.makeText(ProfileActivity.this, "Failed to upload user image", Toast.LENGTH_LONG).show();

                        }
                    });

                    // Use Uri object instead of File to avoid storage permissions
                    prof.setImageURI(pfpURI);
                }
            } else if (changingBgImg) {
                changingBgImg = false;
                if (bgImg != null) {
                    //Image Uri will not be null for RESULT_OK
                    bgURI = data.getData();

                    StorageReference bgpRef = storageRef.child("Bgp").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                    bgpRef.putFile(bgURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful())
                                Toast.makeText(ProfileActivity.this, "User image successfully uploaded", Toast.LENGTH_LONG).show();

                            else
                                Toast.makeText(ProfileActivity.this, "Failed to upload user image", Toast.LENGTH_LONG).show();

                        }
                    });

                    // Use Uri object instead of File to avoid storage permissions
                    bgImg.setImageURI(bgURI);
                }
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}