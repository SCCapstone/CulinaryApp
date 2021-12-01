package com.github.CulinaryApp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private CircleImageView prof;
    private ImageView bgImg;
    boolean changingProfPic, changingBgImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        changingBgImg = false;
        changingProfPic = false;

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.clipboardFragmentHolder, ClipboardFragment.class, null)
                    .commit();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        bgImg = findViewById(R.id.profBackground);
        prof = findViewById(R.id.profPic);

        Button profChangeButton = findViewById(R.id.editAvatar);
        profChangeButton.setOnClickListener(profImgChanger);

        Button bgChangeButton = findViewById(R.id.editBGImg);
        bgChangeButton.setOnClickListener(bgImgChanger);

        FragmentContainerView clipboardContainer = findViewById(R.id.clipboardFragmentHolder);
        clipboardContainer.setOnFocusChangeListener((container, hasFocus) -> {
            if(!hasFocus)
                container.setVisibility(View.INVISIBLE);
        });

        Button clipboardButton = findViewById(R.id.toolbarClip);
        clipboardButton.setOnClickListener(v -> {
            clipboardContainer.setVisibility(View.VISIBLE);
            clipboardContainer.requestFocus();
        });
    }

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
                    Uri uri = data.getData();

                    // Use Uri object instead of File to avoid storage permissions
                    prof.setImageURI(uri);
                }
            } else if (changingBgImg) {
                changingBgImg = false;
                if (bgImg != null) {
                    //Image Uri will not be null for RESULT_OK
                    Uri uri = data.getData();

                    // Use Uri object instead of File to avoid storage permissions
                    bgImg.setImageURI(uri);
                }
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}