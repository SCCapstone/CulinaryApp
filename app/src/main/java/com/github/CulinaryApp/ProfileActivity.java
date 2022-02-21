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

        //fragment containers for navbar
//        clipboardContainer = findViewById(R.id.clipboardFragmentHolder);
//        trendingContainer = findViewById(R.id.trendingFragmentHolder);
        getSupportFragmentManager().beginTransaction().add(R.id.Navbar, NavbarFragment.class, null).commit();

        //buttons within settings
        Button profChangeButton = findViewById(R.id.editAvatar);
        Button bgChangeButton = findViewById(R.id.editBGImg);
        Button editBioButton = findViewById(R.id.editBio);
        Button editDisplayNameButton = findViewById(R.id.editDispName);
        Button profileDisplayButton = findViewById(R.id.howTheySee);

        //buttons within navbar
//        Button clipboardButton = findViewById(R.id.toolbarClip);
//        Button trendingButton = findViewById(R.id.toolbarTrending);
////        Button searchButton = findViewById(R.id.toolbarSearch);
//        Button profileButton = findViewById(R.id.toolbarProfile);

        //buttons within preferences
        Button healthSettingsButton = findViewById(R.id.setHealth);
        Button lifestyleSettingsButton = findViewById(R.id.setLifestyle);
        Button activitySettingsButton = findViewById(R.id.setActivity);
        Button privacySettingsButton = findViewById(R.id.setPrivacy);

        //navbar onclicks
//        clipboardButton.setOnClickListener(toggleClipboard);
//        trendingButton.setOnClickListener(toggleTrending);
//        profileButton.setOnClickListener(navToProf);
//        searchButton.setOnClickListener(v -> System.out.println(this.getCurrentFocus()));

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

        //these allow the "tap button -> popup appears -> tap outside of popup -> popup disappears" action
//        clipboardContainer.setOnFocusChangeListener(toolbarFocusListener);
//        trendingContainer.setOnFocusChangeListener(toolbarFocusListener);
    }

    /*View.OnClickListener navToProf = view -> {
        Intent goToProfile = new Intent(this, ProfileActivity.class);
        startActivity(goToProfile);

        //todo remove when toolbar is changed to a fragment, and then check the instance of the fragment parent to see if button needs to be non-functional if ur already on profile page
        finish();
    };*/

    View.OnFocusChangeListener toolbarFocusListener = (container, hasFocus) -> {
        if(!hasFocus)
            container.setVisibility(View.GONE);
    };

    /*View.OnClickListener toggleTrending = view -> {
        FragmentManager manager = getSupportFragmentManager();
        int visibility = trendingContainer.getVisibility();

        if(visibility==View.GONE){
            manager.beginTransaction()
                    .add(R.id.trendingFragmentHolder, TrendingFragment.class, null)
                    .commit();

            trendingContainer.setVisibility(View.VISIBLE);
            trendingContainer.requestFocus();
        } else if (visibility == View.VISIBLE){
            manager.beginTransaction()
                    .remove(manager.findFragmentById(R.id.trendingFragmentHolder))
                    .commit();

            trendingContainer.setVisibility(View.GONE);

        }
    };*/

    /*View.OnClickListener toggleClipboard = dummy -> {
        FragmentManager manager = getSupportFragmentManager();
        int visibility = clipboardContainer.getVisibility();

        if(visibility==View.GONE){
            manager.beginTransaction()
                    .add(R.id.clipboardFragmentHolder, ClipboardFragment.class, null)
                    .commit();

            clipboardContainer.setVisibility(View.VISIBLE);
            clipboardContainer.requestFocus();
        } else if (visibility == View.VISIBLE){
            manager.beginTransaction()
                    .remove(manager.findFragmentById(R.id.clipboardFragmentHolder))
                    .commit();

            clipboardContainer.setVisibility(View.GONE);

        }
    };*/

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

    View.OnClickListener displayNameEditor = view -> {
        textFromDialog("Edit Display Name", "New Display Name Here", (output) -> this.displayName = output);
    };

    View.OnClickListener bioEditor = view -> {
        textFromDialog("Edit Bio", "Your Bio Here", (output) -> this.bio = output);
    };

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

    private String textFromDialog(String title, String hint, setValue setter){
        final String[] textRetrieved = {"Default Text"};

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

        return textRetrieved[0];
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