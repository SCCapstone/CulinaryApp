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
import com.github.CulinaryApp.models.Recipe;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class RecipeInstructionsActivity extends AppCompatActivity {

    private static final String KEY_LIKES = "likes";
    private static final String VALUE_DEFAULT_NONE_FOUND = "{}";
    private static final String FILENAME_ENCRYPTED_SHARED_PREFS = "secret_shared_prefs";

    private Recipe currentRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_instructions);

        ImageButton likeButton = findViewById(R.id.likeButton);
        likeButton.setOnClickListener(likeListener);

        currentRecipe = null;
    }

    @Override
    protected void onStart() {
        super.onStart();

        this.setCurrentRecipe(new Recipe()); //todo using this as placeholder, this should actually use intents or other means to either:
                                                                                        // get the id of the recipe user clicked on and build a recipe object
                                                                                        // get a full recipe object thru an Intent. This may require serialization, etc.
    }

    View.OnClickListener likeListener = view -> {
        SharedPreferences prefs = getSharedPrefs();
        String likes = prefs.getString(KEY_LIKES, VALUE_DEFAULT_NONE_FOUND);

        SharedPreferences.Editor editor = prefs.edit();

        String recipeJSON = recipeToJSON(this.getCurrentRecipe());

        editor.putString(KEY_LIKES, likes);
        editor.apply();
    };

    private static String recipeToJSON(Recipe currentRecipe) { //todo easy
        return "{}";
    }

    private void setCurrentRecipe(Recipe currentRecipe){
        this.currentRecipe = currentRecipe;
    }

    private Recipe getCurrentRecipe(){ //todo
        return this.currentRecipe;
    }

    private SharedPreferences getSharedPrefs(){
        SharedPreferences sharedPreferences = null;
        try {
            MasterKey key = new MasterKey.Builder(RecipeInstructionsActivity.this, MasterKey.DEFAULT_MASTER_KEY_ALIAS).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build();

            sharedPreferences = EncryptedSharedPreferences.create(
                    RecipeInstructionsActivity.this,
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


}