package com.github.CulinaryApp.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
import androidx.security.crypto.MasterKeys;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.github.CulinaryApp.R;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class RecipeInstructionsActivity extends AppCompatActivity {

    private static final String KEY_LIKES = "likes";
    private static final String VALUE_DEFAULT_NONE_FOUND = "{}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_instructions);

        ImageButton likeButton = findViewById(R.id.likeButton);
        likeButton.setOnClickListener(likeListener);
    }

    View.OnClickListener likeListener = view -> {
        SharedPreferences prefs = getSharedPrefs();
        String likes = prefs.getString(KEY_LIKES, VALUE_DEFAULT_NONE_FOUND);

        SharedPreferences.Editor editor = prefs.edit();
        //todo current get recipe in object form. We will need to have this object form to fit the recipe for when it's displayed in this activity as well
        //
        //
        //todo I'll convert to JSON and string form and stuff. This class is not quite ready to run but almost

        editor.putString(KEY_LIKES, likes);
        editor.apply();
    };

    private SharedPreferences getSharedPrefs(){
        SharedPreferences sharedPreferences = null;
        try {
            MasterKey key = new MasterKey.Builder(RecipeInstructionsActivity.this, MasterKey.DEFAULT_MASTER_KEY_ALIAS).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build();

            sharedPreferences = EncryptedSharedPreferences.create(
                    RecipeInstructionsActivity.this,
                    "secret_shared_prefs",
                    key,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }

        return sharedPreferences;
    }


}