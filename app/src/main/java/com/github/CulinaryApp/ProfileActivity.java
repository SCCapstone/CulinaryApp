package com.github.CulinaryApp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private CircleImageView prof;
    private ImageView bgImg;
    private FragmentContainerView clipboardContainer;
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

//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .setReorderingAllowed(true)
//                    .add(R.id.profileDisplayContainer, ProfileDisplayFragment.class, null)
//                    .commit();
//        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        bgImg = findViewById(R.id.profBackground);
        prof = findViewById(R.id.profPic);

        clipboardContainer = findViewById(R.id.clipboardFragmentHolder);
        LinearLayout content = findViewById(R.id.contentLayout);

        Button profChangeButton = findViewById(R.id.editAvatar);
        Button bgChangeButton = findViewById(R.id.editBGImg);
        Button clipboardButton = findViewById(R.id.toolbarClip);
        Button editBioButton = findViewById(R.id.editBio);
        Button editDisplayNameButton = findViewById(R.id.editDispName);
        Button profileDisplayButton = findViewById(R.id.howTheySee);
        Button searchButton = findViewById(R.id.toolbarSearch);

        profChangeButton.setOnClickListener(profImgChanger);
        bgChangeButton.setOnClickListener(bgImgChanger);
        clipboardButton.setOnClickListener(toggleClipboardVis);
        editBioButton.setOnClickListener(bioEditor);
        editDisplayNameButton.setOnClickListener(displayNameEditor);
        profileDisplayButton.setOnClickListener(displayProfileFull);

        clipboardContainer.setOnFocusChangeListener(clipboardFocusListener);

        //todo delete or replace
        searchButton.setOnClickListener(v -> System.out.println(this.getCurrentFocus()));

    }

    public String[] getProfileStrings(){
        return new String[] {displayName, bio};
    }

    public Uri[] getUris(){
        return new Uri[] {bgURI, pfpURI};
    }

    View.OnClickListener displayProfileFull = view ->{
//        findViewById(R.id.clipboardFragmentHolder).setVisibility(View.VISIBLE);

        FragmentManager manager = getSupportFragmentManager();

        manager.beginTransaction()
                .add(R.id.profileDisplayContainer, ProfileDisplayFragment.class, null)
//                .addSharedElement(bgImg, "bgImg")
//                .addSharedElement(prof, "prof")
                .commit();
    };

    private String textFromDialog(String title, String hint){
        final String[] textRetrieved = {"Default Text"};

        AlertDialog.Builder textInputDialog = new AlertDialog.Builder(this);
        textInputDialog.setTitle(title);

        EditText newTextInput = new EditText(ProfileActivity.this);
        newTextInput.setHint(hint);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        int dpPaddingSides = 32;
        float scale = getResources().getDisplayMetrics().density;
        int pixelPaddingSides = (int) (dpPaddingSides*scale + 0.5f);
        layout.setPadding(pixelPaddingSides, pixelPaddingSides, pixelPaddingSides, pixelPaddingSides);

        layout.addView(newTextInput);

        textInputDialog.setView(layout);

        textInputDialog.setPositiveButton("Done", (dialog, choice) -> {
            textRetrieved[0] = newTextInput.getText().toString();
        });

        textInputDialog.setNegativeButton("Cancel", (dialog, choice) -> {
            dialog.cancel();
        });

        textInputDialog.show();

        return textRetrieved[0];
    }

    View.OnClickListener displayNameEditor = view -> {
        displayName = textFromDialog("Edit Display Name", "New Display Name Here");
    };

    View.OnClickListener bioEditor = view -> {
        bio = textFromDialog("Edit Bio", "Your Bio Here");
    };

    View.OnFocusChangeListener clipboardFocusListener = (container, hasFocus) -> {
        if(!hasFocus)
            container.setVisibility(View.GONE);
    };

    View.OnClickListener toggleClipboardVis = dummy -> {
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