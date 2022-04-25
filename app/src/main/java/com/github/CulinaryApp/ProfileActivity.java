package com.github.CulinaryApp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;

import com.github.dhaval2404.imagepicker.ImagePicker;

import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Main Profile activity class, caries out all basic profile functions
 */
public class ProfileActivity extends AppCompatActivity {
    private CircleImageView prof;
    private ImageView bgImg;
    boolean changingProfPic, changingBgImg;
    private String bio, displayName;
//    private Uri pfpURI, bgURI;
    private StorageReference pfpRef, bgpRef;
    private boolean typing = false;

    private static final String FILENAME_ENCRYPTED_SHARED_PREFS = "secret_shared_prefs";
    private static final String KEY_SHAREDPREFS_DISPLAY_NAME = "userDisplayName";
    private static final String KEY_SHAREDPREFS_BIO = "userBio";
    private static final String KEY_FIREBASE_PFP = "Pfp";
    private static final String KEY_FIREBASE_BGIMG = "Bgp";
    private static final String VALUE_SHAREDPREFS_DEFAULT_DISPLAY_NAME = "New User";
    private static final String VALUE_SHAREDPREFS_DEFAULT_BIO = "No Bio yet...";
    private static final int LINES_DISPLAY_NAME = 1;
    private static final int LINES_BIO = 6;


    /**
     * Tells the profiles page what to do when the activity is called
     * @param savedInstanceState past saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set the activity to use the activity_profile xml
        setContentView(R.layout.activity_profile);
        changingBgImg = false;
        changingProfPic = false;
        pfpRef = null;
        bgpRef = null;

        //get the bio if already set by the user
        bio =  getSharedPrefs(this).getString(KEY_SHAREDPREFS_BIO, VALUE_SHAREDPREFS_DEFAULT_BIO);
        //get the user display name
        displayName = getSharedPrefs(this).getString(KEY_SHAREDPREFS_DISPLAY_NAME, VALUE_SHAREDPREFS_DEFAULT_DISPLAY_NAME);

    }

    /**
     * Tells the app what to do when the activity is called, similar to onCreate just doesnt hold past instance
     */
    @Override
    protected void onStart() {
        super.onStart();

        //global values, need these for other functions' uses
        //non button related
        prof = findViewById(R.id.profPic);
        bgImg = findViewById(R.id.profBackground);

        //buttons within settings
        findViewById(R.id.editAvatar).setOnClickListener(profImgChanger);
        findViewById(R.id.editBGImg).setOnClickListener(bgImgChanger);
        findViewById(R.id.editBio).setOnClickListener(bioEditor);
        findViewById(R.id.editDispName).setOnClickListener(displayNameEditor);
        findViewById(R.id.howTheySee).setOnClickListener(displayProfileFull);

        //buttons within preferences - potentially permanent removal of other preferences
        findViewById(R.id.updatePreferences).setOnClickListener(updatePreferenceSettings);
        loadAcctImgsFromFirebase();
    }

    /**
     * Takes in an image and gets the bitmap to be displayed
     * @param img
     * @return created bitMap
     */
    private Bitmap getBitmapFromVectorDrawable(VectorDrawable img){
        return Bitmap.createBitmap(
                img.getIntrinsicWidth(),
                img.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
    }

    /**
     * Get the bitmaps from storage to be used
     * @return drawables
     */
    public Bitmap[] getBmps() {
        Drawable profDrawable = prof.getDrawable();
        Drawable bgDrawable = bgImg.getDrawable();

        Bitmap[] bmps = new Bitmap[2];

        if(profDrawable instanceof VectorDrawable)
            bmps[0] = getBitmapFromVectorDrawable((VectorDrawable) profDrawable);
        else if(profDrawable instanceof BitmapDrawable)
            bmps[0] = ((BitmapDrawable) profDrawable).getBitmap();

        if(bgDrawable instanceof VectorDrawable)
            bmps[1] = getBitmapFromVectorDrawable((VectorDrawable) bgDrawable);
        else if(bgDrawable instanceof BitmapDrawable)
            bmps[1] = ((BitmapDrawable) bgDrawable).getBitmap();


        return bmps;
    }

    /**
     * Loads the account iages from firebase
     */
    private void loadAcctImgsFromFirebase(){
        //Load Pfp and Bgp
        final FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        //Creates a pfp reference under the users UID
        pfpRef = storageRef.child(KEY_FIREBASE_PFP).child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        //Checks if user has uploaded a pfp and loads a default picture if not
        loadImg(pfpRef, prof);

        //Create a bgp reference under the users UID
        //No check if reference exist as we don't currently have a default background to use
        bgpRef = storageRef.child(KEY_FIREBASE_BGIMG).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        loadImg(bgpRef, bgImg);
    }



    View.OnClickListener updatePreferenceSettings = view-> navigateToUpdatePreferences();

    /**
     * switches current view to update preferences activity
     */
    public void navigateToUpdatePreferences(){
        Intent intentToGoToUpdatePreferences = new Intent(this, PreferencesActivity.class);
        startActivity(intentToGoToUpdatePreferences);
    }


    View.OnClickListener activitySettingsChanger = view -> {
        //track recipe history switch
        androidx.appcompat.widget.SwitchCompat historySwitch = new androidx.appcompat.widget.SwitchCompat(this);
        historySwitch.setText("Track my history");

        showGenericDialog("Activity Settings", historySwitch);
    };

    /**
     * Below are the onclick listeners for the buttons
     */
    View.OnClickListener privacySettingsChanger = view -> {
        //private acct switch
        androidx.appcompat.widget.SwitchCompat privateSwitch = new androidx.appcompat.widget.SwitchCompat(this);
        privateSwitch.setText("Private Account");

        showGenericDialog("Privacy Settings", privateSwitch);
    };

    View.OnClickListener displayProfileFull = view ->{

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

    /**
     * saves current images
     * @param key
     * @param value
     * @param setter
     */
    private void save(String key, String value, ValueSetter setter){
        setter.set(value);
        getSharedPrefs(this).edit().putString(key, value).apply();
    }

    View.OnClickListener displayNameEditor = view -> getStringFromDialog("Edit Display Name", "New Display Name Here", LINES_DISPLAY_NAME, KEY_SHAREDPREFS_DISPLAY_NAME, (dialogInput) -> this.displayName = dialogInput);

    View.OnClickListener bioEditor = view -> getStringFromDialog("Edit Bio", "Your Bio Here", LINES_BIO, KEY_SHAREDPREFS_BIO, (output) -> this.bio = output);

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

    /**
     * Saves the current preferences to be retrieved next time the activity is opened
     * @param context
     * @return all the saved preferences
     */
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

    /**
     * Get the users profile name and bio
     * @return
     */
    public String[] getProfileStrings(){
        return new String[] {displayName, bio};
    }


    private void getStringFromDialog(String title, String hint, int lines, String valueBeingSaved, ValueSetter setter){
        AlertDialog.Builder textInputDialog = new AlertDialog.Builder(this);
        textInputDialog.setTitle(title);

        EditText newTextInput = new EditText(ProfileActivity.this);
        newTextInput.setHint(hint);
        newTextInput.setLines(lines);
        newTextInput.setMinLines(lines);

        newTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                typing = !newTextInput.getText().toString().trim().equals("");
            }
        });

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        int padding = getPixelsFromDp(this,32);
        layout.setPadding(padding, padding, padding, padding);

        layout.addView(newTextInput);
        textInputDialog.setView(layout);

        textInputDialog.setPositiveButton("Done", (dialog, choice) -> {
            String inputText = newTextInput.getText().toString();
            save(valueBeingSaved, inputText, setter);
        });
        textInputDialog.setNegativeButton("Cancel", (dialog, choice) -> dialog.cancel());

        AlertDialog dialog = textInputDialog.create();
        dialog.show();




    }

    /**
     * Generic dialog options for the profile page, what is automatically set before the user updates
     * @param title
     * @param toAdd
     */
    private void showGenericDialog(String title, View toAdd){
        AlertDialog.Builder textInputDialog = new AlertDialog.Builder(this);
        textInputDialog.setTitle(title);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        int padding = getPixelsFromDp(this,32);
        layout.setPadding(padding, padding, padding, padding);
        layout.addView(toAdd);

        textInputDialog.setView(layout);

        textInputDialog.setPositiveButton("Done", (dialog, choice) -> dialog.dismiss());

        textInputDialog.setNegativeButton("Cancel", (dialog, choice) -> dialog.cancel());

        textInputDialog.show();
    }

    public static int getPixelsFromDp(Context context, int densityPoints){
        final float DP_CONSTANT = 0.5f;
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(densityPoints*scale + DP_CONSTANT);
    }

    /**
     * this method temporarily changes what is loaded into the imageviews for avatar and bgimg
     * this must be done bc the image is not updating with the firebase until onstart runs again
     * That has to be circumvented somehow
     * @param into
     * @param imgUri
     */
    private void loadImg(ImageView into, Uri imgUri){
        try {
            Bitmap bmp = BitmapFactory.decodeStream(new URL(imgUri.toString()).openConnection().getInputStream());

            runOnUiThread(() -> into.setImageBitmap(bmp));
            //System.out.println("-------------------------------yo------------------------");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * this method does the same thing as load img, it just acquires the URI from firebase
     * this happens this way bc the only way to get the uri from Firebase is to use .getDownloadUrl()
     * this method is only used when initially loading the image from the website, otherwise, when the uri is already at hand, use the other loadimg
     * @param reference
     * @param into
     */
    private void loadImg(StorageReference reference, ImageView into){
        reference.getDownloadUrl().addOnSuccessListener((uri) -> new Thread( () -> {
            loadImg(into, uri);
        }).start());
    }


    private void updatePersonalizedData(Intent data, StorageReference storageRef, ImageView viewToUpdate, String key){
        if (viewToUpdate != null) {
            //Image Uri will not be null for RESULT_OK
            Uri imgUri = data.getData();

            StorageReference viewRef = storageRef.child(key).child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            UploadTask upload = viewRef.putFile(imgUri);

            upload.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    //Toast.makeText(this, "Image successfully updated", Toast.LENGTH_LONG).show();
                    loadImg(viewToUpdate, imgUri);
                } else
                    Toast.makeText(this, "Failed to your image", Toast.LENGTH_LONG).show();

            });


        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            final FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();

            if(changingProfPic) {
                changingProfPic = false;
                updatePersonalizedData(data, storageRef, prof, KEY_FIREBASE_PFP);

            } else if (changingBgImg) {
                changingBgImg = false;
                updatePersonalizedData(data, storageRef, bgImg, KEY_FIREBASE_BGIMG);
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            //Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

}