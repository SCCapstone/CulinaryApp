package com.github.CulinaryApp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.CulinaryApp.views.RecipeInstructionsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;

import com.github.dhaval2404.imagepicker.ImagePicker;

import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.net.URI;
import java.security.GeneralSecurityException;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private CircleImageView prof;
    private ImageView bgImg;
    boolean changingProfPic, changingBgImg;
    private String bio, displayName;
    private Uri pfpURI, bgURI;
    private StorageReference pfpRef, bgpRef;

    private static final String FILENAME_ENCRYPTED_SHARED_PREFS = "secret_shared_prefs";
    private static final String KEY_SHAREDPREFS_DISPLAY_NAME = "userDisplayName";
    private static final String KEY_SHAREDPREFS_BIO = "userBio";
    private static final String VALUE_SHAREDPREFS_DEFAULT_DISPLAY_NAME = "No Bio yet...";
    private static final String VALUE_SHAREDPREFS_DEFAULT_BIO = "New User...";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        changingBgImg = false;
        changingProfPic = false;
        pfpURI = null;
        bgURI = null;
        pfpRef = null;
        bgpRef = null;

        bio =  getSharedPrefs(this).getString(KEY_SHAREDPREFS_BIO, VALUE_SHAREDPREFS_DEFAULT_BIO);
        displayName = getSharedPrefs(this).getString(KEY_SHAREDPREFS_DISPLAY_NAME, VALUE_SHAREDPREFS_DEFAULT_DISPLAY_NAME);

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

        //buttons within preferences - potentially permanent removal of other preferences
        Button healthSettingsButton = findViewById(R.id.setHealth);
        Button lifestyleSettingsButton = findViewById(R.id.setLifestyle);

        //settings onclicks
        profChangeButton.setOnClickListener(profImgChanger);
        bgChangeButton.setOnClickListener(bgImgChanger);
        editBioButton.setOnClickListener(bioEditor);
        editDisplayNameButton.setOnClickListener(displayNameEditor);
        profileDisplayButton.setOnClickListener(displayProfileFull);

        //preferences onclicks
        healthSettingsButton.setOnClickListener(healthSettingsChanger);
        lifestyleSettingsButton.setOnClickListener(lifestyleSettingsChanger);

        loadAcctImgsFromFirebase();
    }

    private void loadAcctImgsFromFirebase(){
        //Load Pfp and Bgp
        final FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        //Creates a pfp reference under the users UID
        StorageReference pfpRef = storageRef.child("Pfp").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        //Checks if user has uploaded a pfp and loads a default picture if not
        pfpRef.getDownloadUrl().addOnSuccessListener(uri -> loadPfp(pfpRef)).addOnFailureListener(exception -> loadPfp(storageRef.child("DefaultPfp.jpg")));

        //Create a bgp reference under the users UID
        //No check if reference exist as we don't currently have a default background to use
        StorageReference bgpRef = storageRef.child("Bgp").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        loadBgp(bgpRef);
    }

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

    private interface ValueSetter { void set(String value); }

    private void save(String key, String value, ValueSetter setter){
        setter.set(value);
        getSharedPrefs(this).edit().putString(key, value).apply();
    }

    View.OnClickListener displayNameEditor = view -> textFromDialog("Edit Display Name", "New Display Name Here", KEY_SHAREDPREFS_DISPLAY_NAME, (dialogInput) -> this.displayName = dialogInput);

    View.OnClickListener bioEditor = view -> textFromDialog("Edit Bio", "Your Bio Here", KEY_SHAREDPREFS_BIO, (output) -> this.bio = output);

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

    private static SharedPreferences getSharedPrefs(Context context){
        SharedPreferences sharedPreferences = null;
        try {
            MasterKey key = new MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build();

            sharedPreferences = EncryptedSharedPreferences.create(
                    context,
                    FILENAME_ENCRYPTED_SHARED_PREFS,
                    key,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }

        return sharedPreferences;
    }

    public String[] getProfileStrings(){
        return new String[] {displayName, bio};
    }

    public Uri[] getUris(){
        return new Uri[] {bgURI, pfpURI};
    }

    private void textFromDialog(String title, String hint, String valueBeingSaved, ValueSetter setter){
        AlertDialog.Builder textInputDialog = new AlertDialog.Builder(this);
        textInputDialog.setTitle(title);

        EditText newTextInput = new EditText(ProfileActivity.this);
        newTextInput.setHint(hint);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        int padding = getPixelsFromDp(this,32);
        layout.setPadding(padding, padding, padding, padding);

        layout.addView(newTextInput);
        textInputDialog.setView(layout);

        textInputDialog.setPositiveButton("Done", (dialog, choice) -> save(valueBeingSaved, newTextInput.getText().toString(), setter));
        textInputDialog.setNegativeButton("Cancel", (dialog, choice) -> dialog.cancel());
        textInputDialog.show();
    }

    public static int getPixelsFromDp(Context context, int densityPoints){
        final float DP_CONSTANT = 0.5f;
        float scale = context.getResources().getDisplayMetrics().density;
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

    private Uri updatePersonalizedDisplay(Intent data, StorageReference storageRef, ImageView viewToUpdate, String key){
        Uri uri = null;
        if (viewToUpdate != null) {
            //Image Uri will not be null for RESULT_OK
            uri = data.getData();

            StorageReference pfpRef = storageRef.child(key).child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            pfpRef.putFile(uri).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "User image successfully uploaded", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(ProfileActivity.this, "Failed to upload user image", Toast.LENGTH_LONG).show();

            });

            // Use Uri object instead of File to avoid storage permissions
            viewToUpdate.setImageURI(uri);
        }

        return uri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            final FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();

            if(changingProfPic) {
                changingProfPic = false;
                this.pfpURI = updatePersonalizedDisplay(data, storageRef, prof, "Pfp");

            } else if (changingBgImg) {
                changingBgImg = false;
                this.bgURI = updatePersonalizedDisplay(data, storageRef, bgImg, "Bgp");
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    /*@Override
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

                    pfpRef.putFile(pfpURI).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ProfileActivity.this, "User image successfully uploaded", Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(ProfileActivity.this, "Failed to upload user image", Toast.LENGTH_LONG).show();

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

                    bgpRef.putFile(bgURI).addOnCompleteListener(task -> {
                        if (task.isSuccessful())
                            Toast.makeText(ProfileActivity.this, "User image successfully uploaded", Toast.LENGTH_LONG).show();

                        else
                            Toast.makeText(ProfileActivity.this, "Failed to upload user image", Toast.LENGTH_LONG).show();

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
    }*/
}