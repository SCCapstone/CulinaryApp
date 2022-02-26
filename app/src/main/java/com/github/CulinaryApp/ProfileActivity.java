package com.github.CulinaryApp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.HasDefaultViewModelProviderFactory;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;

import java.util.function.Consumer;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private CircleImageView prof;
    private ImageView bgImg;
//    private FragmentContainerView clipboardContainer, trendingContainer;
    boolean changingProfPic, changingBgImg;
    private String bio, displayName;
    private Uri pfpURI, bgURI;


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



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if(changingProfPic) {
                changingProfPic = false;
                if (prof != null) {
                    //Image Uri will not be null for RESULT_OK
                    pfpURI = data.getData();

                    // Use Uri object instead of File to avoid storage permissions
                    prof.setImageURI(pfpURI);
                }
            } else if (changingBgImg) {
                changingBgImg = false;
                if (bgImg != null) {
                    //Image Uri will not be null for RESULT_OK
                    bgURI = data.getData();

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